package com.epam.game.exceptions;

public class RequestReadingException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public RequestReadingException(String msg) {
        super(msg);
    }
    
    public RequestReadingException(String msg, Throwable ex) {
        super(msg, ex);
    }

}
