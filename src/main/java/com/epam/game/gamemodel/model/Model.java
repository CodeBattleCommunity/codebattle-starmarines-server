package com.epam.game.gamemodel.model;

import com.epam.game.constants.GameState;
import com.epam.game.constants.GameType;
import com.epam.game.dao.GameDAO;
import com.epam.game.dao.UserDAO;
import com.epam.game.domain.Game;
import com.epam.game.domain.User;
import com.epam.game.gamemodel.map.Galaxy;
import com.epam.game.gamemodel.map.GalaxyFactory;
import com.epam.game.gamemodel.model.events.GameAbandonedListener;
import com.epam.game.gamemodel.model.events.GameFinishedListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Stores and maintains all game instances.
 * 
 * @author Evgeny_Tetuhin
 * 
 */
@Resource
public class Model {

    private Map<Long, GameInstance> games = new ConcurrentHashMap<>();
    private Map<Long, GameInstance> gamesHistory = new ConcurrentHashMap<>();
    @Autowired
    private GameDAO gameDAO;
    @Autowired
    private UserDAO userDAO;

    @PostConstruct
    public void init() {
        loadPreviousGames();
    }

    private void loadPreviousGames() {
        List<Game> statistics = gameDAO.getStatistics();
        Map<Long, User> users = new HashMap<>();
        statistics.sort(Comparator.comparing(Game::getTimeCreated));
        statistics.forEach(game -> {
            User creator = users.computeIfAbsent(game.getCreatorId(), userDAO::getUserWith);
            gamesHistory.put(game.getGameId(), new GameInstance(game.getGameId(), game.getType(), game.getStatistics(), getUsers(game), creator, GalaxyFactory.getDefault()));
        });
    }

    private Map<Long, User> getUsers(Game game) {
        return game.getStatistics()
                .stream()
                .map(UserScore::getUser)
                .collect(Collectors.toMap(User::getId, Function.identity()));
    }

    /**
     * @param id
     *            - game id
     * @return - game instance
     */
    public GameInstance getGameById(Long id, boolean includeHistory) {
        Map<Long, GameInstance> games = new HashMap<>(this.games);
        if (includeHistory) {
            games.putAll(gamesHistory);
        }
        return games.get(id);
    }

    public GameInstance getGameById(Long id) {
        return getGameById(id, false);
    }

    /**
     * @param userId
     * @return {@link GameInstance} which that creator currently plays
     */
    public GameInstance getByUser(Long userId) {
        GameInstance result = null;
        for (GameInstance game : games.values()) {
            if (game.hasPlayerWithId(userId)) {
                result = game;
            }
        }
        return result;
    }

    public GameInstance getByToken(String token) {
        GameInstance result = null;
        for (GameInstance game : games.values()) {
            if (game.hasPlayerWithToken(token)) {
                result = game;
            }
        }
        return result;
    }



    /**
     * Creates a new game.
     * 
     * @param generator
     * @param id
     * @return
     */
    public GameInstance createNewGame(long id, GameType gameType, String title, User creator) {
        GameInstance newGame = new GameInstance(id, gameType, gameDAO.getSettings(), creator);
        newGame.setTitle(title);
        games.put(newGame.getId(), newGame);
        newGame.addFinishListener(new GameFinishedListener() {

            @Override
            public void afterGameFinished(EventObject e) {
                if ( !(e.getSource() instanceof GameInstance) ) {
                    return;
                }
                GameInstance finishedGame = (GameInstance)(e.getSource());
                Long id = finishedGame.getId();
                Game game = gameDAO.getById(id);
                if(game == null){
                    return;
                }
                if(finishedGame.getWinners().size() > 1) {
                    game.setWinnerId(-2);
                } else if(finishedGame.getWinners().size() == 1) {
                    game.setWinnerId(finishedGame.getWinners().get(0).getId());
                } else {
                    game.setWinnerId(-1);
                }
                game.setNumberOfTurns(finishedGame.getTurnsNumber());
                game.setState(GameState.FINISHED);
                gameDAO.updateGame(game,finishedGame);
            }
        });
        newGame.addAbandonListener(new GameAbandonedListener() {
            
            @Override
            public void afterGameAbandoned(EventObject e) {
                GameInstance abandonedGame = (GameInstance)(e.getSource());
                games.remove(abandonedGame.getId());
            }
        });
        return newGame;
    }
    
    public GameInstance createNewGame(Galaxy generator, long id, GameType gameType, String title, User creator) {
        GameInstance game = createNewGame(id, gameType, title, creator);
        game.setGalaxy(generator);
        return game;
    }

    /**
     * Returns all games which are not started yet.
     * 
     * @return
     */
    public Map<Long, GameInstance> getNotStartedGames() {
        Map<Long, GameInstance> result = new HashMap<Long, GameInstance>();
        for (Map.Entry<Long, GameInstance> game : games.entrySet()) {
            if (!game.getValue().isStarted()) {
                result.put(game.getKey(), game.getValue());
            }
        }
        return result;
    }

    /**
     * Returns all tournament games which is not started yet.
     * 
     * @return
     */
    public Map<Long, GameInstance> getNotStartedTournaments() {
        Map<Long, GameInstance> result = new HashMap<Long, GameInstance>();
        for (Map.Entry<Long, GameInstance> game : games.entrySet()) {
            if (!game.getValue().isStarted() && (GameType.PLAYER_TOURNAMENT.equals(game.getValue().getType())
                                                    || GameType.ADMIN_TOURNAMENT.equals(game.getValue().getType()))) {
                result.put(game.getKey(), game.getValue());
            }
        }
        return result;
    }

    public void deleteGameById(Long id) {
        games.remove(id);
    }

    public Map<Long, GameInstance> getAllTournaments(Long creatorId) {
        Map<Long, GameInstance> result = new HashMap<>();
        Map<Long, GameInstance> games = new HashMap<>(this.games);

        if (creatorId != null) {
            Map<Long, GameInstance> creatorGames = gamesHistory.entrySet()
                    .stream()
                    .filter(e -> e.getValue().getCreator().getId().equals(creatorId))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            games.putAll(creatorGames);
        } else {
            games.putAll(gamesHistory);
        }

        for (Map.Entry<Long, GameInstance> game : games.entrySet()) {
            if (GameType.PLAYER_TOURNAMENT.equals(game.getValue().getType())
                    || GameType.ADMIN_TOURNAMENT.equals(game.getValue().getType())) {
                result.put(game.getKey(), game.getValue());
            }
        }
        return result;
    }
}