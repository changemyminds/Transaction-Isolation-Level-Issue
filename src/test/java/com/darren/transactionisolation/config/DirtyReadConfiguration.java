package com.darren.transactionisolation.config;

import com.darren.transactionisolation.constant.DatabaseType;
import com.darren.transactionisolation.constant.SpringProfile;
import com.darren.transactionisolation.isolation.dirtyread.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description:
 * Reference:
 */
@Configuration
public class DirtyReadConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.H2)
    public BaseDirtyRead dirtyReadH2() {
        return new DirtyReadH2();
    }

    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.MY_SQL)
    public BaseDirtyRead dirtyReadMySql() {
        return new DirtyReadMySql();
    }

    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.POSTGRE_SQL)
    public BaseDirtyRead dirtyReadPostgreSql() {
        return new DirtyReadPostgreSql();
    }

    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.SQL_SERVER)
    public BaseDirtyRead dirtyReadSqlServer() {
        return new DirtyReadSqlServer();
    }
}
