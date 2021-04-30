package com.darren.transactionisolation.isolation.lostupdate;

import com.darren.transactionisolation.isolation.Ticket;
import com.darren.transactionisolation.model.IsolationResult;
import com.darren.transactionisolation.model.LostUpdateExpectOccur;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
public class LostUpdateH2 extends BaseLostUpdate {
    @Override
    public IsolationResult assertSERIALIZABLE(Ticket actual, LostUpdateExpectOccur expectOccur) {
        return occur(actual, expectOccur);
    }
}
