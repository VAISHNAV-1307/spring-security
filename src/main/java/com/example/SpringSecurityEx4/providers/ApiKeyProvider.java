package com.example.SpringSecurityEx4.providers;

import com.example.SpringSecurityEx4.authentications.ApiKeyAuthentications;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@AllArgsConstructor
public class ApiKeyProvider implements AuthenticationProvider {

    private final String key;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ApiKeyAuthentications auth = (ApiKeyAuthentications) authentication;
        if (key.equals(auth.getKey())){
            auth.setAuthenticated(true);
            return auth;
        }
        throw new BadCredentialsException("Oh no");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthentications.class.equals(authentication);
    }
}
