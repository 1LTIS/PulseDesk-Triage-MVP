package com.pulsedesk.triage.repository;
import com.pulsedesk.triage.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {}