package com.darren.transactionisolation.isolation.phantomread;

import com.darren.transactionisolation.isolation.GameTask;
import com.darren.transactionisolation.model.IsolationResult;
import com.darren.transactionisolation.model.PhantomReadExpectOccur;

import java.util.List;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
public class PhantomReadPostgreSql extends BasePhantomRead {
    @Override
    public IsolationResult assertREPEATABLE_READ(List<GameTask> actual, PhantomReadExpectOccur expectOccur) {
        return notOccur(actual, expectOccur);
    }
}
