package com.epam.game.bot.main;

import com.epam.game.bot.domain.Behaviors;
import com.epam.game.bot.domain.BotAction;
import com.epam.game.bot.domain.Planet;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Advanced implementation of bot's logic. Not finished yet.
 * 
 * @author Evgeny_Tetuhin
 *
 */
public class AdvancedLogic implements PlayerLogic{
    
    private final int EXPLORERS_MINIMAL_PLANET_SUPPORT = 100;
    private final double EXPLORERS_SENDING_RATE = 0.8;
    private final int INVADERS_MINIMAL_PLANET_SUPPORT = 100;
    
    @Override
    public List<BotAction> whatToDo(Map<Long, Planet> situation, String whoWeAre){
        List<BotAction> decision = new LinkedList<BotAction>();
        List<Planet> ourPlanets = selectByOwner(situation.values(), whoWeAre);
        markPlanets(ourPlanets);
        for(Planet p : ourPlanets){
            decision.addAll(getActions(p));
        }
        return decision;
    }

    private List<BotAction> getActions(Planet p) {
        List<BotAction> actions = new LinkedList<BotAction>();
        switch(p.getBehavior()){
        case STORAGE:{
            //does nothing
            break;
        }
        case GENERATOR:{
            int toSend = p.getDroids() - (int) p.getBalanceValue();
            if(toSend > p.getNeighbours().size()){      // some inaccuracy still exists.
                for(Planet n : p.getNeighbours()){
                    actions.add(new BotAction(p.getId(), n.getId(), toSend/p.getNeighbours().size()));
                }
            }
            break;
        }
        case EXPLORER:{
            if(p.getDroids() > EXPLORERS_MINIMAL_PLANET_SUPPORT){
                int toSend = (int) (p.getDroids() * EXPLORERS_SENDING_RATE);
                for(Planet n : p.getEmptyNeighbours()){
                    actions.add(new BotAction(p.getId(), n.getId(), toSend/p.getEmptyNeighbours().size()));
                }
            }
            break;
        }
        case INVADER: {
            int toSend = p.getDroids() - INVADERS_MINIMAL_PLANET_SUPPORT;
            int allEnemyUnits = getAllUnits(p.getEnemies());
            if(toSend > p.getEnemies().size()){
                for(Planet e : p.getEnemies()){
                    actions.add(new BotAction(p.getId(), e.getId(), toSend * (e.getDroids() / allEnemyUnits)));
                }
            }
            break;
        }
        case NEEDY: {
            break;
        }
        case SPARTAN: {
            System.out.println("THIS IS SPAAARTAAAA!!1");
            break;
        }
        }
        return actions;
    }

    private List<Planet> selectByOwner(Collection<Planet> planets, String whoWeAre) {
        List<Planet> result = new LinkedList<Planet>();
        for(Planet p : planets){
            if(p.getOwner() != null && p.getOwner().equals(whoWeAre)){
                result.add(p);
            }
        }
        return result;
    }

    private void markPlanets(Collection<Planet> planets) {
        for(Planet p : planets){
            Behaviors behavior = null;
            if(p.hasEnemies()){
                List<Planet> enemies = p.getEnemies();
                if(getAllUnits(enemies) < p.getDroids()){
                    behavior = Behaviors.INVADER;
                } else if(p.hasConfederates()){
                    behavior = Behaviors.NEEDY;
                } else {
                    behavior = Behaviors.SPARTAN;
                }
            } else{
                if(p.hasEmptyNeighbours()){
                    behavior = Behaviors.EXPLORER;
                } else {
                    behavior = Behaviors.GENERATOR;
                }
            }
            p.setBehavior(behavior);
        }
    }

    private int getAllUnits(List<Planet> planets) {
        int result = 0;
        for(Planet p : planets){
            result += p.getDroids();
        }
        return result;
    }
}
