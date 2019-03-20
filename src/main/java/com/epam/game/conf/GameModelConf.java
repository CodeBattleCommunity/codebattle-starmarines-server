package com.epam.game.conf;

import com.epam.game.gamemodel.model.Model;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/15/2019
 */
@Configuration
@DependsOn("DBConf")
public class GameModelConf {

    @Bean
    @Lazy
    public Model modelInstance() {
        return new Model();
    }
}
