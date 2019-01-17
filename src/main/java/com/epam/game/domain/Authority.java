package com.epam.game.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * @author Roman_Spiridonov
 * 
 */
public enum Authority implements GrantedAuthority {

    ROLE_ADMIN, ROLE_USER;

    @Override
    public String getAuthority() {
        return toString();
    }
}
