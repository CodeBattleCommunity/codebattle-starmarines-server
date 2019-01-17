package com.epam.game.gameinfrastructure.requessthandling;

import com.epam.game.constants.Settings;
import com.epam.game.exceptions.GameIsFinishedException;
import com.epam.game.exceptions.NoSuchGameException;
import com.epam.game.exceptions.RequestReadingException;
import com.epam.game.gameinfrastructure.actions.ActionFactory;
import com.epam.game.gameinfrastructure.parser.ClientRequestParser;
import com.epam.game.gameinfrastructure.parser.ClientsDataObject;
import com.epam.game.gameinfrastructure.parser.RequestXMLTag;
import com.epam.game.gamemodel.model.GameInstance;
import com.epam.game.gamemodel.model.Model;
import com.epam.game.gamemodel.model.action.Action;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

/**
 * Thread for handling of clients request.
 * 
 * @author Andrey_Eremeev
 * 
 */
public class ClientRequestHandlerThread implements Runnable {

    private static final Logger log = Logger.getLogger(ClientRequestHandlerThread.class.getName());

    private Socket socket;
    private InputStream inputSocketStream;
    private ClientRequestParser parser;
    private byte[] buffer = new byte[1000];
    private long readTimeoutMs;

    /**
     * 
     * @param socket
     *            - socket of current client
     * @param parser
     *            - Parser of request
     */
    public ClientRequestHandlerThread(Socket socket, ClientRequestParser parser, long readTimeoutMs) {
        this.socket = socket;
        try {
            this.inputSocketStream = socket.getInputStream();
            this.parser = parser;
            this.readTimeoutMs = readTimeoutMs;
            Thread.sleep(100);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void run() { // needs to be rewritten.
        try {
            SocketResponseSender srs = SocketResponseSender.getInstance();
            List<ClientsDataObject> dataObjectList;
            String request = readInputMessage();
            GameInstance gameToCommand = null;
            PeerController pc = null;
            try{
                dataObjectList = parser.parse(request);
                for (ClientsDataObject dataObject : dataObjectList) {
                    Action action = ActionFactory.getInstance(dataObject, socket);
                    action.doAction();
                    if(gameToCommand == null || pc == null){
                        String token = dataObject.getParams().get(RequestXMLTag.TOKEN);
                        gameToCommand = Model.getInstance().getByToken(token);
                        pc = action.getPeerController();
                    }
                }
                if(gameToCommand != null && pc != null) {
                    srs.addSocketToGame(gameToCommand, pc);
                }
            } catch (NullPointerException e) { //omfg
                sendDelayedMessage("Incorrect request.");
            }
        } catch (NoSuchGameException e) {
            sendDelayedMessage("User has not joined any game.");
        } catch (GameIsFinishedException e) {
            sendDelayedMessage(e.getMessage(), "gameover");
        } catch (RequestReadingException e){
            sendDelayedMessage("Time limit exceeded while receiving the message.");
        } catch (Exception e) {
            sendDelayedMessage(xmlEscape(e.getMessage()));
        }
    }

    /**
     * Method reads InputStream to String
     * 
     * @return
     * @throws IOException
     * @throws RequestReadingException
     */
    private String readInputMessage() throws IOException, RequestReadingException {
        StringBuilder result = new StringBuilder();
        int readed = 1;
        boolean lastTagReceived = false;
        int i = 0;
        long startTime = System.currentTimeMillis();
        long readingTime = 0;
        while(!lastTagReceived && readingTime < readTimeoutMs){  // if "/request" is readed or reading take too long, quit and return what we've got.
            if(inputSocketStream.available() > 0){                          // there is data in the stream, read it and check whether it completes request.
                readed = inputSocketStream.read(buffer);
                result.append(new String(buffer), 0, readed);
                lastTagReceived = (result.lastIndexOf("/" + RequestXMLTag.REQUEST.getValue()) >= 0);
            }
            readingTime = System.currentTimeMillis() - startTime;
        }
        return result.toString();
    }
    
    private String xmlEscape(String input) {
        String tmp = input;
        tmp.replaceAll("<", "");
        tmp.replaceAll(">", "");
        return tmp;
    }
    
    private void sendDelayedMessage(String msg) {
        sendDelayedMessage(msg, "");
    }
    
    
    private void sendDelayedMessage(String msg, String type) {
        try{
            Thread.sleep(Settings.ERROR_RESPONSE_DELAY);
            String typeAttr = (type.isEmpty()) ? "" : "type=\"" + type + "\"";
            SocketResponseSender.getInstance().sendMessage(socket,
                String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><planets /><errors><error %s>%s</error></errors></response>", typeAttr, msg));
            socket.close();
        } catch(Exception e) {
            System.out.println("Bad socked out of pool!");
        }
    }
}