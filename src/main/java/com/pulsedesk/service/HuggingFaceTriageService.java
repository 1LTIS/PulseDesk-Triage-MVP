package com.pulsedesk.triage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulsedesk.triage.model.Comment;
import com.pulsedesk.triage.model.Ticket;
import com.pulsedesk.triage.repository.CommentRepository;
import com.pulsedesk.triage.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class HuggingFaceTriageService {

    @Value("${hf.api.url}")
    private String hfApiUrl;

    @Value("${hf.api.token}")
    private String hfApiToken;

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HuggingFaceTriageService(CommentRepository commentRepository, TicketRepository ticketRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
    }

    public Comment processComment(Comment comment) {
        // 1. Save the incoming comment
        Comment savedComment = commentRepository.save(comment);

        // 2. Call Hugging Face API for evaluation
        try {
            String aiAnalysis = callHuggingFace(savedComment.getContent());
            Map<String, Object> parsedResult = parseAiResponse(aiAnalysis);

            boolean isTicket = (boolean) parsedResult.getOrDefault("isTicket", false);

            if (isTicket) {
                Ticket ticket = new Ticket();
                ticket.setTitle((String) parsedResult.get("title"));
                ticket.setCategory((String) parsedResult.get("category"));
                ticket.setPriority((String) parsedResult.get("priority"));
                ticket.setSummary((String) parsedResult.get("summary"));
                ticket.setOriginComment(savedComment);
                ticketRepository.save(ticket);
            }
        } catch (Exception e) {
            // Log error, gracefully degrade or fallback to human triage (Defaulting to 'other' classification)
            System.err.println("AI Processing Failed: " + e.getMessage());
        }

        return savedComment;
    }

    private String callHuggingFace(String commentText) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + hfApiToken);

        String systemPrompt = "Analyze this user comment. Determine if it's an actionable technical/customer issue requiring a ticket. " +
                "Respond ONLY with a valid JSON object matching this structure: " +
                "{\"isTicket\":true/false, \"title\":\"Short Title\", \"category\":\"bug/feature/billing/account/other\", \"priority\":\"low/medium/high\", \"summary\":\"brief summary\"}. " +
                "Do not include code block syntax or markdown backticks around the JSON. Comment: ";

        Map<String, Object> body = new HashMap<>();
        body.put("inputs", systemPrompt + commentText);
        
        // Optional parameters to prevent the model from getting creative
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("return_full_text", false);
        parameters.put("temperature", 0.1);
        body.put("parameters", parameters);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(hfApiUrl, entity, String.class);
        
        return response.getBody();
    }

    private Map<String, Object> parseAiResponse(String rawResponse) throws Exception {
        // Depending on the model variant, Hugging Face may return an array containing standard text fields
        // E.g., [{"generated_text": "{...}"}]
        if (rawResponse.startsWith("[")) {
            Map[] rawMapArray = objectMapper.readValue(rawResponse, Map[].class);
            String cleanJson = (String) rawMapArray[0].get("generated_text");
            // Clean up potential markdown formatting if returned by LLM
            cleanJson = cleanJson.replaceAll("```json|```", "").trim();
            return objectMapper.readValue(cleanJson, Map.class);
        }
        return objectMapper.readValue(rawResponse, Map.class);
    }
}