package com.epam.game.conf;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/28/2019
 */
@Configuration
public class DBConf {

    @Bean
    public SpringLiquibase liquibase(DataSource ds) {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setChangeLog("classpath:db/changelog-master.xml");
        springLiquibase.setDataSource(ds);
        return springLiquibase;
    }
}
