package com.epam.game.gamemodel.model.action.impl;

import com.epam.game.gameinfrastructure.requessthandling.PeerController;
import com.epam.game.gameinfrastructure.requessthandling.SocketResponseSender;
import com.epam.game.gamemodel.model.GameInstance;
import com.epam.game.gamemodel.model.action.Action;


/**
 * Action object for login of bot. Not used by now.
 * @author Andrey_Eremeev
 *
 */

public class LoginAction extends Action{
    
    private GameInstance game;
    
    private PeerController pc;
    
    public LoginAction(GameInstance game, PeerController pc) {
        this.game = game;
        this.pc = pc;
    }

    @Override
    public void doAction() {
        SocketResponseSender srs = SocketResponseSender.getInstance();
        srs.addSocketToGame(game, pc);
    }
}
