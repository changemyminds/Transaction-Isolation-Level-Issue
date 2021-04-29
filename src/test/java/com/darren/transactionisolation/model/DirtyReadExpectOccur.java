package com.darren.transactionisolation.model;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
public class DirtyReadExpectOccur {
    private final Integer defaultTotalAmount;
    private final Integer depositAmount;
    private final Integer withdrawAmount;
    private final Integer correctTotalAmount;
    private final Integer incorrectTotalAmount;

    public DirtyReadExpectOccur(Integer defaultTotalAmount, Integer depositAmount, Integer withdrawAmount, Integer correctTotalAmount, Integer incorrectTotalAmount) {
        this.defaultTotalAmount = defaultTotalAmount;
        this.depositAmount = depositAmount;
        this.withdrawAmount = withdrawAmount;
        this.correctTotalAmount = correctTotalAmount;
        this.incorrectTotalAmount = incorrectTotalAmount;
    }

    public Integer getDefaultTotalAmount() {
        return defaultTotalAmount;
    }

    public Integer getDepositAmount() {
        return depositAmount;
    }

    public Integer getWithdrawAmount() {
        return withdrawAmount;
    }

    public Integer getCorrectTotalAmount() {
        return correctTotalAmount;
    }

    public Integer getIncorrectTotalAmount() {
        return incorrectTotalAmount;
    }
}
