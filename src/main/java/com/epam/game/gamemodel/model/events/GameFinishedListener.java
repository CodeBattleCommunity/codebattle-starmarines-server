package com.epam.game.gamemodel.model.events;

import java.util.EventObject;

public interface GameFinishedListener {
    public void afterGameFinished(EventObject e);
}
