package com.epam.game.bot.main;

import com.epam.game.bot.domain.BotAction;
import com.epam.game.bot.domain.Planet;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Minimal implementation of bot's logic.
 * 
 * @author Evgeny_Tetuhin
 *
 */
public class SimpleLogic implements PlayerLogic {
    
    double SENDING_RATE = 0.2;              // sending rate (compared to number of droids on the planet)
    int MINIMAL_PLANET_SUPPORT = 20;       // minimal support - minimal number of droids to be maintained on the planet
    double MINIMAL_SENDING = 10;          // minimal rate of units to be sended to a new, empty planet
    
    @Override
    public List<BotAction> whatToDo(Map<Long, Planet> situation, String whoWeAre){
        List<BotAction> decision = new LinkedList<BotAction>();
        for(Planet p : findPlanetsByOwner(situation.values(), whoWeAre)){   // select all our planets,
            decision.addAll(getActions(p));                                 // perform expansion strategy on each one
        }
        return decision;
    }

    private List<? extends BotAction> getActions(Planet p) {
        List<BotAction> actions = new LinkedList<BotAction>();
        int whatWeHave = p.getDroids();
        int whatWeSend = Math.max(whatWeHave - MINIMAL_PLANET_SUPPORT, 0);
        if(whatWeSend > MINIMAL_SENDING){
            List<Planet> whatIsNear = p.getNeighbours();
            List<Planet> empties = findPlanetsByOwner(whatIsNear, null);
            List<Planet> ours = findPlanetsByOwner(whatIsNear, p.getOwner());
            List<Planet> enemies = new LinkedList<Planet>(whatIsNear);
            enemies.removeAll(empties);
            enemies.removeAll(ours);
            
            if(enemies.isEmpty()){
                for(Planet n : ours){
                    actions.add(new BotAction(p.getId(), n.getId(), whatWeSend/(ours.size() + empties.size())));
                }
                for(Planet n : empties){
                    actions.add(new BotAction(p.getId(), n.getId(), whatWeSend/(ours.size() + empties.size())));
                }
            } else {
                for(Planet e : enemies){
                    if(whatWeSend > e.getDroids()){
                        actions.add(new BotAction(p.getId(), e.getId(), e.getDroids()));
                        whatWeSend -= e.getDroids();
                    }
                }
            }
        }
        return actions;
    }

    private List<Planet> findPlanetsByOwner(Collection<Planet> allPlanets, String whoWeAre) {
        List<Planet> ourBases = new LinkedList<Planet>();
        for(Planet p : allPlanets){
            if(whoWeAre == null){
                if(p.getOwner() == null){
                    ourBases.add(p);
                }
            } else {
                if(whoWeAre.equals(p.getOwner())){
                    ourBases.add(p);
                }
            }
                    
        }
        return ourBases;
    }
}
