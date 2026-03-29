package com.example.SpringSecurityEx3.config.security.filters;

import com.example.SpringSecurityEx3.config.security.authentications.CustomAuthentication;
import com.example.SpringSecurityEx3.config.security.managers.CustomAuthenticationManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final CustomAuthenticationManager customAuthenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Create an authentication object which is not yet authenticated
        // delegate the authentication object to the manager
        // get back the authentication from the manager
        // if the object is authenticated then send request to the next filter in the chain

        String key = String.valueOf(request.getHeader("key"));
        var customAuthentication = new CustomAuthentication(false, key);

        var authenticate = customAuthenticationManager.authenticate(customAuthentication);
        if (authenticate.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            filterChain.doFilter(request, response); // Only when the authentication worked
        }
    }
}
