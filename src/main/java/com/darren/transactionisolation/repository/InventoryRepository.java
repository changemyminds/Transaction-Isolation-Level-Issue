package com.darren.transactionisolation.repository;

import com.darren.transactionisolation.isolation.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Modifying
    @Query(value = "truncate table inventory", nativeQuery = true)
    void truncate();

    @Modifying
    @Query(value = "VACUUM", nativeQuery = true)
    void vacuum();
}
