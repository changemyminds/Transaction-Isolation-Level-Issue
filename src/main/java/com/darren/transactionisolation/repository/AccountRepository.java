package com.darren.transactionisolation.repository;

import com.darren.transactionisolation.isolation.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Modifying
    @Query(value = "truncate table account", nativeQuery = true)
    void truncate();
}
