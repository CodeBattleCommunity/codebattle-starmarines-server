package com.epam.game.bot.main;

import com.epam.game.bot.domain.BotAction;
import com.epam.game.bot.domain.Planet;
import com.epam.game.bot.domain.PlanetType;
import com.epam.game.domain.User;
import com.epam.game.exceptions.IllegalCommandException;
import com.epam.game.gamemodel.model.GameInstance;
import com.epam.game.gamemodel.model.Vertex;
import com.epam.game.gamemodel.model.VertexType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Some kind of adapter for using bot's logic implementation on the server side.
 * 
 * @author Evgeny_Tetuhin
 *
 */
public class Bot {
    
    private User bot;
    
    private Logger log = LoggerFactory.getLogger(Bot.class.getName());
    
    public Bot(User bot){
        this.bot = bot;
    }
    
    public User getUser(){
        return this.bot;
    }
    
    /**
     * Performs actions proposed by bot's logic
     * 
     * @param game - the game for applying actions.
     */
    public void makeTurn(GameInstance game){
        Map<Long, Planet> planets = convertVerticesToPlanets(game.getVertices());
        PlayerLogic botLogic = new SimpleLogic();
        List<BotAction> actions = botLogic.whatToDo(planets, bot.getUserName());
        for(BotAction a : actions){
            try {
                game.move(a.from, a.to, a.count, bot);
            } catch (IllegalCommandException e) {
                log.warn("Bot proposes a senseless action. An error has been thrown.");
            }
        }
    }

    private Map<Long, Planet> convertVerticesToPlanets(List<Vertex> vertices) {
        Map<VertexType, PlanetType> enumMap = new HashMap<VertexType, PlanetType>();
        enumMap.put(VertexType.TYPE_A, PlanetType.TYPE_A);
        enumMap.put(VertexType.TYPE_B, PlanetType.TYPE_B);
        enumMap.put(VertexType.TYPE_C, PlanetType.TYPE_C);
        enumMap.put(VertexType.TYPE_D, PlanetType.TYPE_D);
        
        Map<Long, Planet> planets = new HashMap<Long, Planet>();
        for(Vertex v : vertices){
            Planet p = new Planet();
            p.setDroids(v.getUnitsCount());
            p.setId(v.getId());
            String owner = (v.getOwner() == null) ? null
                    : v.getOwner().getUserName();
            p.setOwner(owner);
            p.setType(enumMap.get(v.getType()));
            planets.put(p.getId(), p);
        }
        for(Vertex v : vertices){
            Planet p = planets.get(v.getId());
            for(Vertex n : v.getNeighbours()){
                p.setNeighbour(planets.get(n.getId()));
            }
        }
        return planets;
    }
}
