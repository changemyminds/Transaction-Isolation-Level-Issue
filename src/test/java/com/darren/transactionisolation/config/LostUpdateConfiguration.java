package com.darren.transactionisolation.config;

import com.darren.transactionisolation.constant.DatabaseType;
import com.darren.transactionisolation.constant.SpringProfile;
import com.darren.transactionisolation.isolation.lostupdate.*;
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
public class LostUpdateConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.H2)
    public BaseLostUpdate lostUpdateH2() {
        return new LostUpdateH2();
    }

    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.MS_SQL)
    public BaseLostUpdate lostUpdateMsSql() {
        return new LostUpdateMsSql();
    }

    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.MY_SQL)
    public BaseLostUpdate lostUpdateMySql() {
        return new LostUpdateMySql();
    }

    @Bean
    @ConditionalOnProperty(prefix = SpringProfile.PREFIX, name = SpringProfile.NAME, havingValue = DatabaseType.POSTGRE_SQL)
    public BaseLostUpdate lostUpdatePostgreSql() {
        return new LostUpdatePostgreSql();
    }
}
