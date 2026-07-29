package com.pulsedesk.triage.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String category; // bug / feature / billing / account / other
    private String priority; // low / medium / high
    
    @Column(columnDefinition = "TEXT")
    private String summary;
    
    @OneToOne
    @JoinColumn(name = "comment_id")
    private Comment originComment;
}