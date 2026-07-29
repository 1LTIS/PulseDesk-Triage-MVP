package com.pulsedesk.triage.controller;

import com.pulsedesk.triage.model.Comment;
import com.pulsedesk.triage.model.Ticket;
import com.pulsedesk.triage.repository.CommentRepository;
import com.pulsedesk.triage.repository.TicketRepository;
import com.pulsedesk.triage.service.HuggingFaceTriageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allows simple UI configurations to reach the endpoints seamlessly
public class TriageController {

    private final HuggingFaceTriageService triageService;
    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;

    public TriageController(HuggingFaceTriageService triageService, 
                            CommentRepository commentRepository, 
                            TicketRepository ticketRepository) {
        this.triageService = triageService;
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment processed = triageService.processComment(comment);
        return ResponseEntity.ok(processed);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllComments() {
        return ResponseEntity.ok(commentRepository.findAll());
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketRepository.findAll());
    }

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long ticketId) {
        return ticketRepository.findById(ticketId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}