package com.example.SpringSecurityEx4.managers;

import com.example.SpringSecurityEx4.providers.ApiKeyProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@AllArgsConstructor
public class CustomAuthenticationManagers implements AuthenticationManager {

    private final String key;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ApiKeyProvider provider = new ApiKeyProvider(key);
        if (provider.supports(authentication.getClass())) {
            return provider.authenticate(authentication);
        }
        return authentication;
    }
}
