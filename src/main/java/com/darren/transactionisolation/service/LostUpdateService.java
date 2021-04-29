package com.darren.transactionisolation.service;

import com.darren.transactionisolation.isolation.Inventory;
import com.darren.transactionisolation.log.LogTopic;
import com.darren.transactionisolation.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
@Slf4j(topic = LogTopic.LOST_UPDATE)
@Service
@RequiredArgsConstructor
public class LostUpdateService extends BaseService {
    private final InventoryRepository inventoryRepository;

    @Transactional(isolation = Isolation.DEFAULT)
    public void sellItemDEFAULT(Long id, Integer itemCount, boolean isT1) {
        sellItem(id, itemCount, isT1);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void sellItemREAD_UNCOMMITTED(Long id, Integer itemCount, boolean isT1) {
        sellItem(id, itemCount, isT1);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void sellItemREAD_COMMITTED(Long id, Integer itemCount, boolean isT1) {
        sellItem(id, itemCount, isT1);
    }

    // @Retryable is used in MySQL LostUpdate
    @Retryable(value = CannotAcquireLockException.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void sellItemREPEATABLE_READ(Long id, Integer itemCount, boolean isT1) {
        sellItem(id, itemCount, isT1);
    }

    @Retryable(value = CannotAcquireLockException.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void sellItemSERIALIZABLE(Long id, Integer itemCount, boolean isT1) {
        sellItem(id, itemCount, isT1);
    }

    public void sellItem(Long id, Integer itemCount, boolean isT1) {
        if (!isT1) sleep(0.5);

        Inventory inventory = getInventory(id);
        if (isT1) {
            log.info("[SellItem (T1)] Read {}", inventory);
            sleep(1.0);
        } else {
            log.info("[SellItem (T2)] Read {}", inventory);
            sleep(1.5);
        }

        Integer quantity = inventory.getQuantity();
        inventory.setQuantity(quantity - itemCount);
        inventoryRepository.saveAndFlush(inventory);

        if (isT1) {
            log.info("[SellItem (T1)] Update Quantity {}", inventory);
            log.info("[SellItem (T1)] Commit");
        } else {
            log.info("[SellItem (T2)] Update Quantity {}", inventory);
            log.info("[SellItem (T2)] Commit");
        }
    }

    public Inventory getInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        log.info("{}", inventory);
        return inventory;
    }
}
