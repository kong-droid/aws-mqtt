package com.example.mqtt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    public static final String[] PERMIT_ANT_PATH = {
            "/aws/mqtt/**"
    };

    public static final String[] SWAGGER_ANT_PATH = {
            "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests()
                .antMatchers(SWAGGER_ANT_PATH).permitAll()
                .antMatchers(PERMIT_ANT_PATH).permitAll()
                .anyRequest().authenticated()
                .and().cors()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
    }
}
