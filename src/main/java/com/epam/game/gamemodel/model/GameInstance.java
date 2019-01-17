package com.epam.game.gamemodel.model;

import com.epam.game.bot.main.Bot;
import com.epam.game.constants.GameType;
import com.epam.game.domain.GameSettings;
import com.epam.game.domain.User;
import com.epam.game.exceptions.IllegalCommandException;
import com.epam.game.exceptions.NotEnoughPlayersException;
import com.epam.game.gameinfrastructure.requessthandling.SocketResponseSender;
import com.epam.game.gamemodel.mapgenerator.MapGenerator;
import com.epam.game.gamemodel.model.events.GameAbandoned;
import com.epam.game.gamemodel.model.events.GameAbandonedListener;
import com.epam.game.gamemodel.model.events.GameFinished;
import com.epam.game.gamemodel.model.events.GameFinishedListener;

import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Logger;

/**
 * Represents instance of a game field.
 * 
 * @author Evgeny_Tetuhin
 * 
 */
public class GameInstance extends Observable {

    /**
     * A transfer object to send information about units moving.
     * 
     * @author Evgeny_Tetuhin
     * 
     */
    public class Change { // in the event of sickness use refactoring.

        public long from;

        public long to;

        public int count;

        public Change(long from, long to, int count) {
            this.from = from;
            this.to = to;
            this.count = count;
        }

    }

	private Logger log = Logger.getLogger(GameInstance.class.getName());

	private MapGenerator mapGenerator;

	private long id;

	private Map<Long, Vertex> vertices;

	private Map<Long, User> players;

	private List<Change> currentChanges; // Changes performed after last turn

	private List<Change> lastTurnChanges; // Changes performed before last turn

	private List<User> survivors;

	private boolean started;

	private boolean finished;

	private GameType type;

	private List<Bot> bots;

	private long turnsNumber;

	private String title;

	private LinkedList<UserScore> statistic;

	private List<GameFinishedListener> finishListeners;

	private List<GameAbandonedListener> abandonListeners;

	private User creator;

	private Timestamp timeCreated;

	private GameSettings gameSettings;

	public GameInstance(long id, GameType type, GameSettings gameSettings, User creator) {
		this.vertices = new HashMap<>();
		this.players = new HashMap<>();
		this.currentChanges = new LinkedList<>();
		this.lastTurnChanges = new LinkedList<>();
		this.survivors = new LinkedList<>();
		this.id = id;
		this.type = type;
		this.bots = new LinkedList<>();
		this.finishListeners = new ArrayList<>();
		this.abandonListeners = new ArrayList<>();
		this.statistic = new LinkedList<>();
		this.creator = creator;
	    this.timeCreated = new Timestamp(System.currentTimeMillis());
	    this.gameSettings = gameSettings;
        addObserver(SocketResponseSender.getInstance());
    }

    public GameInstance(long id, MapGenerator generator, GameType type, GameSettings gameSettings, User creator) {
        this(id, type, gameSettings, creator);
        this.mapGenerator = generator;
    }

    /**
     * Move units from one vertex to another.
     * 
     * @param vertexId1
     *            - initial vertex id
     * @param vertexId2
     *            - destination vertex id
     * @param unitsCount
     *            - amount of units
     * @param player
     *            - owner of an initial vertex and units
     * @throws IllegalCommandException
     */
    public synchronized void move(long vertexId1, long vertexId2, int unitsCount, User player) throws IllegalCommandException {
        String command = String.format("Command ignored: [from %d to %d, %d units]", vertexId1, vertexId2, unitsCount);
        String errorMsg = null;
        Vertex from = vertices.get(vertexId1);
        Vertex to = vertices.get(vertexId2);
        if (!vertices.containsKey(vertexId1) || !vertices.containsKey(vertexId2)) {
            errorMsg = "Specified vertex does not exist";
        } else if (from.getOwner() == null || from.getOwner().getId() != player.getId()) {
            errorMsg = "Specified user is not an owner of the vertex.";
        } else if (!from.connectedWith(to)) {
            errorMsg = "Can not move units: no link between specified vertices.";
        } else if (from.getUnitsCount() < unitsCount) {
            errorMsg = "Specified number of units exceeds units count in the vertex.";
        } else if (unitsCount < 0) {
            errorMsg = "Negative number of units cannot be sent.";
        } else {
            try {
                from.decreaseUnits(unitsCount);
                to.addUnits(player, unitsCount);
                currentChanges.add(new Change(vertexId1, vertexId2, unitsCount));
            } catch (Exception e) {
                errorMsg = e.getMessage();
            }
        }
        if (errorMsg != null) {
            throw new IllegalCommandException(command + " - " + errorMsg);
        }
    }

