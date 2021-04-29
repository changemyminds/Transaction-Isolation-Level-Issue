package com.darren.transactionisolation.repository;

import com.darren.transactionisolation.isolation.GameTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
public interface GameTaskRepository extends JpaRepository<GameTask, Long> {

    @Modifying
    @Query(value = "truncate table game_task", nativeQuery = true)
    void truncate();

    @Modifying
    @Query(value = "UPDATE game_task SET credit = credit + 1 WHERE score>=:score",
            nativeQuery = true)
    void updateCreditGreaterThan(@Param("score") Integer score);
}
