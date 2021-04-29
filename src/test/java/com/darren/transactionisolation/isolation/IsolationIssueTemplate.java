package com.darren.transactionisolation.isolation;

import com.darren.transactionisolation.model.IsolationResult;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
public abstract class IsolationIssueTemplate<Actual, ExpectOccur> {
    public IsolationResult assertDEFAULT(Actual actual, ExpectOccur expectOccur) {
        return occur(actual, expectOccur);
    }

    public IsolationResult assertREAD_UNCOMMITTED(Actual actual, ExpectOccur expectOccur) {
        return occur(actual, expectOccur);
    }

    public IsolationResult assertREAD_COMMITTED(Actual actual, ExpectOccur expectOccur) {
        return occur(actual, expectOccur);
    }

    public IsolationResult assertREPEATABLE_READ(Actual actual, ExpectOccur expectOccur) {
        return occur(actual, expectOccur);
    }

    public IsolationResult assertSERIALIZABLE(Actual actual, ExpectOccur expectOccur) {
        return notOccur(actual, expectOccur);
    }

    protected final IsolationResult occur(Actual actual, ExpectOccur expectOccur) {
        assertOccur(actual, expectOccur);
        return IsolationResult.OCCUR;
    }
    protected final IsolationResult notOccur(Actual actual, ExpectOccur expectOccur) {
        assertNotOccur(actual, expectOccur);
        return IsolationResult.NOT_OCCUR;
    }

    protected abstract void assertOccur(Actual actual, ExpectOccur expectOccur);
    protected abstract void assertNotOccur(Actual actual, ExpectOccur expectOccur);
}
