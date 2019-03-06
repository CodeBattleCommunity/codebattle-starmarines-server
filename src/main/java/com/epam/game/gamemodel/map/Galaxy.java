package com.epam.game.gamemodel.map;

import com.epam.game.domain.User;
import com.epam.game.gameinfrastructure.commands.server.GalaxySnapshot;
import com.epam.game.gamemodel.model.Edge;
import com.epam.game.gamemodel.model.Vertex;
import com.epam.game.gamemodel.model.disaster.Disaster;
import com.epam.game.gamemodel.naming.NamingHandler;

import java.util.List;
import java.util.Map;

/**
 * Interface for generating a game field.
 * 
 * @author Evgeny_Tetuhin
 * 
 */
public abstract class Galaxy {

    protected NamingHandler namingHandler;
    
    /**
     * Generates a map for players.
     * 
     * @param players
     * @return map to be added into GameInstance
     */
    public abstract void generate(Map<Long, User> players);
    
    /**
     * Sets the handler for planets names generation.
     */
    public void setNamingHandler(NamingHandler handler){
        this.namingHandler = handler;
    }

    public abstract GalaxySnapshot makeSnapshot();

    public abstract Map<Long, Vertex> getPlanets();

    public abstract List<Disaster> generateDisasters();

    public abstract List<Edge> generatePortals();

    public abstract int moveUnits(User player, Vertex from, Vertex to, int unitsCount) throws Exception;

    public abstract void recalculateVertex(Vertex v);
}
