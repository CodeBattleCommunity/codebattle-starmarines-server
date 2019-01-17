package com.epam.game.domain;

import java.util.List;

/**
 * 
 * @author Roman_Spiridonov
 * 
 */
public class Client {

    private Long id;
    private String login;
    private String userName;
    private List<Authority> authorities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public boolean hasAnyRole(String roles) {
        if(authorities == null){
            return false;
        }
        String[] arrRoles = roles.split(",");
        for (Authority authority : authorities) {
            for (String role : arrRoles) {
                role = role.trim();
                if (authority.getAuthority().equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canCreateAGame() {
        return true;    // everyone can.
    }

    public boolean canControlGame(Game game) {
        return this.id == game.getCreatorId() || hasAnyRole(Authority.ROLE_ADMIN.getAuthority());
    }
}
