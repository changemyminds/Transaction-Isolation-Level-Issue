package com.darren.transactionisolation.isolation.dirtyread;

import com.darren.transactionisolation.model.DirtyReadExpectOccur;
import com.darren.transactionisolation.model.IsolationResult;
import com.darren.transactionisolation.isolation.Account;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
public class DirtyReadH2 extends BaseDirtyRead {

    @Override
    public IsolationResult assertDEFAULT(Account account, DirtyReadExpectOccur dirtyReadExpectOccur) {
        return notOccur(account, dirtyReadExpectOccur);
    }

    @Override
    public IsolationResult assertREAD_COMMITTED(Account account, DirtyReadExpectOccur dirtyReadExpectOccur) {
        return notOccur(account, dirtyReadExpectOccur);
    }

    @Override
    public IsolationResult assertREPEATABLE_READ(Account account, DirtyReadExpectOccur dirtyReadExpectOccur) {
        return notOccur(account, dirtyReadExpectOccur);
    }
}
