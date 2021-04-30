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

    @Modifying
    @Query(value = "ALTER TABLE inventory AUTO_INCREMENT = 1", nativeQuery = true)
    void resetId();

    @Modifying
    @Query(value = "ALTER SEQUENCE seq RESTART;", nativeQuery = true)
    void resetSeq();

    @Modifying
    @Query(value = "UPDATE inventory SET id = DEFAULT;", nativeQuery = true)
    void updateIdColumn();

}
