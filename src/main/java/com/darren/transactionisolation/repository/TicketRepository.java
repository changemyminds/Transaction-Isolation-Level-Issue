package com.darren.transactionisolation.repository;

import com.darren.transactionisolation.isolation.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: changemyminds.
 * Date: 2021/4/30.
 * Description:
 * Reference:
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
