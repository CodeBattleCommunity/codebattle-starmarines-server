package com.epam.game.conf.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/11/2019
 */
@Configuration
public class MvcConf implements WebMvcConfigurer {

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions("classpath:/tiles.xml");
        tilesConfigurer.setCheckRefresh(true);
        return tilesConfigurer;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        TilesViewResolver viewResolver = new TilesViewResolver();
        registry.viewResolver(viewResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/");
    }

//        @Bean(name = "viewResolver")
//        public ViewResolver getViewResolver() {
//            UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
//
//            // TilesView 3
//            viewResolver.setViewClass(TilesView.class);
//
//            return viewResolver;
//        }
//
//        @Bean(name = "tilesConfigurer")
//        public TilesConfigurer getTilesConfigurer() {
//            TilesConfigurer tilesConfigurer = new TilesConfigurer();
//            tilesConfigurer.setValidateDefinitions(false);
//            // TilesView 3
//            tilesConfigurer.setDefinitions("classpath:/tiles.xml");
//
//            return tilesConfigurer;
//        }
}
