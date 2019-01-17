package com.epam.game.exceptions;

/**
 * Exception for a case of insufficient number of player.
 * 
 * @author Evgeny_Tetuhin
 *
 */
public class NotEnoughPlayersException extends Exception{

    private static final long serialVersionUID = 1L;
    
    public NotEnoughPlayersException(String msg) {
        super(msg);
    }
    
    public NotEnoughPlayersException(String msg, Throwable ex) {
        super(msg, ex);
    }

}
