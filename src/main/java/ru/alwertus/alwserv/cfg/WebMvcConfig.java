package ru.alwertus.alwserv.cfg;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//@Log4j2
//@Configuration
//@EnableWebMvc
public class WebMvcConfig/* implements WebMvcConfigurer*/ {

/**
     * HttpStatus.NOT_FOUND = 404 page
     * return "/" page for all unknown pages
     */

/*    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerCustomizer() {
        return container -> container.addErrorPages(
                new ErrorPage(HttpStatus.NOT_FOUND, "/"),
                new ErrorPage(HttpStatus.FORBIDDEN, "/error.html"));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info(">> ADD CORS MAPPINGS ");
        registry.addMapping("/auth/login").allowedHeaders("Access-Control-Allow-Origin");
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*");
    }*/
}
