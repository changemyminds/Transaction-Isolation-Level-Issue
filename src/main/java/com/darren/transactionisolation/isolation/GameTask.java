package com.darren.transactionisolation.isolation;

import lombok.Data;

import javax.persistence.*;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
@Data
@Table
@Entity
public class GameTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer score;

    private Integer credit;

    public static GameTask create(String name, int score) {
        GameTask gameTask = new GameTask();
        gameTask.setName(name);
        gameTask.setScore(score);
        gameTask.setCredit(0);
        return gameTask;
    }

}
