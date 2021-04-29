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
public class LostUpdatePostgreSql extends BaseLostUpdate {
    @Override
    public IsolationResult assertREPEATABLE_READ(Inventory actual, LostUpdateExpectOccur expectOccur) {
        return notOccur(actual, expectOccur);
    }
}
