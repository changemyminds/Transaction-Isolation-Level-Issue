package com.darren.transactionisolation;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@RequiredArgsConstructor
@SpringBootApplication
@EnableRetry
public class TransactionIsolationApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransactionIsolationApplication.class, args);
    }
}
