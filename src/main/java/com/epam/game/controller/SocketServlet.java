package com.epam.game.controller;

import com.epam.game.dao.GameDAO;
import com.epam.game.gameinfrastructure.requessthandling.SocketListnerThread;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Servlet implementation class SocketServlet
 */
@Component
@RequiredArgsConstructor
public class SocketServlet {

    private static final long serialVersionUID = 1L;
    private SocketListnerThread listenerThread;

    private final GameDAO gameDAO;

    @PostConstruct
    public void init()  {
        long clientTimeout = gameDAO.getSettings().getClientTimeoutMs();
        listenerThread = new SocketListnerThread(clientTimeout);
        new Thread(listenerThread).start();
    }

    @PreDestroy
    public void destroy() {
        listenerThread.stopAndDie();
    }
}
