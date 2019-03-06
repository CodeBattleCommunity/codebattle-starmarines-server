package com.epam.game.gameinfrastructure.requessthandling;

import com.epam.game.gameinfrastructure.UserSessionState;
import com.epam.game.gameinfrastructure.commands.CommandManager;
import com.epam.game.gameinfrastructure.parser.RequestXMLTag;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/17/2019
 */
@Component
public class ServerWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private CommandConverter commandConverter;

    @Autowired
    private CommandManager commandManager;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        UserSessionState userSessionState = commandManager.handleUserCommands(session, extractToken(session), message.getPayload());
        if (!userSessionState.isValid()) {
            terminateSession(session, userSessionState.getErrorMessage(), userSessionState.getCloseStatus());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        UserSessionState userSessionState = commandManager.handleNewUserConnection(session, extractToken(session));
        if (!userSessionState.isValid()) {
            terminateSession(session, userSessionState.getErrorMessage(), userSessionState.getCloseStatus());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        commandManager.handleLeavingUser(extractToken(session));
    }

    @SneakyThrows
    private void terminateSession(WebSocketSession session, String message, CloseStatus closeStatus) {
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(commandConverter.buildErrorResponse(Collections.singletonList(message))));
            session.close(closeStatus);
        }
    }

    private String extractToken(WebSocketSession session) {
        List<String> tokenHeaders = session.getHandshakeHeaders().get(RequestXMLTag.TOKEN.getValue());

        if (!CollectionUtils.isEmpty(tokenHeaders)) {
            return tokenHeaders.get(0);
        }

        Map<String, List<String>> requestParams = ((StandardWebSocketSession) session).getNativeSession().getRequestParameterMap();
        if (!CollectionUtils.isEmpty(requestParams) && requestParams.containsKey(RequestXMLTag.TOKEN.getValue())) {
            tokenHeaders = requestParams.get(RequestXMLTag.TOKEN.getValue());
        }

        if (!CollectionUtils.isEmpty(tokenHeaders)) {
            return tokenHeaders.get(0);
        }

        return null;
    }
}
