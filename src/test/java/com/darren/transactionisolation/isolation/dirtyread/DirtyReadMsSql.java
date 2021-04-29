package com.darren.transactionisolation.isolation.dirtyread;

import com.darren.transactionisolation.isolation.Account;
import com.darren.transactionisolation.model.DirtyReadExpectOccur;
import com.darren.transactionisolation.model.IsolationResult;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
public class DirtyReadMsSql extends BaseDirtyRead {
    @Override
    public IsolationResult assertDEFAULT(Account actual, DirtyReadExpectOccur expectOccur) {
        return notOccur(actual, expectOccur);
    }

    @Override
    public IsolationResult assertREPEATABLE_READ(Account actual, DirtyReadExpectOccur expectOccur) {
        return notOccur(actual, expectOccur);
    }

    @Override
    public IsolationResult assertREAD_COMMITTED(Account actual, DirtyReadExpectOccur expectOccur) {
        return notOccur(actual, expectOccur);
    }
}
