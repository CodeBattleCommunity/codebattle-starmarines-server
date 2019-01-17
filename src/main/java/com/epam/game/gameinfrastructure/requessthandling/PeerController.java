package com.epam.game.gameinfrastructure.requessthandling;

import com.epam.game.domain.User;

import java.net.Socket;

/**
 * 
 * @author Andrey_Eremeev
 *
 */
public class PeerController {

    private Socket socket;

    private User user;

    public PeerController(User user, Socket socket) {
        this.user = user;
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public User getUser() {
        return user;
    }
}
