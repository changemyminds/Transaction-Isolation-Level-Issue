package com.darren.transactionisolation.service;

import java.util.concurrent.TimeUnit;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
public class BaseService {
    protected void sleep(double timeoutSec) {
        try {
            TimeUnit.MILLISECONDS.sleep((long) (timeoutSec * 1000));
        } catch (InterruptedException ignored) {
        }
    }
}
