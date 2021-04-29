package com.darren.transactionisolation.controller;

import com.darren.transactionisolation.isolation.GameTask;
import com.darren.transactionisolation.log.LogTopic;
import com.darren.transactionisolation.service.PhantomReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
@RestController
@RequestMapping("/phantom-read")
@RequiredArgsConstructor
@Slf4j(topic = LogTopic.UNREPEATABLE_READ)
public class PhantomReadController {
    private final PhantomReadService phantomReadService;

    @GetMapping("/audit")
    public List<GameTask> auditGameTask(@RequestParam Integer auditScore,
                                        @RequestParam String isolation) {
        switch (Isolation.valueOf(isolation)) {
            case READ_UNCOMMITTED:
                return phantomReadService.auditGameTaskREAD_UNCOMMITTED(auditScore);
            case READ_COMMITTED:
                return phantomReadService.auditGameTaskREAD_COMMITTED(auditScore);
            case REPEATABLE_READ:
                return phantomReadService.auditGameTaskREPEATABLE_READ(auditScore);
            case SERIALIZABLE:
                return phantomReadService.auditGameTaskSERIALIZABLE(auditScore);
            case DEFAULT:
            default:
                return phantomReadService.auditGameTaskDEFAULT(auditScore);
        }
    }

    @PostMapping("/create")
    public void create(@RequestParam String name,
                       @RequestParam Integer score,
                       @RequestParam String isolation) {

        switch (Isolation.valueOf(isolation)) {
            case READ_UNCOMMITTED:
                 phantomReadService.createGameTaskREAD_UNCOMMITTED(name, score);
                 return;
            case READ_COMMITTED:
                phantomReadService.createGameTaskREAD_COMMITTED(name, score);
                return;
            case REPEATABLE_READ:
                phantomReadService.createGameTaskREPEATABLE_READ(name, score);
                return;
            case SERIALIZABLE:
                phantomReadService.createGameTaskSERIALIZABLE(name, score);
                return;
            case DEFAULT:
            default:
                phantomReadService.createGameTaskDEFAULT(name, score);
                return;
        }
    }
}
