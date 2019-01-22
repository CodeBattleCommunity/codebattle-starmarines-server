package com.epam.game.gamemodel.model.action.impl;

import com.epam.game.domain.User;
import com.epam.game.exceptions.IllegalCommandException;
import com.epam.game.gameinfrastructure.commands.client.ClientAction;
import com.epam.game.gameinfrastructure.requessthandling.PeerController;
import com.epam.game.gameinfrastructure.requessthandling.SocketResponseSender;
import com.epam.game.gamemodel.model.GameInstance;
import com.epam.game.gamemodel.model.action.Action;

import java.util.logging.Logger;

/**
 * Action object for moving units from vertex to vertex.
 * Note: constructor requires {@link GameInstance} and few other args.
 * 
 * @author Evgeny_Tetuhin
 *
 */
public class MoveAction extends Action {

	
	private Long from;
	private Long to;
	private int unitsCount;
	
	private Logger log = Logger.getLogger(MoveAction.class.getName());
	
	public MoveAction(GameInstance game, ClientAction action, User player, PeerController pc){
		this.game = game;
		this.from = action.getFrom();
		this.to = action.getTo();
		this.unitsCount = action.getUnitsCount();
		this.player = player;
		this.pc = pc;
	}
	
	@Override
	public void doAction() {
        if(game == null) {
            return;
        }
        if(!game.isStarted()){
            return;
        }
        if(game.isFinished()){
            return;
        }
        SocketResponseSender srs = SocketResponseSender.getInstance();
        try{
            game.move(from, to, unitsCount, player);
        } catch (IllegalCommandException e) {
            srs.addMessageToClient(player, e.getMessage());
        }
	}

}
