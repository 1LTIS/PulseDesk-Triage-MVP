package com.pulsedesk.triage.repository;
import com.pulsedesk.triage.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {}