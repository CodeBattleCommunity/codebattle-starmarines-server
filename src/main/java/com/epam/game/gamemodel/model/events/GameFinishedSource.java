package com.epam.game.gamemodel.model.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameFinishedSource {
    
    private List<GameFinishedListener> _listeners = new ArrayList<GameFinishedListener>();

    public synchronized void addEventListener(GameFinishedListener listener) {
        _listeners.add(listener);
    }

    public synchronized void removeEventListener(GameFinishedListener listener) {
        _listeners.remove(listener);
    }

    public synchronized void fire() {
        GameFinished event = new GameFinished(this);
        Iterator<GameFinishedListener> i = _listeners.iterator();
        while (i.hasNext()) {
            ((GameFinishedListener) i.next()).afterGameFinished(event);
        }
    }
}
