package com.epam.game.exceptions;

public class GameIsFinishedException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public GameIsFinishedException(String msg) {
        super(msg);
    }
    
    public GameIsFinishedException(String msg, Throwable ex) {
        super(msg, ex);
    }

}
