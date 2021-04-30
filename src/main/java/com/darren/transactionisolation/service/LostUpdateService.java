package com.darren.transactionisolation.service;

import com.darren.transactionisolation.isolation.Ticket;
import com.darren.transactionisolation.log.LogTopic;
import com.darren.transactionisolation.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransactionException;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
@Slf4j(topic = LogTopic.LOST_UPDATE)
@Service
@RequiredArgsConstructor
public class LostUpdateService extends BaseService {
    private final TicketRepository ticketRepository;

    // [SQLITE_BUSY]  The database file is locked (database is locked). So SQLite need retried it.
    @Retryable(value = {TransactionException.class}, maxAttempts = 2, backoff = @Backoff(delay = 100))
    @Transactional(isolation = Isolation.DEFAULT)
    public void sellTicketDEFAULT(Long id, Integer quantity, boolean isT1) {
        sellTicket(id, quantity, isT1);
    }

    // [SQLITE_BUSY]  The database file is locked (database is locked). So SQLite need retried it.
    @Retryable(value = {TransactionException.class}, maxAttempts = 2, backoff = @Backoff(delay = 100))
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void sellTicketREAD_UNCOMMITTED(Long id, Integer quantity, boolean isT1) {
        sellTicket(id, quantity, isT1);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void sellTicketREAD_COMMITTED(Long id, Integer quantity, boolean isT1) {
        sellTicket(id, quantity, isT1);
    }

    // (Postgresql) LockAcquisitionException: Deadlock found when trying to get lock; try restarting transaction
    @Retryable(value = LockAcquisitionException.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void sellTicketREPEATABLE_READ(Long id, Integer quantity, boolean isT1) {
        sellTicket(id, quantity, isT1);
    }

    // (MySQL、Postgresql) LockAcquisitionException: Deadlock found when trying to get lock; try restarting transaction
    // [SQLITE_BUSY]  The database file is locked (database is locked). So SQLite need retried it.
    @Retryable(value = {LockAcquisitionException.class, TransactionException.class}, maxAttempts = 2, backoff = @Backoff(delay = 100))
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void sellTicketSERIALIZABLE(Long id, Integer quantity, boolean isT1) {
        sellTicket(id, quantity, isT1);
    }

    public void sellTicket(Long id, Integer quantity, boolean isT1) {
        if (!isT1) sleep(0.5);

        Ticket ticket = getRemainingTicket(id);
        if (isT1) {
            log.info("[SellTicket (T1)] Read {}", ticket);
            sleep(1.0);
        } else {
            log.info("[SellTicket (T2)] Read {}", ticket);
            sleep(1.5);
        }

        Integer nowQuantity = ticket.getQuantity();
        ticket.setQuantity(nowQuantity - quantity);
        ticketRepository.saveAndFlush(ticket);

        if (isT1) {
            log.info("[SellTicket (T1)] Update selling quantity {}", ticket);
            log.info("[SellTicket (T1)] Commit");
        } else {
            log.info("[SellTicket (T2)] Update selling quantity {}", ticket);
            log.info("[SellTicket (T2)] Commit");
        }
    }

    public Ticket getRemainingTicket(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find the id " + id));
    }
}
