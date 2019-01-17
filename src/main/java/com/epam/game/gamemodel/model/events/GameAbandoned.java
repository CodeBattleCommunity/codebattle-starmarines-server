package com.epam.game.gamemodel.model.events;

import java.util.EventObject;

public class GameAbandoned extends EventObject{

    private static final long serialVersionUID = 1L;

    public GameAbandoned(Object source) {
        super(source);
    }
    
}