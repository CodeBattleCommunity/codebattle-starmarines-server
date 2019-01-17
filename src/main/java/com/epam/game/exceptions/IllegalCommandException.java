package com.epam.game.exceptions;

/**
 * Represents exceptions during command processing.
 * 
 * @author Evgeny_Tetuhin
 *
 */
public class IllegalCommandException extends Exception{

    private static final long serialVersionUID = 1L;
    
    public IllegalCommandException(String msg) {
        super(msg);
    }
    
    public IllegalCommandException(String msg, Throwable ex) {
        super(msg, ex);
    }

}
