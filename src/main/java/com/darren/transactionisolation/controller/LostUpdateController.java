package com.darren.transactionisolation.controller;

import com.darren.transactionisolation.isolation.Ticket;
import com.darren.transactionisolation.log.LogTopic;
import com.darren.transactionisolation.service.LostUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.web.bind.annotation.*;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
@RestController
@RequestMapping("/lost-update")
@RequiredArgsConstructor
@Slf4j(topic = LogTopic.UNREPEATABLE_READ)
public class LostUpdateController {
    private final LostUpdateService lostUpdateService;

    @PostMapping("/sell-ticket/{id}")
    public void SellTicket(@PathVariable Long id,
                         @RequestParam Integer sellCount,
                         @RequestParam String isolation,
                         @RequestParam Boolean isT1) {
        log.info("Isolation {}, ", Isolation.valueOf(isolation));
        switch (Isolation.valueOf(isolation)) {
            case READ_UNCOMMITTED:
                lostUpdateService.sellTicketREAD_UNCOMMITTED(id, sellCount, isT1);
                return;
            case READ_COMMITTED:
                lostUpdateService.sellTicketREAD_COMMITTED(id, sellCount, isT1);
                return;
            case REPEATABLE_READ:
                lostUpdateService.sellTicketREPEATABLE_READ(id, sellCount, isT1);
                return;
            case SERIALIZABLE:
                lostUpdateService.sellTicketSERIALIZABLE(id, sellCount, isT1);
                return;
            case DEFAULT:
            default:
                lostUpdateService.sellTicketDEFAULT(id, sellCount, isT1);
        }
    }

    @GetMapping("/ticket/{id}")
    public Ticket getRemainingTicket(@PathVariable Long id) {
        return lostUpdateService.getRemainingTicket(id);
    }
}
