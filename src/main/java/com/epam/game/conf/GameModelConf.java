package com.epam.game.conf;

import com.epam.game.gamemodel.model.Model;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/15/2019
 */
@Configuration
public class GameModelConf {

    @Bean
    public Model modelInstance() {
        return Model.getInstance();
    }
}
