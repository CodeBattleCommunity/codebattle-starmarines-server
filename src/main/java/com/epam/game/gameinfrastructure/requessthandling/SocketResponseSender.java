package com.epam.game.gameinfrastructure.requessthandling;

import com.epam.game.domain.User;
import com.epam.game.gamemodel.model.GameInstance;
import com.epam.game.gamemodel.model.Vertex;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Singleton keeps the map of games and sockets of clients.
 *
 * @author Andrey_Eremeev
 *
 */

public class SocketResponseSender implements Observer {

    private Map<User, Set<String>> clientsMessages;

    private CommandConverter commandConverter;

    private static volatile SocketResponseSender srs;

    public static SocketResponseSender getInstance() {
        if (srs == null) {
            synchronized (SocketResponseSender.class) {
                if (srs == null) {
                    srs = new SocketResponseSender();
                }
            }
        }
        return srs;
    }

    /**
     * Binding socket and game
     *
     * @param id
     *            - games id
     * @param socket
     */
    public void addSocketToGame(GameInstance game, PeerController pc) {
        synchronized (game) {
            if (!game.getClientsPeers().containsKey(game)) {
                game.getClientsPeers().put(game, ConcurrentHashMap.newKeySet());
            }
            game.getClientsPeers().get(game).add(pc);
        }
    }

    public void addMessageToClient(User user, String message) {
        if (!clientsMessages.containsKey(user)) {
            clientsMessages.put(user, new HashSet<String>());
        }
        clientsMessages.get(user).add(message);
    }

    public void sendMessage(WebSocketSession session, String message) throws IOException {
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }

    public boolean isConnected(GameInstance game, long playerId) {
        Set<PeerController> peerControllers = game.getClientsPeers().get(game);
        if (peerControllers != null) {
            for (PeerController controller : peerControllers) {
                if (playerId == controller.getUser().getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Map && o instanceof GameInstance) {
            @SuppressWarnings("unchecked")
            Map<Long, Vertex> vertices = (Map<Long, Vertex>) arg;
            GameInstance game = (GameInstance) o;

            Set<PeerController> pcs = game.getClientsPeers().get(game);

            if (pcs == null) {
                return;
            }

            List<String> errors = new ArrayList<>();
            pcs.forEach(pc -> {
                Set<String> pcErrors = clientsMessages.get(pc.getUser());
                if (pcErrors != null) {
                    errors.addAll(pcErrors);
                    clientsMessages.get(pc.getUser()).clear();
                }
            });

            String response = commandConverter.buildResponse(vertices, errors);

            try {
                sendMessageToAll(game, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param id
     *            - games id
     * @param message
     * @throws IOException
     */
    private void sendMessageToAll(GameInstance game, String message) {
        Set<PeerController> peerControllers;
        synchronized (game) {
            peerControllers = new HashSet<>(game.getClientsPeers().get(game));
            if (peerControllers != null) {
                for (PeerController pc : peerControllers) {
                    WebSocketSession clientSession = pc.getSocket();
                    try{
                        sendMessage(clientSession, message);
                    } catch(IOException e) {
                        game.getClientsPeers().get(game).remove(pc);
                    }
                }
            }
            game.getClientsPeers().put(game, ConcurrentHashMap.newKeySet());
        }
    }

    private SocketResponseSender() {
        clientsMessages = new HashMap<User, Set<String>>();
        commandConverter = new CommandConverter();
    }
}
