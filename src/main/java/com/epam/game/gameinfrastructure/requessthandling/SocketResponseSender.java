package com.epam.game.gameinfrastructure.requessthandling;

import com.epam.game.domain.User;
import com.epam.game.gameinfrastructure.parser.XmlResponseGenerator;
import com.epam.game.gamemodel.model.GameInstance;
import com.epam.game.gamemodel.model.Vertex;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.*;

/**
 * Singleton keeps the map of games and sockets of clients.
 * 
 * @author Andrey_Eremeev
 * 
 */

public class SocketResponseSender implements Observer {

    private Map<GameInstance, Set<PeerController>> clientsPeers;

    private Map<User, Set<String>> clientsMessages;

    private XmlResponseGenerator xmlResponseGenerator;

    private Transformer transformer;

    private SocketReaper connectionUpdater;

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

    public void addMessageToClient(User user, String message) {
        if (!clientsMessages.containsKey(user)) {
            clientsMessages.put(user, new HashSet<String>());
        }
        clientsMessages.get(user).add(message);
    }

    public void sendMessage(Socket socket, String message) throws IOException {
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
        if (message != null) {
            osw.write(message);
        }
        osw.close();
        os.flush();
        os.close();
        socket.close();
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
            Document doc = xmlResponseGenerator.generate(vertices);
            Node response = doc.getLastChild();
            Node errors = doc.createElement("errors");
            Set<PeerController> pcs = clientsPeers.get(game);
            if (pcs == null) {
                return;
            }
            for (PeerController pc : pcs) {
                Set<String> errMessages = clientsMessages.get(pc.getUser());
                if (errMessages != null) {
                    for (String errMessage : errMessages) {
                        Node error = doc.createElement("error");
                        error.appendChild(doc.createTextNode(errMessage));
                        errors.appendChild(error);
                    }
                    clientsMessages.get(pc.getUser()).clear();
                }
            }
            response.appendChild(errors);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            DOMSource source = new DOMSource(doc);
            try {
                transformer.transform(source, result);
                sendMessageToAll(game, writer.toString());
            } catch (TransformerException e) {
                // TODO Auto-generated catch block
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
                Socket client = pc.getSocket();
                try{
                    sendMessage(client, message);
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
        xmlResponseGenerator = new XmlResponseGenerator();
        TransformerFactory factory = TransformerFactory.newInstance();
        new Thread(new SocketReaper(clientsPeers, 3000)).start();
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
