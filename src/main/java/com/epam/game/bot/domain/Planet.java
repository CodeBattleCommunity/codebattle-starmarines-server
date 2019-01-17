package com.epam.game.bot.domain;

import java.util.LinkedList;
import java.util.List;


/**
 * Representation for a planet in bot's logic
 * 
 * @author Evgeny_Tetuhin
 *
 */
public class Planet {

    private Long id;
    private String owner;
    private int droids;
    private PlanetType type;
    private List<Planet> neighbours;
    private Behaviors behavior;
    
    public Planet(){
        neighbours = new LinkedList<Planet>();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getDroids() {
        return droids;
    }

    public void setDroids(int droids) {
        this.droids = droids;
    }

    public PlanetType getType() {
        return type;
    }

    public void setType(PlanetType type) {
        this.type = type;
    }

    public List<Planet> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Planet> neighbours) {
        this.neighbours = neighbours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNeighbour(Planet neighbour) {
        neighbours.add(neighbour);
    }
    
    /**
     * @return true, if planet's neighbors list contains planets owned by other players.
     */
    public boolean hasEnemies(){
        for(Planet p : neighbours){
            if(p.getOwner() != null && !p.getOwner().equals(this.getOwner())){
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return list of neighboring planets, owned by other players.
     */
    public List<Planet> getEnemies(){
        List<Planet> result = new LinkedList<Planet>();
        for(Planet n : neighbours){
            if(n.getOwner() != null && !n.getOwner().equals(this.owner)){
                result.add(n);
            }
        }
        return result;
    }
    
    /**
     * @return list of neighboring planets, owned by the same player
     */
    public List<Planet> getConfederates(){
        List<Planet> result = new LinkedList<Planet>();
        for(Planet n : neighbours){
            if(n.getOwner() != null && n.getOwner().equals(this.owner)){
                result.add(n);
            }
        }
        return result;
    }
    
    /**
     * @return list of neighboring empty planets
     */
    public List<Planet> getEmptyNeighbours(){
        List<Planet> result = new LinkedList<Planet>();
        for(Planet n : neighbours){
            if(n.getOwner() == null){
                result.add(n);
            }
        }
        return result;
    }
    
    /**
     * The balance value is a number of units, which produces maximal number of new units.
     * (Being regenerated, a planet with balance value reaches limit of regeneration.)
     * For example, for a planet with regeneration rate = 0.5 and regeneration limit = 1000
     * the balance value is 666.(6)
     * 
     * @return the balance value
     */
    public double getBalanceValue(){
        return type.getMaxUnits() / (1 + type.getRegenerationRate());
    }
    
    /**
     * Potential output is number of units, that can be regenerated from planet's balance value.
     * 
     * @return potential output.
     */
    public int getPotentialOutput(){
        return (int)(type.getMaxUnits() - getBalanceValue());
    }

    /**
     * @return true if the planet has neighbors owned by the same player.
     */
    public boolean hasConfederates() {
        for(Planet p : neighbours){
            if(p.getOwner() != null && p.getOwner().equals(this.getOwner())){
                return true;
            }
        }
        return false;
    }

    public Behaviors getBehavior(){
        return this.behavior;
    }
    
    public void setBehavior(Behaviors behavior) {
        this.behavior = behavior;
    }

    /**
     * @return true if the planet has empty neighbors.
     */
    public boolean hasEmptyNeighbours() {
        for(Planet n : neighbours){
            if(n.getOwner() == null){
                return true;
            }
        }
        return false;
    }
}
