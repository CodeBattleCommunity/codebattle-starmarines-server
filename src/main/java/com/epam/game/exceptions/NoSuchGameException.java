package com.epam.game.exceptions;

/**
 * 
 * @author Andrey_Eremeev
 *
 */
public class NoSuchGameException extends Exception {

    private static final long serialVersionUID = 1L;

    public NoSuchGameException(String msg) {
        super(msg);
    }
    
    public NoSuchGameException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
