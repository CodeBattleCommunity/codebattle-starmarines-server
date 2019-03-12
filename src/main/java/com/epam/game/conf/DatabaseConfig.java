package com.epam.game.conf;

import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/28/2019
 */
@Configuration
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatabaseConfig {

    @PostConstruct
    void init() {
        log.info("Running db conf");
    }

    @Bean
    public SpringLiquibase liquibase(DataSource ds) {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setChangeLog("classpath:db/changelog-master.xml");
        springLiquibase.setDataSource(ds);
        return springLiquibase;
    }
}
