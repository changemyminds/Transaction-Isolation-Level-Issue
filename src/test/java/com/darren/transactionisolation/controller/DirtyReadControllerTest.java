package com.darren.transactionisolation.controller;

import com.darren.transactionisolation.isolation.Account;
import com.darren.transactionisolation.isolation.IsolationIssueDelegate;
import com.darren.transactionisolation.isolation.dirtyread.BaseDirtyRead;
import com.darren.transactionisolation.model.DirtyReadExpectOccur;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Isolation;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
public class DirtyReadControllerTest extends BaseIsolationControllerTest {
    private static final String DIRTY_READ_DEPOSIT_ENDPOINT = "/dirty-read/deposit";
    private static final String DIRTY_READ_WITHDRAW_ENDPOINT = "/dirty-read/withdraw";
    private static final String DIRTY_READ_ACCOUNT_ENDPOINT = "/dirty-read/account";

    private final DirtyReadExpectOccur expectOccur
            = new DirtyReadExpectOccur(0, 500, 300,
            -300, 200);

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private BaseDirtyRead dirtyRead;

    @BeforeAll
    @Override
    public void setupAll() {
        super.setupAll();
        System.out.println("[Reset Account]");
        seedService.resetAccount(expectOccur.getDefaultTotalAmount(), 5);

        System.out.println("=============================================");
        System.out.println("|  Deposit(T1)       | Withdraw(T2)         |");
        System.out.println("---------------------------------------------");
        System.out.println("|  Read(0)           |                      |");
        System.out.println("|  deposit(0 + 500)  |                      |");
        System.out.println("|  save              |                      |");
        System.out.println("|                    |  Read(500)           |");
        System.out.println("|  Rollback          |                      |");
        System.out.println("|                    |  Withdraw(500 - 300) |");
        System.out.println("|                    |  Commit              |");
        System.out.println("---------------------------------------------");
        System.out.println("[Not Occur Dirty Read] Amount is " + expectOccur.getCorrectTotalAmount());
        System.out.println("[Occur Dirty Read]     Amount is " + expectOccur.getIncorrectTotalAmount());
        System.out.println("=============================================");
    }

    @Test
    @Order(1)
    @Override
    public void test_DEFAULT() throws Exception {
        testDirtyRead(1, Isolation.DEFAULT, dirtyRead::assertDEFAULT);
    }

    @Test
    @Order(2)
    @Override
    public void test_READ_UNCOMMITTED() throws Exception {
        testDirtyRead(2, Isolation.READ_UNCOMMITTED, dirtyRead::assertREAD_UNCOMMITTED);
    }

    @Test
    @Order(3)
    @Override
    @DisabledIf(expression = "#{environment.acceptsProfiles('sqlite')}", loadContext = true, reason = "SQLite supports only TRANSACTION_SERIALIZABLE and TRANSACTION_READ_UNCOMMITTED.")
    public void test_READ_COMMITTED() throws Exception {
        testDirtyRead(3, Isolation.READ_COMMITTED, dirtyRead::assertREAD_COMMITTED);
    }

    @Test
    @Order(4)
    @Override
    @DisabledIf(expression = "#{environment.acceptsProfiles('sqlite')}", loadContext = true, reason = "SQLite supports only TRANSACTION_SERIALIZABLE and TRANSACTION_READ_UNCOMMITTED.")
    public void test_REPEATABLE_READ() throws Exception {
        testDirtyRead(4, Isolation.REPEATABLE_READ, dirtyRead::assertREPEATABLE_READ);
    }

    @Test
    @Order(5)
    @Override
    public void test_SERIALIZABLE() throws Exception {
        testDirtyRead(5, Isolation.SERIALIZABLE, dirtyRead::assertSERIALIZABLE);
    }

    private void testDirtyRead(long accountId, Isolation isolation, DirtyReadDelegate delegate) throws Exception {
        Account account = dirtyRead(accountId, expectOccur.getDepositAmount(), expectOccur.getWithdrawAmount(), isolation);
        executeTemplate(isolation, account, (data) -> delegate.assertResult(data, expectOccur));
    }

    public Account dirtyRead(long accountId, int depositAmount, int withdrawAmount, Isolation isolation) throws Exception {
        // Create multiple threads to simulate concurrency
        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Deposit Amount
        Future<ResultActions> t1 = executorService.submit(() -> {
            String url = String.format("%s/%s", DIRTY_READ_DEPOSIT_ENDPOINT, accountId);
            return performPost(url, (builder) -> builder.param("amount", String.valueOf(depositAmount)));
        });

        // Withdraw Amount
        Future<ResultActions> t2 = executorService.submit(() -> {
            String url = String.format("%s/%s", DIRTY_READ_WITHDRAW_ENDPOINT, accountId);
            return performPost(url, (builder) -> {
                builder.param("amount", String.valueOf(withdrawAmount));
                builder.param("isolation", isolation.name());
            });
        });

        // Waiting Response
        Collection<Future<ResultActions>> futures = new LinkedList<>(Arrays.asList(t1, t2));
        for (Future<ResultActions> future : futures) {
            ResultActions actions = future.get();
            actions.andExpect(STATUS_OK);
        }

        // Check Result
        ResultActions resultActions = performGet(String.format("%s/%s", DIRTY_READ_ACCOUNT_ENDPOINT, accountId))
                .andExpect(STATUS_OK);

        // Get the Account
        return fromResult(resultActions, Account.class);
    }

    @Override
    protected String getIsolationIssue() {
        return "Dirty Read";
    }

    private interface DirtyReadDelegate extends IsolationIssueDelegate<Account, DirtyReadExpectOccur> {
    }
}
