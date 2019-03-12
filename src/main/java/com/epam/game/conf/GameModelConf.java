package com.epam.game.conf;

import com.epam.game.gamemodel.model.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/15/2019
 */
@Configuration
@DependsOn("databaseConfig")
@Slf4j
public class GameModelConf {

    @PostConstruct
    void init() {
        log.info("Running model conf");
    }

    @Bean
    public Model modelInstance() {
        return Model.getInstance();
    }
}
