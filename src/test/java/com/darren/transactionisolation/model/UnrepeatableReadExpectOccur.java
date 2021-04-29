package com.darren.transactionisolation.model;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
public class UnrepeatableReadExpectOccur {
    private final Integer defaultQuantity;
    private final Integer purchaseQuantityT2;
    private final Integer correctQuantity;
    private final Integer incorrectQuantity;

    public UnrepeatableReadExpectOccur(Integer defaultQuantity, Integer purchaseQuantityT2, Integer correctQuantity, Integer incorrectQuantity) {
        this.defaultQuantity = defaultQuantity;
        this.purchaseQuantityT2 = purchaseQuantityT2;
        this.correctQuantity = correctQuantity;
        this.incorrectQuantity = incorrectQuantity;
    }

    public Integer getDefaultQuantity() {
        return defaultQuantity;
    }

    public Integer getPurchaseQuantityT2() {
        return purchaseQuantityT2;
    }

    public Integer getCorrectQuantity() {
        return correctQuantity;
    }

    public Integer getIncorrectQuantity() {
        return incorrectQuantity;
    }
}
