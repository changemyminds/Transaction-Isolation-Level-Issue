package com.darren.transactionisolation.config;

import com.darren.transactionisolation.constant.DatabaseType;
import com.darren.transactionisolation.constant.SpringProfile;
import com.darren.transactionisolation.isolation.phantomread.*;
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
public class PhantomReadConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.H2)
    public BasePhantomRead phantomReadH2() {
        return new PhantomReadH2();
    }

    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.MY_SQL)
    public BasePhantomRead phantomReadMySql() {
        return new PhantomReadMySql();
    }

    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.POSTGRE_SQL)
    public BasePhantomRead phantomReadPostgreSql() {
        return new PhantomReadPostgreSql();
    }

    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.SQL_SERVER)
    public BasePhantomRead phantomReadSqlServer() {
        return new PhantomReadSqlServer();
    }
}
