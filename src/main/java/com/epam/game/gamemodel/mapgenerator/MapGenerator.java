package com.epam.game.gamemodel.mapgenerator;

import com.epam.game.domain.User;
import com.epam.game.gamemodel.model.Vertex;
import com.epam.game.gamemodel.naming.NamingHandler;

import java.util.Map;

/**
 * Interface for generating a game field.
 * 
 * @author Evgeny_Tetuhin
 * 
 */
public abstract class MapGenerator {

    protected NamingHandler namingHandler;
    
    /**
     * Generates a map for players.
     * 
     * @param players
     * @return map to be added into GameInstance
     */
    abstract public Map<Long, Vertex> generate(Map<Long, User> players);
    
    /**
     * Sets the handler for planets names generation.
     */
    public void setNamingHandler(NamingHandler handler){
        this.namingHandler = handler;
    }
}
