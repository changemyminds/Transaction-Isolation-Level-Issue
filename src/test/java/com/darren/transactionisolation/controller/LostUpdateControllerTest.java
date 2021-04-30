package com.darren.transactionisolation.controller;

import com.darren.transactionisolation.isolation.IsolationIssueDelegate;
import com.darren.transactionisolation.isolation.Ticket;
import com.darren.transactionisolation.isolation.lostupdate.BaseLostUpdate;
import com.darren.transactionisolation.model.LostUpdateExpectOccur;
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
public class LostUpdateControllerTest extends BaseIsolationControllerTest {
    private static final String LOST_UPDATE_SELL_ITEM_ENDPOINT = "/lost-update/sell-ticket";
    private static final String LOST_UPDATE_GET_ITEM_ENDPOINT = "/lost-update/ticket";

    private final LostUpdateExpectOccur expectOccur
            = new LostUpdateExpectOccur(10, 4, 1, 5, 9);

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private BaseLostUpdate lostUpdate;

    @BeforeAll
    @Override
    public void setupAll() {
        super.setupAll();
        System.out.println("[Reset Ticket]");
        seedService.resetTicket(expectOccur.getDefaultQuantity(), 5);

        System.out.println("======================================");
        System.out.println("|  SellTicket(T1)  | SellTicket(T2)  |");
        System.out.println("--------------------------------------");
        System.out.println("|  Read(10)       |                  |");
        System.out.println("|                 | Read(10)         |");
        System.out.println("|  Sell(10 - 4)   |                  |");
        System.out.println("|  Commit         |                  |");
        System.out.println("|                 | Sell(10 - 1)     |");
        System.out.println("|                 | Commit           |");
        System.out.println("--------------------------------------");
        System.out.println("[Not Occur Lost Update] Amount is " + expectOccur.getCorrectQuantity());
        System.out.println("[Occur Lost Update]     Amount is " + expectOccur.getIncorrectQuantity());
        System.out.println("======================================");
    }

    @Test
    @Order(1)
    @Override
    public void test_DEFAULT() throws Exception {
        testLostUpdate(1, Isolation.DEFAULT, lostUpdate::assertDEFAULT);
    }

    @Test
    @Order(2)
    @Override
    public void test_READ_UNCOMMITTED() throws Exception {
        testLostUpdate(2, Isolation.READ_UNCOMMITTED, lostUpdate::assertREAD_UNCOMMITTED);
    }

    @Test
    @Order(3)
    @Override
    @DisabledIf(expression = "#{environment.acceptsProfiles('sqlite')}", loadContext = true, reason = "SQLite supports only TRANSACTION_SERIALIZABLE and TRANSACTION_READ_UNCOMMITTED.")
    public void test_READ_COMMITTED() throws Exception {
        testLostUpdate(3, Isolation.READ_COMMITTED, lostUpdate::assertREAD_COMMITTED);
    }

    @Test
    @Order(4)
    @Override
    @DisabledIf(expression = "#{environment.acceptsProfiles('sqlite')}", loadContext = true, reason = "SQLite supports only TRANSACTION_SERIALIZABLE and TRANSACTION_READ_UNCOMMITTED.")
    public void test_REPEATABLE_READ() throws Exception {
        testLostUpdate(4, Isolation.REPEATABLE_READ, lostUpdate::assertREPEATABLE_READ);
    }

    @Test
    @Order(5)
    @Override
    public void test_SERIALIZABLE() throws Exception {
        testLostUpdate(5, Isolation.SERIALIZABLE, lostUpdate::assertSERIALIZABLE);
    }

    private void testLostUpdate(long ticketId, Isolation isolation, LostUpdateDelegate readDelegate) throws Exception {
        Ticket inventory = lostUpdate(ticketId, isolation);
        executeTemplate(isolation, inventory, (data) -> readDelegate.assertResult(data, expectOccur));
    }

    private Ticket lostUpdate(long ticketId, Isolation isolation) throws Exception {
        // Create multiple threads to simulate concurrency
        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Sell g
        Future<ResultActions> t1 = executorService.submit(() -> {
            String url = String.format("%s/%s", LOST_UPDATE_SELL_ITEM_ENDPOINT, ticketId);
            return performPost(url, (builder) -> {
                builder.param("sellCount", String.valueOf(expectOccur.getSellQuantityT1()));
                builder.param("isolation", isolation.name());
                builder.param("isT1", "true");
            });
        });

        // Withdraw Amount
        Future<ResultActions> t2 = executorService.submit(() -> {
            String url = String.format("%s/%s", LOST_UPDATE_SELL_ITEM_ENDPOINT, ticketId);
            return performPost(url, (builder) -> {
                builder.param("sellCount", String.valueOf(expectOccur.getSellQuantityT2()));
                builder.param("isolation", isolation.name());
                builder.param("isT1", "false");
            });
        });

        // Waiting Response
        Collection<Future<ResultActions>> futures = new LinkedList<>(Arrays.asList(t1, t2));
        for (Future<ResultActions> future : futures) {
            ResultActions actions = future.get();
            actions.andExpect(STATUS_OK);
        }

        // Check Result
        ResultActions resultActions = performGet(String.format("%s/%s", LOST_UPDATE_GET_ITEM_ENDPOINT, ticketId))
                .andExpect(STATUS_OK);

        // Get the Account
        return fromResult(resultActions, Ticket.class);
    }

    @Override
    protected String getIsolationIssue() {
        return "Lost Update";
    }

    private interface LostUpdateDelegate extends IsolationIssueDelegate<Ticket, LostUpdateExpectOccur> {
    }
}
