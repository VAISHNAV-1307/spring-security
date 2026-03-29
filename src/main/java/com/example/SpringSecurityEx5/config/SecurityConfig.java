package com.example.SpringSecurityEx5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll() // endpoint level authorization
//                                .anyRequest().authenticated()
//                                .anyRequest().denyAll()
//                                .anyRequest().hasAnyAuthority("read", "write")
//                                .anyRequest().hasAnyRole()
//                                .anyRequest().hasAuthority("read")
//                                .anyRequest().hasRole("ADMIN")
                                .anyRequest().access(new WebExpressionAuthorizationManager("isAuthenticated() and hasAuthority('read')")) // here we can use SpEL --> Authorization rules
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var uds = new InMemoryUserDetailsManager();
        var u1 = User
                .withUsername("vaishnav")
                .password(passwordEncoder().encode("12345"))
                .authorities("read")
//                .roles("ADMIN") // this is equivalent to the .authorities("ROLE_ADMIN")
                .build();

        var u2 = User
                .withUsername("chotya")
                .password(passwordEncoder().encode("12345"))
                .authorities("write")
//                .roles("MANAGER")
                .build();
        uds.createUser(u1);
        uds.createUser(u2);
        return uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
