package com.darren.transactionisolation.isolation.unrepeatableread;

import com.darren.transactionisolation.isolation.Inventory;
import com.darren.transactionisolation.model.IsolationResult;
import com.darren.transactionisolation.model.UnrepeatableReadExpectOccur;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
public class UnrepeatableReadSqlite extends BaseUnrepeatableRead {

    @Override
    public IsolationResult assertDEFAULT(Inventory inventory, UnrepeatableReadExpectOccur unrepeatableReadExpectOccur) {
        return notOccur(inventory, unrepeatableReadExpectOccur);
    }

    @Override
    public IsolationResult assertREAD_UNCOMMITTED(Inventory inventory, UnrepeatableReadExpectOccur unrepeatableReadExpectOccur) {
        return notOccur(inventory, unrepeatableReadExpectOccur);
    }

    @Override
    public final IsolationResult assertREAD_COMMITTED(Inventory inventory, UnrepeatableReadExpectOccur unrepeatableReadExpectOccur) {
        throw new UnsupportedOperationException("SQLite supports only TRANSACTION_SERIALIZABLE and TRANSACTION_READ_UNCOMMITTED.");
    }

    @Override
    public final IsolationResult assertREPEATABLE_READ(Inventory inventory, UnrepeatableReadExpectOccur unrepeatableReadExpectOccur) {
        throw new UnsupportedOperationException("SQLite supports only TRANSACTION_SERIALIZABLE and TRANSACTION_READ_UNCOMMITTED.");
    }
}
