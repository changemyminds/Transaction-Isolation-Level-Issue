package com.darren.transactionisolation.model;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
public class IsolationResult {
    public static final IsolationResult OCCUR = new IsolationResult(true);
    public static final IsolationResult NOT_OCCUR = new IsolationResult(false);

    private final Boolean occur;

    private IsolationResult(Boolean occur) {
        this.occur = occur;
    }

    public Boolean getOccur() {
        return occur;
    }
}
