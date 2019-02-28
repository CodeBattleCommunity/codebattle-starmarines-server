package com.epam.game.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author Roman_Spiridonov
 * 
 */
@Data
public class User implements UserDetails {

    private Long id;
    private String userName;
    private String login;
    private String password;
    private List<GrantedAuthority> authorities;
    private String token;
    private String email;
    private String phone;
    private boolean bot;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasAnyRole(String... roles) {
        List<GrantedAuthority> requestedRoles = Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return authorities.stream()
                .anyMatch(requestedRoles::contains);
    }

    public boolean canControlGame(Game game) {
        return this.id == game.getCreatorId() || hasAnyRole(Authority.ROLE_ADMIN.getAuthority());
    }

    public boolean canCreateAGame() {
        return true;    // everyone can.
    }
}
