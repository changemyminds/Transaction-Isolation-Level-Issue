package com.darren.transactionisolation.service;

import com.darren.transactionisolation.isolation.Account;
import com.darren.transactionisolation.isolation.GameTask;
import com.darren.transactionisolation.isolation.Inventory;
import com.darren.transactionisolation.repository.AccountRepository;
import com.darren.transactionisolation.repository.GameTaskRepository;
import com.darren.transactionisolation.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SeedService {
    private final AccountRepository accountRepository;
    private final InventoryRepository inventoryRepository;
    private final GameTaskRepository gameTaskRepository;

    public void resetGameTasks() {
        initGameTasks(true);
    }

    public void initGameTasks(boolean clear) {
        if (clear && gameTaskRepository.count() > 0) {
            gameTaskRepository.deleteAll();
        }

        gameTaskRepository.save(GameTask.create("Darren", 900));
        gameTaskRepository.save(GameTask.create("Frank", 700));
        gameTaskRepository.save(GameTask.create("Jimpo", 600));
        gameTaskRepository.save(GameTask.create("Mario", 300));
        gameTaskRepository.save(GameTask.create("Hank", 250));
    }

    public void resetAccount(int amount, int count) {
        initAccount(amount, count, true);
    }

    public void initAccount(int amount, int count, boolean clear) {
        if (clear && accountRepository.count() > 0) {
            accountRepository.deleteAll();
        }

        createSeedCount(count, () -> accountRepository.save(createAccount()));
    }

    public void resetInventory(int quantity, int count) {
        initInventory(quantity, count, true);
    }

    public void initInventory(int quantity, int count, boolean clear) {
        if (clear && inventoryRepository.count() > 0) {
            inventoryRepository.deleteAll();
        }

        createSeedCount(count, () -> inventoryRepository.save(createInventory(quantity)));
    }

    private void createSeedCount(int count, Runnable runnable) {
        Stream.iterate(1, n -> n + 1)
                .limit(count)
                .forEach((i) -> runnable.run());
    }

    private Account createAccount() {
        Account account = new Account();
        account.setAmount(0);
        return account;
    }

    private Inventory createInventory(int quantity) {
        Inventory inventory = new Inventory();
        inventory.setQuantity(quantity);
        return inventory;
    }
}
