package com.darren.transactionisolation.isolation;

import com.darren.transactionisolation.model.IsolationResult;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
@FunctionalInterface
public interface IsolationIssueDelegate<Actual, ExpectOccur> {
    IsolationResult assertResult(Actual actual, ExpectOccur expectOccur);
}
