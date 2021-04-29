package com.darren.transactionisolation.service;

import com.darren.transactionisolation.isolation.Account;
import com.darren.transactionisolation.log.LogTopic;
import com.darren.transactionisolation.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.EntityNotFoundException;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
@Service
@Slf4j(topic = LogTopic.DIRTY_READ)
@RequiredArgsConstructor
public class DirtyReadService extends BaseService {
    private final AccountRepository accountRepository;

    @Transactional
    public void deposit(Long id, Integer amount) {
        Account account = getAccount(id);
        log.info("[Deposit (T1)] Read Account: {}", account);

        int beforeDepositAmount = account.getAmount();
        int afterDepositAmount = beforeDepositAmount + amount;
        account.setAmount(afterDepositAmount);
        accountRepository.saveAndFlush(account);
        log.info("[Deposit (T1)] {} + {} = {}", beforeDepositAmount, amount, afterDepositAmount);
        sleep(1.0);

        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        log.info("[Deposit (T1)] Rollback");
    }

    @Transactional(isolation = Isolation.DEFAULT)
    public void withdrawDefault(Long id, Integer amount) {
        withdraw(id, amount);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void withdrawReadUncommitted(Long id, Integer amount) {
        withdraw(id, amount);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void withdrawReadCommitted(Long id, Integer amount) {
        withdraw(id, amount);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void withdrawRepeatableRead(Long id, Integer amount) {
        withdraw(id, amount);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void withdrawSerializable(Long id, Integer amount) {
        withdraw(id, amount);
    }

    public void withdraw(Long id, Integer amount) {
        sleep(0.5);
        Account account = getAccount(id);
        log.info("[Withdraw (T2)] Read Account: {}", account);

        sleep(1.0);
        int beforeWithdrawAmount = account.getAmount();
        int afterWithdrawAmount = account.getAmount() - amount;
        account.setAmount(afterWithdrawAmount);
        log.info("[Withdraw (T2)] {} - {} = {}", beforeWithdrawAmount, amount, afterWithdrawAmount);
        log.info("[Withdraw (T2)] Commit");
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find the Account id " + id));
    }
}
