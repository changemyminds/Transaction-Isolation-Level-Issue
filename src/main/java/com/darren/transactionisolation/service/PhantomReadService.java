package com.darren.transactionisolation.service;

import com.darren.transactionisolation.isolation.GameTask;
import com.darren.transactionisolation.log.LogTopic;
import com.darren.transactionisolation.repository.GameTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        log.info("[auditGameTask] find All");
        List<GameTask> gameTasks = gameTaskRepository.findAll();
        String gameTasksString = getGameTaskString(gameTasks);
        log.info("[auditGameTask] List {} ", gameTasksString);
        sleep(1.0);

        gameTaskRepository.updateCreditGreaterThan(auditScore);

        // If you don't clear the hibernate cache,
        // you will load data from the cache not the db and entity is incorrect.
        entityManager.clear();
        List<GameTask> newGameTask = gameTaskRepository.findAll();

        gameTasksString = getGameTaskString(newGameTask);
        log.info("[auditGameTask] New List {} ", gameTasksString);

        return newGameTask;
    }

    private String getGameTaskString(List<GameTask> gameTasks) {
        return gameTasks.stream().map(Object::toString)
                .collect(Collectors.joining(", "));
    }

    @Transactional(isolation = Isolation.DEFAULT)
    public void createGameTaskDEFAULT(String name, Integer score) {
        createGameTask(name, score);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void createGameTaskREAD_UNCOMMITTED(String name, Integer score) {
        createGameTask(name, score);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void createGameTaskREAD_COMMITTED(String name, Integer score) {
        createGameTask(name, score);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void createGameTaskREPEATABLE_READ(String name, Integer score) {
        createGameTask(name, score);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createGameTaskSERIALIZABLE(String name, Integer score) {
        createGameTask(name, score);
    }

    @Transactional
    public void createGameTask(String name, Integer score) {
        sleep(0.5);
        gameTaskRepository.save(GameTask.create(name, score));
        log.info("[createGameTask] Commit");
    }


}
