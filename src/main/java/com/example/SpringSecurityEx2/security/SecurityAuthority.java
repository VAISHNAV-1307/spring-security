package com.example.SpringSecurityEx2.security;

import com.example.SpringSecurityEx2.entites.Authority;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;


@AllArgsConstructor
public class SecurityAuthority implements GrantedAuthority {

    private final Authority authority;

    @Override
    public String getAuthority() {
        return authority.getName();
    }
}
