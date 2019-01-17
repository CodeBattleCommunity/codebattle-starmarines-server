package com.epam.game.gamemodel.model;

import com.epam.game.constants.GameState;
import com.epam.game.constants.GameType;
import com.epam.game.dao.GameDAO;
import com.epam.game.domain.Game;
import com.epam.game.domain.User;
import com.epam.game.gamemodel.mapgenerator.MapGenerator;
import com.epam.game.gamemodel.model.events.GameAbandonedListener;
import com.epam.game.gamemodel.model.events.GameFinishedListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores and maintains all game instances.
 * 
 * @author Evgeny_Tetuhin
 * 
 */
@Resource
public class Model {

    private Map<Long, GameInstance> games;
    private static volatile Model model = new Model();
    private GameDAO gameDAO;

    private Model() {
        games = new HashMap<Long, GameInstance>();
    }

    @Autowired
    public void setGameDAO(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }
    
    public static Model getInstance() {
        return model;
    }

    /**
     * @param id
     *            - game id
     * @return - game instance
     */
    public GameInstance getGameById(Long id) {
        return games.get(id);
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
    
    public GameInstance createNewGame(MapGenerator generator, long id, GameType gameType, String title, User creator) {
        GameInstance game = createNewGame(id, gameType, title, creator);
        game.setMapGenerator(generator);
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

    public Map<Long, GameInstance> getAllTournaments() {
        Map<Long, GameInstance> result = new HashMap<Long, GameInstance>();
        for (Map.Entry<Long, GameInstance> game : games.entrySet()) {
            if (GameType.PLAYER_TOURNAMENT.equals(game.getValue().getType())
                    || GameType.ADMIN_TOURNAMENT.equals(game.getValue().getType())) {
                result.put(game.getKey(), game.getValue());
            }
        }
        return result;
    }
}