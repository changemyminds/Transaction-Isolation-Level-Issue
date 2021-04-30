package com.darren.transactionisolation.config;

import com.darren.transactionisolation.constant.DatabaseType;
import com.darren.transactionisolation.constant.SpringProfile;
import com.darren.transactionisolation.isolation.unrepeatableread.*;
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
public class UnrepeatableReadConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.H2)
    public BaseUnrepeatableRead unrepeatableReadH2() {
        return new UnrepeatableReadH2();
    }

    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.MY_SQL)
    public BaseUnrepeatableRead unrepeatableReadMySql() {
        return new UnrepeatableReadMySql();
    }

    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.POSTGRE_SQL)
    public BaseUnrepeatableRead unrepeatableReadPostgreSql() {
        return new UnrepeatableReadPostgreSql();
    }

    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.SQLITE)
    public BaseUnrepeatableRead unrepeatableReadSqlite() {
        return new UnrepeatableReadSqlite();
    }

    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.SQL_SERVER)
    public BaseUnrepeatableRead unrepeatableReadSqlServer() {
        return new UnrepeatableReadSqlServer();
    }
}
