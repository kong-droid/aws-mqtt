package com.example.mqtt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/templates/")
            .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
            .allowedOrigins(CorsConfiguration.ALL)
            .allowedHeaders(CorsConfiguration.ALL)
            .allowedMethods(CorsConfiguration.ALL)
            .maxAge(3600L);
    }
}
