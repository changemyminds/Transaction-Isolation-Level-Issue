package com.darren.transactionisolation.controller;

import com.darren.transactionisolation.isolation.Inventory;
import com.darren.transactionisolation.isolation.IsolationIssueDelegate;
import com.darren.transactionisolation.isolation.unrepeatableread.BaseUnrepeatableRead;
import com.darren.transactionisolation.model.UnrepeatableReadExpectOccur;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Isolation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
public class UnrepeatableReadControllerTest extends BaseIsolationControllerTest {
    private static final String ACQUIRE_ENDPOINT = "/unrepeatable-read/acquire";
    private static final String PURCHASE_ENDPOINT = "/unrepeatable-read/purchase";

    private final UnrepeatableReadExpectOccur expectOccur
            = new UnrepeatableReadExpectOccur(0, 900, 0, 900);

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private BaseUnrepeatableRead unrepeatableRead;

    @BeforeAll
    @Override
    public void setupAll() {
        super.setupAll();
        System.out.println("[Reset Inventory]");
        seedService.resetInventory(expectOccur.getDefaultQuantity(), 5);

        System.out.println("======================================");
        System.out.println("|  Acquire(T1)  | Purchase(T2)       |");
        System.out.println("--------------------------------------");
        System.out.println("|  Read(0)      |                    |");
        System.out.println("|               |  Read(0)           |");
        System.out.println("|               |  Purchase(0 +900)  |");
        System.out.println("|               |  Commit            |");
        System.out.println("|  Read(900)    |                    |");
        System.out.println("--------------------------------------");
        System.out.println("[Not Occur Dirty Read] Amount is " + expectOccur.getCorrectQuantity());
        System.out.println("[Occur Dirty Read]     Amount is " + expectOccur.getIncorrectQuantity());
        System.out.println("======================================");
    }

    @Test
    @Order(1)
    @Override
    public void test_DEFAULT() throws Exception {
        testUnrepeatableRead(1, Isolation.DEFAULT, unrepeatableRead::assertDEFAULT);
    }

    @Test
    @Order(2)
    @Override
    public void test_READ_UNCOMMITTED() throws Exception {
        testUnrepeatableRead(2, Isolation.READ_UNCOMMITTED, unrepeatableRead::assertREAD_UNCOMMITTED);
    }

    @Test
    @Order(3)
    @Override
    public void test_READ_COMMITTED() throws Exception {
        testUnrepeatableRead(3, Isolation.READ_COMMITTED, unrepeatableRead::assertREAD_COMMITTED);
    }

    @Test
    @Order(4)
    @Override
    public void test_REPEATABLE_READ() throws Exception {
        testUnrepeatableRead(4, Isolation.REPEATABLE_READ, unrepeatableRead::assertREPEATABLE_READ);
    }

    @Test
    @Order(5)
    @Override
    public void test_SERIALIZABLE() throws Exception {
        testUnrepeatableRead(5, Isolation.SERIALIZABLE, unrepeatableRead::assertSERIALIZABLE);
    }

    private void testUnrepeatableRead(long inventoryId, Isolation isolation, UnrepeatableReadDelegate delegate) throws Exception {
        Inventory inventory = unrepeatableRead(inventoryId, isolation);
        executeTemplate(isolation, inventory, (data) -> delegate.assertResult(data, expectOccur));
    }

    private Inventory unrepeatableRead(long inventoryId, Isolation isolation) throws Exception {
        // Create multiple threads to simulate concurrency
        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Acquire inventory
        Future<ResultActions> t1 = executorService.submit(() -> {
            String url = String.format("%s/%s", ACQUIRE_ENDPOINT, inventoryId);
            return performGet(url, (builder) -> builder.param("isolation", isolation.name()));
        });

        // Purchase inventory
        Future<ResultActions> t2 = executorService.submit(() -> {
            String url = String.format("%s/%s", PURCHASE_ENDPOINT, inventoryId);
            return performPost(url, (builder) -> builder.param("quantity", String.valueOf(expectOccur.getPurchaseQuantityT2())));
        });

        // Waiting Response
        t2.get().andExpect(STATUS_OK);
        ResultActions resultActions = t1.get().andExpect(STATUS_OK);

        // Get the Inventory
        return fromResult(resultActions, Inventory.class);
    }

    @Override
    protected String getIsolationIssue() {
        return "Unrepeatable Read";
    }

    private interface UnrepeatableReadDelegate extends IsolationIssueDelegate<Inventory, UnrepeatableReadExpectOccur> {
    }
}
