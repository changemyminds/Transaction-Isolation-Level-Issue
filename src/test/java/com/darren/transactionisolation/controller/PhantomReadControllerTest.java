package com.darren.transactionisolation.controller;

import com.darren.transactionisolation.model.PhantomReadExpectOccur;
import com.darren.transactionisolation.isolation.GameTask;
import com.darren.transactionisolation.isolation.IsolationIssueDelegate;
import com.darren.transactionisolation.isolation.phantomread.BasePhantomRead;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Isolation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
public class PhantomReadControllerTest extends BaseIsolationControllerTest {
    private static final String AUDIT_ENDPOINT = "/phantom-read/audit";
    private static final String CREATE_ENDPOINT = "/phantom-read/create";

    private final PhantomReadExpectOccur expectOccur
            = new PhantomReadExpectOccur("changemyminds", 888, 1, 700);

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private BasePhantomRead phantomRead;

    @BeforeEach
    public void setup() {
        System.out.println("[Reset GameTasks]");
        seedService.resetGameTasks();
    }

    @BeforeAll
    @Override
    public void setupAll() {
        super.setupAll();
//        System.out.println("==========================================");
//        System.out.println("|  Deposit(T1)      | Withdraw(T2)       |");
//        System.out.println("------------------------------------------");
//        System.out.println("|  Read(0)          |                    |");
//        System.out.println("|  deposit(0 + 500) |                    |");
//        System.out.println("|                   |  Read 0            |");
//        System.out.println("|  Rollback         |                    |");
//        System.out.println("|                   |  Withdraw(0 - 300) |");
//        System.out.println("|                   |  Commit            |");
//        System.out.println("------------------------------------------");
//        System.out.println("[Not Occur Dirty Read] Amount is " + expectOccur.getCorrectTotalAmount());
//        System.out.println("[Occur Dirty Read]     Amount is " + expectOccur.getIncorrectTotalAmount());
//        System.out.println("==========================================");
    }

    @Test
    @Order(1)
    @Override
    public void test_DEFAULT() throws Exception {
        testPhantomRead(Isolation.DEFAULT, phantomRead::assertDEFAULT);
    }

    @Test
    @Order(2)
    @Override
    public void test_READ_UNCOMMITTED() throws Exception {
        testPhantomRead(Isolation.READ_UNCOMMITTED, phantomRead::assertREAD_UNCOMMITTED);
    }

    @Test
    @Order(3)
    @Override
    public void test_READ_COMMITTED() throws Exception {
        testPhantomRead(Isolation.READ_COMMITTED, phantomRead::assertREAD_COMMITTED);
    }

    @Test
    @Order(4)
    @Override
    public void test_REPEATABLE_READ() throws Exception {
        testPhantomRead(Isolation.REPEATABLE_READ, phantomRead::assertREPEATABLE_READ);
    }

    @Test
    @Order(5)
    @Override
    public void test_SERIALIZABLE() throws Exception {
        testPhantomRead(Isolation.SERIALIZABLE, phantomRead::assertSERIALIZABLE);
    }

    private void testPhantomRead(Isolation isolation, PhantomReadDelegate readDelegate) throws Exception {
        List<GameTask> gameTasks = phantomRead(isolation);

        executeTemplate(isolation, gameTasks, (games) -> {
            GameTask gameTask = games.stream().filter(game -> expectOccur.getName().equals(game.getName()))
                    .findFirst()
                    .orElse(null);
            return readDelegate.assertResult(gameTask, expectOccur);
        });
    }

    private List<GameTask> phantomRead(Isolation isolation) throws Exception {
        // Create multiple threads to simulate concurrency
        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Audit GameTask Score
        Future<ResultActions> t1 = executorService.submit(() -> performGet(AUDIT_ENDPOINT, (builder) -> {
            builder.param("auditScore", String.valueOf(expectOccur.getAuditScore()));
            builder.param("isolation", isolation.name());
        }));

        // Create user to GameTask
        Future<ResultActions> t2 = executorService.submit(() -> performPost(CREATE_ENDPOINT, (builder) -> {
            builder.param("name", expectOccur.getName());
            builder.param("score", String.valueOf(expectOccur.getScore()));
            builder.param("isolation", isolation.name());
        }));

        t2.get().andExpect(STATUS_OK);
        ResultActions resultTrA = t1.get().andExpect(STATUS_OK);

        return fromResult(resultTrA, new TypeReference<List<GameTask>>() {
        });
    }

    @Override
    protected String getIsolationIssue() {
        return "Phantom Read";
    }

    private interface PhantomReadDelegate extends IsolationIssueDelegate<GameTask, PhantomReadExpectOccur> {
    }
}
