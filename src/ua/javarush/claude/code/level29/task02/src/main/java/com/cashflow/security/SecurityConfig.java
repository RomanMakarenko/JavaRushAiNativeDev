package com.cashflow.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Конфігурація безпеки. High-risk: використовує WebSecurityConfigurerAdapter,
 * вилучений у Spring Security 6. Тестів немає, під активною паралельною розробкою.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/reports/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}