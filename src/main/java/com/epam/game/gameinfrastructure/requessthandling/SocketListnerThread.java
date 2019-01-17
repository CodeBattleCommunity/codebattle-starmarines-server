package com.epam.game.gameinfrastructure.requessthandling;

import com.epam.game.constants.Settings;
import com.epam.game.gameinfrastructure.parser.ClientRequestParser;
import com.epam.game.gameinfrastructure.parser.SAXParserWrapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Listener of socket. All requests from clients (bot) are being put to Thread
 * Pool.
 * 
 * @author Andrey_Eremeev
 * 
 */

public class SocketListnerThread implements Runnable {


    private ServerSocket serverSocket;

    private ClientRequestParser parser;

    private long readTimeoutMs;

    protected ExecutorService threadPool = Executors.newFixedThreadPool(10);
    
    private boolean alive = true;

    public SocketListnerThread(long readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(Settings.PORT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while (alive) {
            try {
                Socket socket = serverSocket.accept();
                parser = new SAXParserWrapper();
                this.threadPool.execute(new ClientRequestHandlerThread(socket,
                        parser, readTimeoutMs));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void stopAndDie() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        alive = false;
    }
}
