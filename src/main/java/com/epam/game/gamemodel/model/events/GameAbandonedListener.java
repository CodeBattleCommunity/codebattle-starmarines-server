package com.epam.game.gamemodel.model.events;

import java.util.EventObject;

public interface GameAbandonedListener {
    public void afterGameAbandoned(EventObject e);
}