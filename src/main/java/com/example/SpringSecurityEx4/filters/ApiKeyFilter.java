package com.example.SpringSecurityEx4.filters;

import com.example.SpringSecurityEx4.authentications.ApiKeyAuthentications;
import com.example.SpringSecurityEx4.managers.CustomAuthenticationManagers;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final String key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CustomAuthenticationManagers manager = new CustomAuthenticationManagers(key);
        String headerKey = String.valueOf(request.getHeader("key"));

        if (Strings.isNotEmpty(headerKey)) {
            filterChain.doFilter(request, response);
        }

        ApiKeyAuthentications authentication = new ApiKeyAuthentications(headerKey);
        try {
            var a = manager.authenticate(authentication);
            if (a.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(a);
            }
        } catch (AuthenticationException exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        filterChain.doFilter(request, response);
    }
}
