package com.epam.game.gamemodel.model;

import com.epam.game.domain.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a vertex on a game field.
 * 
 * @author Evgeny_Tetuhin
 * 
 */
public class Vertex {

    private long id;
    private VertexType type;
    private User owner;
    private int ownersUnitsCount;
    private int ownersUnitsOutcome;
    private Map<Long, Vertex> neighbors;
    private Map<User, Integer> challengers;
    private Point coordinates;
    private String name;
    private boolean basePlanet;
    
    private Logger log = Logger.getLogger(Vertex.class.getName());

    /**
     * Creates a new vertex.
     * 
     * @param id
     *            - new vertex id
     * @param type
     *            - type of vertex
     */
    public Vertex(long id, VertexType type) {
        this.id = id;
        this.type = type;
        this.neighbors = new HashMap<Long, Vertex>();
        this.challengers = new HashMap<User, Integer>();
        coordinates = new Point(0, 0);
        
    }

    /**
     * Adds units of a specified player. On the next turn all units by all
     * players are recalculated.
     * 
     * @param player
     *            - units owner
     * @param unitsCount
     *            - number of units to be added
     * @throws Exception 
     */
    public void addUnits(User player, int unitsCount) throws Exception {
        if(unitsCount < 0){
            throw new Exception("Units number should not be negative.");
        }
        if (challengers.containsKey(player)) {
            unitsCount += challengers.get(player);
        }
        challengers.put(player, unitsCount);
    }

    /**
     * Connect that vertex with another and another vertex with that.
     * 
     * @param vertex
     *            - vertex to connect with.
     */
    public Edge interconnect(Vertex vertex) {
        this.oneWayConnect(vertex);
        vertex.oneWayConnect(this);
        return Edge.of(this.id, vertex.id);
    }

    public void disconnect(Vertex vertex) {
        this.oneWayDisconnect(vertex);
        vertex.oneWayDisconnect(this);
    }

    private void oneWayDisconnect(Vertex vertex) {
        this.neighbors.remove(vertex.getId());
    }

    public void setBasePlanet(boolean basePlanet) {
        this.basePlanet = basePlanet;
    }

    public boolean isBasePlanet() {
        return basePlanet;
    }

    /**
     * Create a one way link to another vertex.
     * 
     * @param vertex
     */
    private void oneWayConnect(Vertex vertex) {
        neighbors.put(vertex.id, vertex);
    }

    /**
     * @return {@link User} Owner of the vertex
     */
    public User getOwner() {
        return owner;
    }

    /**
     * @return Number of owner's units in the vertex
     */
    public int getUnitsCount() {
        return ownersUnitsCount;
    }

    /**
     * Decreases number of owner's units in the vertex. If {@code unitsCount} is
     * greater than number of units in the vertex (including any earlier decreasing),
     * {@link IllegalArgumentException} is thrown.
     * 
     * @param unitsCount
     * @throws Exception is trown if unitsCount exceeds number of existing units
     * (including just regenerated units)
     */
    public synchronized void decreaseUnits(int unitsCount, boolean safe) throws Exception {
        int outcome = ownersUnitsOutcome + unitsCount;
        if (getUnitsCount() >= outcome) {
            ownersUnitsOutcome += unitsCount;
        } else if (safe) {
            ownersUnitsOutcome = getUnitsCount();
        } else {
            throw new Exception(String.format("Specified decreasing exceeds number of units in the planet (planet id: %d, decreasing: %d, units left: %d", this.getId(), unitsCount, this.ownersUnitsCount - this.ownersUnitsOutcome));
        }
    }

    /**
     * Compares all new units and owner's units in the vertex. Player with
     * maximal number of units became a new owner. Number of his units in the
     * vertex is decreased by maximal number of units among remaining players.
     * 
     * Example: Suppose 10 red, 50 green, 55 blue and 60 black units in one
     * vertex. Once recalculate() is called, black team becoming an owner with 5
     * units (60 - 55).
     */
    public void recalculate() {
        ownersUnitsCount -= ownersUnitsOutcome;
        try {
            addUnits(owner, ownersUnitsCount);
            if(this.challengers.get(this.owner) < type.getMaxUnits()){
                ownersUnitsCount = this.challengers.get(this.owner);
                int regenerated = (int) (ownersUnitsCount + ownersUnitsCount * type.getRegenerationRate());
                this.challengers.put(this.owner, (regenerated > type.getMaxUnits()) ? type.getMaxUnits() : regenerated); 
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Serious troubles in recalculate(); it seems that ownersUnitsCount is negative number.", e);
        }
        int penult = 0;
        int max = 0;
        User potentialOwner = owner;
        for (Map.Entry<User, Integer> challenger : challengers.entrySet()) {
            if (challenger.getValue() > max) { // challenger is new maximum
                penult = max;
                max = challenger.getValue();
                potentialOwner = challenger.getKey();
            } else { // challenger still can be greater than penultimate number
                if (penult < challenger.getValue()) {
                    penult = challenger.getValue();
                }
            }
        }
        ownersUnitsCount = max;
        ownersUnitsCount -= penult;
        owner = (ownersUnitsCount > 0) ? potentialOwner : null;
        challengers.clear();
        ownersUnitsOutcome = 0;
    }

    public double getX() {
        return coordinates.getX();
    }

    public void setX(double x) {
        coordinates.setX(x);
    }

    public double getY() {
        return coordinates.getY();
    }

    public void setY(double y) {
        coordinates.setY(y);
    }

    public Point getPoint() {
        return coordinates;
    }

    public List<Vertex> getNeighbours() {
        return new LinkedList<Vertex>(neighbors.values());
    }

    public VertexType getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    public void setPosition(Point position) {
        setX(position.getX());
        setY(position.getY());
    }

    public boolean connectedWith(Vertex to) {
        return neighbors.containsValue(to);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * deletes owner's units from the vertex.
     */
    public void deleteUsersUnits(User user) {
        challengers.remove(user);
        if(user != null && user.equals(owner)){
            ownersUnitsOutcome = 0;
            ownersUnitsCount = 0;
            owner = null;
        }
    }
}
