package com.epam.game.gamemodel.model.action;

import com.epam.game.domain.User;
import com.epam.game.exceptions.IllegalCommandException;
import com.epam.game.gameinfrastructure.requessthandling.PeerController;
import com.epam.game.gamemodel.model.GameInstance;

/**
 * Abstract class for actions. Implementations should get all needed 
 * (for that action) objects as arguments. Method doAction() contains 
 * all action logic.
 * 
 * @author Evgeny_Tetuhin
 *
 */
public abstract class Action {
	
    protected GameInstance game;
    protected User player;
    protected PeerController pc;
    
	/**
	 * Implements all logic of the action.
	 * @throws IllegalCommandException 
	 */
	abstract public void doAction() throws IllegalCommandException;

    public PeerController getPeerController(){
        return pc;
    }
}
