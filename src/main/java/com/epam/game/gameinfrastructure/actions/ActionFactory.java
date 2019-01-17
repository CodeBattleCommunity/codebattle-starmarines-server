package com.epam.game.gameinfrastructure.actions;

import com.epam.game.domain.User;
import com.epam.game.exceptions.GameIsFinishedException;
import com.epam.game.exceptions.NoSuchGameException;
import com.epam.game.gameinfrastructure.parser.ClientsDataObject;
import com.epam.game.gameinfrastructure.parser.RequestXMLTag;
import com.epam.game.gameinfrastructure.requessthandling.PeerController;
import com.epam.game.gamemodel.model.GameInstance;
import com.epam.game.gamemodel.model.Model;
import com.epam.game.gamemodel.model.action.Action;
import com.epam.game.gamemodel.model.action.ActionsType;
import com.epam.game.gamemodel.model.action.impl.LoginAction;
import com.epam.game.gamemodel.model.action.impl.MoveAction;

import java.net.Socket;
import java.util.List;

/**
 * Factory produces Actions
 * 
 * @author Andrey_Eremeev
 * 
 */
public class ActionFactory {

    public static Action getInstance(ClientsDataObject dataObject, Socket socket)
            throws NoSuchGameException, GameIsFinishedException {
        String token = dataObject.getParams().get(RequestXMLTag.TOKEN);
        GameInstance game = Model.getInstance().getByToken(token);
        
        if (game == null) {
            throw new NoSuchGameException(
                    "Wrong user id. No game for current user.");
        }
        if (game.isFinished()){
            throw new GameIsFinishedException(generateWinnersMessage(game.getWinners()));
        }

        try {
            PeerController pc = new PeerController(game.getUserByToken(token),
                    socket);
            if (ActionsType.LOGIN == dataObject.getType()) {
                return new LoginAction(game, pc);
            } else if (ActionsType.MOVE == dataObject.getType()) {
                String fromStr = dataObject.getParams().get(RequestXMLTag.FROM);
                String toStr = dataObject.getParams().get(RequestXMLTag.TO);
                String unitscountStr = dataObject.getParams().get(
                        RequestXMLTag.UNITSCOUNT);
                long from = Long.parseLong(fromStr);
                long to = Long.parseLong(toStr);
                int unitscount = Integer.parseInt(unitscountStr);
                User user = game.getUserByToken(token);
                return new MoveAction(game, from, to, unitscount, user, pc);
            }
        } catch (NumberFormatException e) {
            //e.printStackTrace();
        }

        return null;
    }
    
    // should be somewhere in utility classes.
    private static String generateWinnersMessage(List<User> winners){
        StringBuilder msg =  new StringBuilder("Game is finished. ");
        if(winners.size() > 0) {
            for(int i = 0; i < winners.size(); i++){
                msg.append(winners.get(i).getUserName());
                switch(winners.size() - i){
                case 1:{
                    msg.append(" ");
                    break;
                }
                case 2: {
                    msg.append(" and ");
                    break;
                }
                case 3: {
                    msg.append(", ");
                    break;
                }
                }
            }
        }
        msg.append("won the game!");
        return msg.toString();
    }
}
