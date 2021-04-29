package com.darren.transactionisolation.model;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
public class PhantomReadExpectOccur {
    private final String name;

    private final Integer score;

    private final Integer credit;

    private final Integer auditScore;

    public PhantomReadExpectOccur(String name, Integer score, Integer credit, Integer auditScore) {
        this.name = name;
        this.score = score;
        this.credit = credit;
        this.auditScore = auditScore;
    }

    public String getName() {
        return name;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getCredit() {
        return credit;
    }

    public int getAuditScore() {
        return auditScore;
    }
}
