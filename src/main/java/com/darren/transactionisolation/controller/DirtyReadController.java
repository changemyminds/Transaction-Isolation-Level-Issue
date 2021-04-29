package com.darren.transactionisolation.controller;

import com.darren.transactionisolation.isolation.Account;
import com.darren.transactionisolation.log.LogTopic;
import com.darren.transactionisolation.service.DirtyReadService;
import com.darren.transactionisolation.service.SeedService;
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
@RequestMapping("/dirty-read")
@RequiredArgsConstructor
@Slf4j(topic = LogTopic.DIRTY_READ)
public class DirtyReadController {
    private final DirtyReadService dirtyReadService;
    private final SeedService seedService;

    @PostMapping("/deposit/{id}")
    public void deposit(@PathVariable Long id, @RequestParam Integer amount) {
        dirtyReadService.deposit(id, amount);
    }

    @PostMapping("/withdraw/{id}")
    public void withdraw(@PathVariable Long id, @RequestParam Integer amount, @RequestParam String isolation) {
        switch (Isolation.valueOf(isolation)) {
            case READ_UNCOMMITTED:
                dirtyReadService.withdrawReadUncommitted(id, amount);
                return;
            case READ_COMMITTED:
                dirtyReadService.withdrawReadCommitted(id, amount);
                return;
            case REPEATABLE_READ:
                dirtyReadService.withdrawRepeatableRead(id, amount);
                return;
            case SERIALIZABLE:
                dirtyReadService.withdrawSerializable(id, amount);
                return;
            case DEFAULT:
            default:
                dirtyReadService.withdrawDefault(id, amount);
        }
    }

    @PostMapping("/withdraw/{id}/repeatable-read")
    public void withdraw(@PathVariable Long id, @RequestParam Integer amount) {
        dirtyReadService.withdrawRepeatableRead(id, amount);
    }

    @GetMapping("account/{id}")
    public Account readResult(@PathVariable Long id) {
        Account account = dirtyReadService.getAccount(id);
        log.info("[Result] {}", account);
        return account;
    }
}
