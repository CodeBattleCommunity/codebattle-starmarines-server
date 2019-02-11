package com.epam.game.gameinfrastructure.commands;

import com.epam.game.gameinfrastructure.UserSessionState;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/18/2019
 */
public interface CommandManager {
    UserSessionState handleUserCommands(WebSocketSession session, String token, String clientPayload);

    UserSessionState handleNewUserConnection(WebSocketSession session, String token);

    void handleLeavingUser(String token);
}