    /**
     * Add player to the game.
     * 
     * @param player
     */
    public void addPlayer(User player) {
        if (started || finished) {
            throw new IllegalStateException("Player cannot be added to a started game");
        }
        if (isFull()) {
            throw new IllegalStateException("The game reached its limit of players.");
        }
        players.put(player.getId(), player);
    }

    /**
     * Prepare data for the next turn.
     */
    private synchronized void recalculate() {
        List<User> newSurvivors = new LinkedList<User>();
        for (Vertex v : vertices.values()) {
            v.recalculate();
            if (v.getOwner() != null && !newSurvivors.contains(v.getOwner())) {
                newSurvivors.add(v.getOwner());
            }
        }
        List<User> srv = new LinkedList<User>(survivors);
        srv.removeAll(bots);
        List<User> defeated = new LinkedList<User>(survivors);
        defeated.removeAll(newSurvivors);
        if(!defeated.isEmpty()) {
            addDefeated(defeated);
        }
        survivors = newSurvivors;
        if (survivors.size() == 1 || survivors.size() == 0 || srv.isEmpty() || turnsNumber >= gameSettings.getRoundTurns()) {
            finish();
        }
    }
    
    private void addDefeated(List<User> defeated) {
        for(User u : defeated){
            UserScore score = new UserScore();
            score.setTurnsSurvived((int)this.turnsNumber);
            score.setUser(u);
            score.setPlace(0);
	        score.setType(this.getType());
	        statistic.add(score);
        }
    }
    
    private void recalculateScore() {
        Comparator<UserScore> comparator = new Comparator<UserScore>() {

            @Override
            public int compare(UserScore o1, UserScore o2) {
                if(o1.getUnitsCount() == o2.getUnitsCount()){
                    return o2.getTurnsSurvived() - o1.getTurnsSurvived();
                } else {
                    return o2.getUnitsCount() - o1.getUnitsCount();
                }
            }
        };
        Collections.sort(this.statistic, comparator);
        int currentPlace = 1;
        Iterator<UserScore> i = statistic.iterator();
        UserScore prevScore = i.next();
        prevScore.setPlace(currentPlace);
        UserScore curScore;
        while(i.hasNext()){
            curScore = i.next();
            if(comparator.compare(prevScore, curScore) != 0) {
                currentPlace++;
            }
            curScore.setPlace(currentPlace);
            prevScore = curScore;
        }
    }
    
    private void finishStatistics() {
        Map<User, Integer> users = new HashMap<User, Integer>(players.size());
        int count;
        for(Vertex v : vertices.values()){
            if(v.getOwner() != null){
               count = v.getUnitsCount();
                if(users.containsKey(v.getOwner())){
                    count += users.get(v.getOwner());
                }
                users.put(v.getOwner(), count);
            }
        }
        for(Map.Entry<User, Integer> entry : users.entrySet()){
            UserScore us = new UserScore();
            us.setTurnsSurvived((int) this.turnsNumber);
            us.setUser(entry.getKey());
            us.setUnitsCount(entry.getValue());
	        us.setType(this.getType());
            this.statistic.addLast(us);
        }
        this.recalculateScore();
        System.out.println(statistic.getFirst().getPlace());
    }
    
    /**
     * Finishes the game.
     */
    private void finish() {
        synchronized (this) {
            finished = true;
            finishStatistics();
        }
        for (Bot bot : bots) {
            players.remove(bot.getUser().getId());
        }
        GameFinished event = new GameFinished(this);
        for (GameFinishedListener listener : finishListeners) {
            listener.afterGameFinished(event);
        }
        if (players.isEmpty()) {
            this.abandon();
        }
    }

    /**
     * Starts the game.
     * 
     * @throws Exception
     *             - if number of players in the game is less than 2.
     */
    public void start() throws NotEnoughPlayersException {
        if (players.size() < 2) {
            throw new NotEnoughPlayersException("Not enough players to start game");
        }
        if (mapGenerator == null) {
            throw new IllegalStateException("Map generator was not set.");
        }
        started = true;
        vertices = mapGenerator.generate(players);
        turnsNumber = 0;
        survivors.addAll(players.values());
        recalculate();
        setChanged();
        notifyObservers(vertices);
    }

    /**
     * Moves the game to the next turn.
     * 
     * @throws IllegalStateException
     *             if game is finished or not started.
     */
    public synchronized void nextTurn() {
        if (!isStarted() || isFinished()) {
            throw new IllegalStateException();
        }
        this.recalculate();
        setChanged();
        notifyObservers(vertices);
        lastTurnChanges = currentChanges;
        currentChanges = new LinkedList<Change>();
        turnsNumber++;
        performBotsActions();
    }

