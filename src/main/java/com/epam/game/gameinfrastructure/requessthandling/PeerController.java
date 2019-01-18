package com.epam.game.gameinfrastructure.requessthandling;

import com.epam.game.domain.User;
import org.springframework.web.socket.WebSocketSession;

/**
 * 
 * @author Andrey_Eremeev
 *
 */
public class PeerController {

    private WebSocketSession socket;

    private User user;

    public PeerController(User user, WebSocketSession socket) {
        this.user = user;
        this.socket = socket;
    }

    public WebSocketSession getSocket() {
        return socket;
    }

    public User getUser() {
        return user;
    }
}
