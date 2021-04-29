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
    private final Integer correctCount;
    private final Integer incorrectCount;

    public PhantomReadExpectOccur(String name, Integer score, Integer credit, Integer auditScore, Integer correctCount, Integer incorrectCount) {
        this.name = name;
        this.score = score;
        this.credit = credit;
        this.auditScore = auditScore;
        this.correctCount = correctCount;
        this.incorrectCount = incorrectCount;
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

    public Integer getCorrectCount() {
        return correctCount;
    }

    public Integer getIncorrectCount() {
        return incorrectCount;
    }
}
