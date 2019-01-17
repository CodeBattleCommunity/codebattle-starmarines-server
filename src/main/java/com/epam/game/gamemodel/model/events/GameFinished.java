package com.epam.game.gamemodel.model.events;

import java.util.EventObject;

public class GameFinished extends EventObject{

    private static final long serialVersionUID = 1L;

    public GameFinished(Object source) {
        super(source);
    }
    
}