    /**
     * @return true if the game is started.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * @return true if the game is finished (and the winner is known).
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Checks whether the user with specified id plays this game.
     * 
     * @param userId
     *            - user to check
     * @return - true if a user with userId plays this game.
     */
    public boolean hasPlayerWithId(Long userId) {
        return players.containsKey(userId);
    }

    public boolean hasPlayerWithToken(String token) {
        for (Map.Entry<Long, User> entry : players.entrySet()) {
            if (entry.getValue().getToken().equals(token)) {
                return true;
            }
        }
        return false;
    }

    public long getId() {
        return id;
    }

    public User getUserById(long uid) {
        return players.get(uid);
    }

    public User getUserByToken(String token) {
        for (Map.Entry<Long, User> entry : players.entrySet()) {
            if (entry.getValue().getToken().equals(token)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void addBot(User user) {
        bots.add(new Bot(user));
    }

    /**
     * @return number of players registered in the game.
     */
    public int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * @return List of {@link GameInstance.Change} representing changes at the
     *         last turn.
     */
    public List<Change> getChanges() {
        return new LinkedList<Change>(this.lastTurnChanges);
    }

    /**
     * @return LinkedList of all vertices at the game field.
     */
    public List<Vertex> getVertices() {
        return new LinkedList<Vertex>(vertices.values());
    }

    /**
     * Deletes user by id.
     * 
     * @param id
     */
    public void deleteUser(Long id) {
        deleteUser(id, false);
    }

    public synchronized void deleteUser(Long id, boolean ignoreAbandonment) {
        User leaver = players.get(id);
        for (Vertex v : vertices.values()) {
            v.deleteUsersUnits(leaver);
        }
        players.remove(id);
        if (!ignoreAbandonment && players.isEmpty()) {
            abandon();
        }
    }

    /**
     * @return type of the game
     */
    public GameType getType() {
        return type;
    }

    /**
     * Adds listener for GameFinished event.
     * 
     * @param listener
     */
    public void addFinishListener(GameFinishedListener listener) {
        finishListeners.add(listener);
    }

    /**
     * Removes listener for GameFinished event.
     * 
     * @param listener
     */
    public void removeFinishListener(GameFinishedListener listener) {
        finishListeners.remove(listener);
    }

    /**
     * Adds listener for GameAbandoned event.
     * 
     * @param listener
     */
    public void addAbandonListener(GameAbandonedListener listener) {
        abandonListeners.add(listener);
    }

    /**
     * Removes listener for GameFinished event.
     * 
     * @param listener
     */
    public void removeAbandonListener(GameAbandonedListener listener) {
        abandonListeners.remove(listener);
    }

    /**
     * @return winner of the game or <code>null</code> if all players left the
     *         game
     * @throws <code>IllegalStateException</code> if the game is not finished.
     */
    public List<User> getWinners() {
        List<User> result = new LinkedList<User>();
        for(UserScore us : statistic){
            if(us.getPlace() == 1){
                result.add(us.getUser());
            }
        }
        return result;
    }

    /**
     * @return Number of turns passed since game start.
     */
    public long getTurnsNumber() {
        return turnsNumber;
    }

    /**
     * @return all players from the game
     */
    public List<User> getPlayers() {
        return new LinkedList<User>(players.values());
    }

    public void performBotsActions() {
        for (Bot b : bots) {
            b.makeTurn(this);
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public List<User> getBots() {
        List<User> result = new LinkedList<User>();
        for (Bot bot : bots) {
            result.add(bot.getUser());
        }
        return result;
    }

    public void setMapGenerator(MapGenerator generator) {
        if (this.isStarted()) {
            throw new IllegalStateException("Game already started and generator can not be changed.");
        }
        this.mapGenerator = generator;
    }

    private void abandon() {
        GameAbandoned event = new GameAbandoned(this);
        for (GameAbandonedListener listener : abandonListeners) {
            listener.afterGameAbandoned(event);
        }
        System.out.println("The game " + this.id + " successfully abandoned.");
    }

    public LinkedList<UserScore> getStatistics() {
        return new LinkedList<UserScore>(this.statistic);
    }

    public User getCreator() {
        return creator;
    }

    public boolean isFull() {
        return players.size() >= gameSettings.getMaxPlayers();
    }

    private void setCreator(User creator) { // so far creator must not be changed
        this.creator = creator;
    }

	public Timestamp getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Timestamp timeCreated) {
		this.timeCreated = timeCreated;
	}
}
