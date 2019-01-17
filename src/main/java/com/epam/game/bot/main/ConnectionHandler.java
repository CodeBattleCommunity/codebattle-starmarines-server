package com.epam.game.bot.main;

import com.epam.game.bot.domain.BotAction;
import com.epam.game.bot.domain.Planet;
import com.epam.game.bot.xml.ModelParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Map;

/**
 * Runnable implementation that operates connections to the game server.
 * 
 * @author Evgeny_Tetuhin
 *
 */
public class ConnectionHandler implements Runnable {

    private final int BUFFER_SIZE = 16000;
    private final long DELAY_AFTER_ERROR = 2000;
    
    private String userToken;
    private int serverPort;
    private String address;
    private boolean gameEnded;
    private String botName;

    public ConnectionHandler(String token, String address, int port, String botName){
        this.userToken = token;
        this.address = address;
        this.serverPort = port;
        this.botName = botName;
    }
    
    @Override
    public void run() {
        while(!gameEnded){
            try {
                InetAddress ipAddress = null;
                ipAddress = InetAddress.getByName(address);
                String serverResponse = "";
                Socket socket = null;
                socket = new Socket(ipAddress, serverPort);
                InputStream sin = socket.getInputStream();
                OutputStream streamToServer = socket.getOutputStream();
                DataOutputStream out = new DataOutputStream(streamToServer);
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(sin));
                String request = generateInitialRequest();
                while (!gameEnded) {
                    streamToServer.write(request.getBytes());
                    streamToServer.flush();
                    CharBuffer cb = CharBuffer.allocate(BUFFER_SIZE);
                    responseReader.read(cb);
                    cb.flip();
                    responseReader.close();
                    out.close();
                    streamToServer.close();
                    sin.close();
                    socket.close();
                    serverResponse = new String(cb.array()).trim();
                    //check for end-of-game-error should be there
                    if ("".equals(serverResponse)) {
                        request = generateInitialRequest();
                    } else {
                        request = generateRequest(serverResponse);
                    }
                    socket = new Socket(ipAddress, serverPort);
                    sin = socket.getInputStream();
                    streamToServer = socket.getOutputStream();
                    out = new DataOutputStream(streamToServer);
                    responseReader = new BufferedReader(new InputStreamReader(sin));
                }
            } catch (Exception x) {
                x.printStackTrace();
            }
            try {
                Thread.sleep(DELAY_AFTER_ERROR);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public String generateRequest(String response) {
        StringBuilder newRequest = new StringBuilder();
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        ModelParser modParser = new ModelParser();
        try {
            SAXParser parser = parserFactory.newSAXParser();
            InputStream stream = new ByteArrayInputStream(response.getBytes());
            parser.parse(stream, modParser);
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map<Long, Planet> planets = modParser.getPlanets();
        List<String> errors = modParser.getErrors();
        if(errors != null && errors.size() > 0){
            for(String error : errors){
                System.out.println(error);
            }
        }
        SimpleLogic brains = new SimpleLogic();
        List<BotAction> actions = brains.whatToDo(planets, botName);
        newRequest.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><request><token>");
        newRequest.append(userToken);
        newRequest.append("</token><actions>");
        for(BotAction a : actions){
            newRequest.append("<action><from>");
            newRequest.append(a.from);
            newRequest.append("</from><to>");
            newRequest.append(a.to);
            newRequest.append("</to><unitscount>");
            newRequest.append(a.count);
            newRequest.append("</unitscount></action>");
        }
        newRequest.append("</actions></request>");
        return newRequest.toString();
    }

    public String generateInitialRequest() {
        return String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<request><token>%1s</token><actions><login></login></actions></request>", userToken);
    }
}
