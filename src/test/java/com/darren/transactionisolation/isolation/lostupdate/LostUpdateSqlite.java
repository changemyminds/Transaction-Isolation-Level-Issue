package com.darren.transactionisolation.isolation.lostupdate;

import com.darren.transactionisolation.isolation.Inventory;
import com.darren.transactionisolation.model.IsolationResult;
import com.darren.transactionisolation.model.LostUpdateExpectOccur;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
public class LostUpdateSqlite extends BaseLostUpdate {
    @Override
    public IsolationResult assertDEFAULT(Inventory actual, LostUpdateExpectOccur expectOccur) {
        return notOccur(actual, expectOccur);
    }

    @Override
    public IsolationResult assertREAD_UNCOMMITTED(Inventory actual, LostUpdateExpectOccur expectOccur) {
        return notOccur(actual, expectOccur);
    }

    @Override
    public final IsolationResult assertREAD_COMMITTED(Inventory inventory, LostUpdateExpectOccur lostUpdateExpectOccur) {
        throw new UnsupportedOperationException("SQLite supports only TRANSACTION_SERIALIZABLE and TRANSACTION_READ_UNCOMMITTED.");
    }

    @Override
    public final IsolationResult assertREPEATABLE_READ(Inventory inventory, LostUpdateExpectOccur lostUpdateExpectOccur) {
        throw new UnsupportedOperationException("SQLite supports only TRANSACTION_SERIALIZABLE and TRANSACTION_READ_UNCOMMITTED.");
    }
}
