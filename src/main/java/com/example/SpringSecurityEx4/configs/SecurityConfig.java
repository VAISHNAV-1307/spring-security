package com.example.SpringSecurityEx4.configs;

import com.example.SpringSecurityEx4.filters.ApiKeyFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Value("${the.secret}")
    private String key;

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ApiKeyFilter apiKeyFilter = new ApiKeyFilter(key);
        return http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilterBefore(apiKeyFilter, BasicAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
