package com.epam.game.controller.forms;

/**
 * Form for createGame page
 * 
 */
public class MultipleLoginForm {

    private Long idGame;
    private String logins;
    
    
    public String getLogins() {
        return logins;
    }
    public void setLogins(String logins) {
        this.logins = logins;
    }
    public Long getIdGame() {
        return idGame;
    }
    public void setIdGame(Long id) {
        this.idGame = id;
    }

}
