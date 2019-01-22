package com.epam.game.gameinfrastructure.requessthandling;

import com.epam.game.domain.User;
import com.epam.game.gamemodel.model.GameInstance;
import com.epam.game.gamemodel.model.Vertex;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

/**
 * Singleton keeps the map of games and sockets of clients.
 * 
 * @author Andrey_Eremeev
 * 
 */

public class SocketResponseSender implements Observer {

    private final Map<GameInstance, Set<PeerController>> clientsPeers;

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
        synchronized (clientsPeers) {
            if (!clientsPeers.containsKey(game)) {
                clientsPeers.put(game, new HashSet<PeerController>());
            }
            synchronized (game) {
                clientsPeers.get(game).add(pc);
            }
        }
    }

    public void removeSocketFromGame(GameInstance game, PeerController pc) {
        synchronized (clientsPeers) {
            if (!clientsPeers.containsKey(game)) {
                return;
            }
            synchronized (game) {
                clientsPeers.get(game).remove(pc);
            }
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
        Set<PeerController> peerControllers = clientsPeers.get(game);
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

            Set<PeerController> pcs = clientsPeers.get(game);

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
        synchronized (clientsPeers) {
            peerControllers = new HashSet<PeerController>(clientsPeers.get(game));
        }
        if (peerControllers != null) {
            for (PeerController pc : peerControllers) {
                WebSocketSession clientSession = pc.getSocket();
                try{
                    sendMessage(clientSession, message);
                } catch(IOException e) {
                    clientsPeers.get(game).remove(pc);
                }
            }
        }
        clientsPeers.put(game, new HashSet<PeerController>() );
    }

    private SocketResponseSender() {
        clientsPeers = new HashMap<GameInstance, Set<PeerController>>();
        clientsMessages = new HashMap<User, Set<String>>();
        commandConverter = new CommandConverter();
//        new Thread(new SocketReaper(clientsPeers, 3000)).start();
    }
}
