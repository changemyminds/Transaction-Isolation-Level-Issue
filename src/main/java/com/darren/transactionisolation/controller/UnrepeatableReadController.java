package com.darren.transactionisolation.controller;

import com.darren.transactionisolation.isolation.Inventory;
import com.darren.transactionisolation.log.LogTopic;
import com.darren.transactionisolation.service.UnrepeatableReadService;
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
@RequestMapping("/unrepeatable-read")
@RequiredArgsConstructor
@Slf4j(topic = LogTopic.UNREPEATABLE_READ)
public class UnrepeatableReadController {
    private final UnrepeatableReadService unrepeatableReadService;

    @GetMapping("/acquire/{id}")
    public Inventory acquireInventory(@PathVariable Long id, @RequestParam String isolation) {
        log.info("Isolation {}, ", Isolation.valueOf(isolation));
        switch (Isolation.valueOf(isolation)) {
            case READ_UNCOMMITTED:
                return unrepeatableReadService.acquireREAD_UNCOMMITTED(id);
            case READ_COMMITTED:
                return unrepeatableReadService.acquireREAD_COMMITTED(id);
            case REPEATABLE_READ:
                return unrepeatableReadService.acquireREPEATABLE_READ(id);
            case SERIALIZABLE:
                return unrepeatableReadService.acquireSERIALIZABLE(id);
            case DEFAULT:
            default:
                return unrepeatableReadService.acquireDEFAULT(id);
        }
    }

    @PostMapping("/purchase/{id}")
    public void purchaseInventory(@PathVariable Long id, @RequestParam Integer quantity) {
        unrepeatableReadService.purchase(id, quantity);
    }
}
