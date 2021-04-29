package com.darren.transactionisolation.controller;

import com.darren.transactionisolation.model.IsolationResult;
import com.darren.transactionisolation.isolation.IsolationOperate;
import com.darren.transactionisolation.service.SeedService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Isolation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Author: changemyminds.
 * Date: 2021/4/27.
 * Description:
 * Reference:
 */
@TestPropertySource(locations = {"classpath:/config/database-setting.properties"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class BaseIsolationControllerTest extends BaseControllerTest implements IsolationOperate {
    private final Map<Isolation, String> resultMap = new LinkedHashMap<>();

    @Autowired
    protected SeedService seedService;

    @AfterAll
    public void destroyAll() {
        System.out.println("===========================================");
        String title = String.format("|  Isolation Level  |  %-17s  |", getIsolationIssue());
        System.out.println(title);
        System.out.println("-------------------------------------------");
        resultMap.forEach((key, value) -> {
            String lineResult = String.format("|  %-16s  |  %-16s  |", key, value);
            System.out.println(lineResult);
        });
        System.out.println("===========================================");
    }

    protected <T> void executeTemplate(Isolation isolation, T resultData, Function<T, IsolationResult> occurSupplier) {
        printObject(isolation, resultData);
        IsolationResult result = occurSupplier.apply(resultData);
        printResult(isolation, result.getOccur());
        setResultMap(isolation, result.getOccur());
    }

    private void printObject(Isolation isolation, Object obj) {
        System.out.println("[" + isolation.name() + "] data: " + obj.toString());
    }

    private void printResult(Isolation isolation, boolean occur) {
        System.out.println("[" + isolation.name() + "] " + getIsolationIssue() + ": " + (occur ? "occur !" : "don't occur !"));
    }

    private void setResultMap(Isolation isolation, boolean occur) {
        resultMap.put(isolation, (occur) ? "may occur" : "not occur");
    }

    protected abstract String getIsolationIssue();
}
