package com.darren.transactionisolation.service;

import com.darren.transactionisolation.isolation.Inventory;
import com.darren.transactionisolation.log.LogTopic;
import com.darren.transactionisolation.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
@Service
@Slf4j(topic = LogTopic.UNREPEATABLE_READ)
@RequiredArgsConstructor
public class UnrepeatableReadService extends BaseService {
    private final InventoryRepository inventoryRepository;
    private final EntityManager entityManager;

    @Transactional(isolation = Isolation.DEFAULT)
    public Inventory acquireDEFAULT(Long id) {
        return acquire(id);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Inventory acquireREAD_UNCOMMITTED(Long id) {
        return acquire(id);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Inventory acquireREAD_COMMITTED(Long id) {
        return acquire(id);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Inventory acquireREPEATABLE_READ(Long id) {
        return acquire(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Inventory acquireSERIALIZABLE(Long id) {
        return acquire(id);
    }

    public Inventory acquire(Long id) {
        Inventory inventory = getInventory(id);

        log.info("[AcquireInventory (T1)] Read {}", inventory);
        sleep(1.0);

        entityManager.clear(); // clear cache
        inventory = getInventory(id);
        log.info("[AcquireInventory (T1)] Read Again {}", inventory);

        return inventory;
    }

    @Transactional
    public void purchase(Long id, Integer quantity) {
        sleep(0.5);

        Inventory inventory = getInventory(id);
        log.info("[Purchase (T2)] Read {}", inventory);

        Integer nowQuantity = inventory.getQuantity();
        inventory.setQuantity(nowQuantity + quantity);
        log.info("[Purchase (T2)] {} + {} = {}", nowQuantity, inventory, inventory.getQuantity());

        inventoryRepository.saveAndFlush(inventory);
        log.info("[Purchase (T2)] Commit");
    }

    public Inventory getInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        log.info("{}", inventory);
        return inventory;
    }
}

