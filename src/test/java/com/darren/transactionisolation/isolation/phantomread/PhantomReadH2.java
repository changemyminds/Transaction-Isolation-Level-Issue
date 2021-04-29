package com.darren.transactionisolation.isolation.phantomread;

import com.darren.transactionisolation.model.PhantomReadExpectOccur;
import com.darren.transactionisolation.model.IsolationResult;
import com.darren.transactionisolation.isolation.GameTask;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
public class PhantomReadH2 extends BasePhantomRead {
    @Override
    public IsolationResult assertREPEATABLE_READ(GameTask actual, PhantomReadExpectOccur expectOccur) {
        return notOccur(actual, expectOccur);
    }
}
