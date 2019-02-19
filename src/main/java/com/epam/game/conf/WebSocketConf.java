package com.epam.game.conf;

import com.epam.game.gameinfrastructure.requessthandling.ServerWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/17/2019
 */
@Configuration
@EnableWebSocket
public class WebSocketConf implements WebSocketConfigurer {

    @Autowired
    private ServerWebSocketHandler socketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, "/galaxy")
                .setAllowedOrigins("*");
    }

    @Bean
    @ConfigurationProperties("websocket")
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        return new ServletServerContainerFactoryBean();
    }
}
