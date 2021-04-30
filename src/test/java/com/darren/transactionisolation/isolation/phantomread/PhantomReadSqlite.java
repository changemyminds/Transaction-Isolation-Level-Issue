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
public class PhantomReadSqlite extends BasePhantomRead {

    @Override
    public IsolationResult assertDEFAULT(List<GameTask> gameTasks, PhantomReadExpectOccur phantomReadExpectOccur) {
        return notOccur(gameTasks, phantomReadExpectOccur);
    }

    @Override
    public IsolationResult assertREAD_UNCOMMITTED(List<GameTask> gameTasks, PhantomReadExpectOccur phantomReadExpectOccur) {
        return notOccur(gameTasks, phantomReadExpectOccur);
    }

    @Override
    public IsolationResult assertREAD_COMMITTED(List<GameTask> gameTasks, PhantomReadExpectOccur phantomReadExpectOccur) {
        throw new UnsupportedOperationException("SQLite supports only TRANSACTION_SERIALIZABLE and TRANSACTION_READ_UNCOMMITTED.");
    }

    @Override
    public IsolationResult assertREPEATABLE_READ(List<GameTask> gameTasks, PhantomReadExpectOccur phantomReadExpectOccur) {
        throw new UnsupportedOperationException("SQLite supports only TRANSACTION_SERIALIZABLE and TRANSACTION_READ_UNCOMMITTED.");
    }
}
