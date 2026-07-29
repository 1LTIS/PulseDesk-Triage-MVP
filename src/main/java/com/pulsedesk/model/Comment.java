package com.pulsedesk.triage.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    private String channel; // e.g., app reviews, web forms
    private LocalDateTime createdAt = LocalDateTime.now();
}