package com.epam.game.gamemodel.model.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameAbandonedSource {
    
    private List<GameAbandonedListener> _listeners = new ArrayList<GameAbandonedListener>();

    public synchronized void addEventListener(GameAbandonedListener listener) {
        _listeners.add(listener);
    }

    public synchronized void removeEventListener(GameAbandonedListener listener) {
        _listeners.remove(listener);
    }

    public synchronized void fire() {
        GameAbandoned event = new GameAbandoned(this);
        Iterator<GameAbandonedListener> i = _listeners.iterator();
        while (i.hasNext()) {
            ((GameAbandonedListener) i.next()).afterGameAbandoned(event);
        }
    }
}
