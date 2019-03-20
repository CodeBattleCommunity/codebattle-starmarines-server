package com.epam.game.gameinfrastructure.commands;

import com.epam.game.dao.GameDAO;
import com.epam.game.domain.User;
import com.epam.game.gameinfrastructure.UserSessionState;
import com.epam.game.gameinfrastructure.commands.client.ClientCommand;
import com.epam.game.gameinfrastructure.requessthandling.CommandConverter;
import com.epam.game.gameinfrastructure.requessthandling.PeerController;
import com.epam.game.gameinfrastructure.requessthandling.SocketResponseSender;
import com.epam.game.gamemodel.model.GameInstance;
import com.epam.game.gamemodel.model.Model;
import com.epam.game.gamemodel.model.action.impl.LoginAction;
import com.epam.game.gamemodel.model.action.impl.MoveAction;
import com.fasterxml.jackson.core.JsonParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/18/2019
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CommandManagerImpl implements CommandManager {

    private final CommandConverter commandConverter;

    private final Map<String, AtomicLong> activeTokens = new ConcurrentHashMap<>();

    private final GameDAO gameDAO;

    @Autowired
    @Lazy
    private Model gameModel;

    @Override
    public UserSessionState handleUserCommands(WebSocketSession session, String token, String clientPayload) {
        ClientCommand clientCommand = null;
        try {
            clientCommand = commandConverter.convertToClientCommand(clientPayload);
        } catch (JsonParseException parseException) {
            log.error("Invalid command received from client with token: {}\nCommand:\n{}", token, clientPayload);
        } catch (Exception e) {
            log.error("Invalid command received from client with token: " + token, e);
            return UserSessionState.invalid(CloseStatus.BAD_DATA, String.format("Invalid json command received from client '%s'." +
                    " Please fix your client and reconnect", clientPayload));
        }

        long actionsLimitPerCommand = gameDAO.getSettings().getPlayerActionsLimitPerCommand();

        if (clientCommand.getActions() != null && clientCommand.getActions().size() > actionsLimitPerCommand) {
            return UserSessionState.invalid(CloseStatus.TOO_BIG_TO_PROCESS, String.format("Not more than %s actions " +
                    "allowed per one command", actionsLimitPerCommand));
        }

        UserSessionState tokenValidation = validateUser(token);
        if (!tokenValidation.isValid()) {
            return tokenValidation;
        }

        GameInstance game = obtainGame(token);

        UserSessionState gameStateValidation = validateGameState(game);
        if (!gameStateValidation.isValid()) {
            return gameStateValidation;
        }

        PeerController pc = new PeerController(game.getUserByToken(token), session);

        clientCommand.getActions().forEach(action -> {
            new MoveAction(game, action, game.getUserByToken(token), pc).doAction();
        });

        SocketResponseSender srs = SocketResponseSender.getInstance();
        if (game != null) {
            srs.addSocketToGame(game, pc);
        }

        return UserSessionState.valid();
    }

    @Override
    public UserSessionState handleNewUserConnection(WebSocketSession session, String token) {
        activeTokens.computeIfAbsent(token, s -> new AtomicLong()).getAndIncrement();

        UserSessionState tokenValidation = validateUser(token);
        if (!tokenValidation.isValid()) {
            return tokenValidation;
        }

        GameInstance game = obtainGame(token);

        UserSessionState gameStateValidation = validateGameState(game);
        if (!gameStateValidation.isValid()) {
            return gameStateValidation;
        }

        PeerController pc = new PeerController(game.getUserByToken(token), session);

        new LoginAction(game, pc).doAction();

        return UserSessionState.valid();
    }

    @Override
    public void handleLeavingUser(String token) {
        activeTokens.get(token).getAndDecrement();
    }

    private UserSessionState validateUser(String token) {
        if (token == null) {
            return UserSessionState.invalid(CloseStatus.POLICY_VIOLATION, "Missing user token");
        }

        if (activeTokens.get(token).get() > 1) {
            return UserSessionState.invalid(CloseStatus.POLICY_VIOLATION, "User already connected");
        }

        return UserSessionState.valid();
    }

    private UserSessionState validateGameState(GameInstance game) {
        if (game == null) {
            return UserSessionState.invalid(CloseStatus.BAD_DATA, "Wrong user id. No game for current user.");
        }

        if (game.isFinished()){
            return UserSessionState.invalid(CloseStatus.NORMAL, generateWinnersMessage(game.getWinners()));
        }

        return UserSessionState.valid();
    }

    private GameInstance obtainGame(String token) {
        return gameModel.getByToken(token);
    }

    private String generateWinnersMessage(List<User> winners){
        StringBuilder msg =  new StringBuilder("Game is finished. ");
        if(winners.size() > 0) {
            for(int i = 0; i < winners.size(); i++){
                msg.append(winners.get(i).getUserName());
                switch(winners.size() - i){
                    case 1:{
                        msg.append(" ");
                        break;
                    }
                    case 2: {
                        msg.append(" and ");
                        break;
                    }
                    case 3: {
                        msg.append(", ");
                        break;
                    }
                }
            }
        }
        msg.append("won the game!");
        return msg.toString();
    }
}
