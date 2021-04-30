package com.darren.transactionisolation.service;

import com.darren.transactionisolation.isolation.GameTask;
import com.darren.transactionisolation.log.LogTopic;
import com.darren.transactionisolation.repository.GameTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransactionException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
@Service
@Slf4j(topic = LogTopic.PHANTOM_READ)
@RequiredArgsConstructor
public class PhantomReadService extends BaseService {
    private final GameTaskRepository gameTaskRepository;
    private final EntityManager entityManager;

    @Transactional(isolation = Isolation.DEFAULT)
    public List<GameTask> auditGameTaskDEFAULT(Integer auditScore) {
        return auditGameTask(auditScore);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public List<GameTask> auditGameTaskREAD_UNCOMMITTED(Integer auditScore) {
        return auditGameTask(auditScore);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<GameTask> auditGameTaskREAD_COMMITTED(Integer auditScore) {
        return auditGameTask(auditScore);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<GameTask> auditGameTaskREPEATABLE_READ(Integer auditScore) {
        return auditGameTask(auditScore);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<GameTask> auditGameTaskSERIALIZABLE(Integer auditScore) {
        return auditGameTask(auditScore);
    }

    public List<GameTask> auditGameTask(Integer auditScore) {
        log.info("[auditGameTask (T1)] find GameTasks by greater than auditScore");
        List<GameTask> auditGameTasks = gameTaskRepository.findGameTasksByScoreGreaterThan(auditScore);

        log.info("[auditGameTask (T1)] Ready Update Credit {} ", auditGameTasks);
        sleep(1.0);

        // use jpql to update database
        gameTaskRepository.updateCreditGreaterThan(auditScore);

        // If you don't clear the hibernate cache,
        // you will load data from the cache not the db and entity is incorrect.
        entityManager.clear();
        List<GameTask> updatedGameTasks = gameTaskRepository.findGameTasksByScoreGreaterThan(auditScore);

        log.info("[auditGameTask (T1)] Complete Update Credit {} ", getGameTaskString(updatedGameTasks));

        return updatedGameTasks;
    }

    private String getGameTaskString(List<GameTask> gameTasks) {
        return gameTasks.stream().map(Object::toString)
                .collect(Collectors.joining(", "));
    }

    // [SQLITE_BUSY]  The database file is locked (database is locked). So SQLite need retried it.
    @Retryable(value = {TransactionException.class}, maxAttempts = 2, backoff = @Backoff(delay = 100))
    @Transactional
    public void createGameTask(String name, Integer score) {
        sleep(0.5);
        gameTaskRepository.save(GameTask.create(name, score));
        log.info("[createGameTask (T2)] Commit");
    }


}
