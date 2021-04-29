package com.darren.transactionisolation.model;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
public class LostUpdateExpectOccur {
    private final Integer defaultQuantity;
    private final Integer sellQuantityT1;
    private final Integer sellQuantityT2;
    private final Integer correctQuantity;
    private final Integer incorrectQuantity;

    public LostUpdateExpectOccur(Integer defaultQuantity, Integer sellQuantityT1, Integer sellQuantityT2, Integer correctQuantity, Integer incorrectQuantity) {
        this.defaultQuantity = defaultQuantity;
        this.sellQuantityT1 = sellQuantityT1;
        this.sellQuantityT2 = sellQuantityT2;
        this.correctQuantity = correctQuantity;
        this.incorrectQuantity = incorrectQuantity;
    }

    public Integer getDefaultQuantity() {
        return defaultQuantity;
    }

    public Integer getSellQuantityT1() {
        return sellQuantityT1;
    }

    public Integer getSellQuantityT2() {
        return sellQuantityT2;
    }

    public Integer getCorrectQuantity() {
        return correctQuantity;
    }

    public Integer getIncorrectQuantity() {
        return incorrectQuantity;
    }
}
