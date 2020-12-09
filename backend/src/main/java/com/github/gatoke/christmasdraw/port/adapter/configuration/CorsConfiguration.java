package com.github.gatoke.christmasdraw.port.adapter.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("127.0.0.1", "http://localhost:3000", "http://192.168.0.192:3000", "http://localhost:8080")
                .allowedMethods("*");
    }
}
