package com.epam.game.controller;

import com.epam.game.constants.AttributesEnum;
import com.epam.game.constants.RequestType;
import com.epam.game.constants.ViewsEnum;
import com.epam.game.gamemodel.model.GameInstance;
import com.epam.game.gamemodel.model.Model;
import com.epam.game.json.JSONConverter;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for data transfer to game view
 * 
 * @author Roman_Spiridonov
 * 
 */
@Controller
@SessionAttributes(value = AttributesEnum.CLIENT)
@RequiredArgsConstructor
public class ViewDataController {

    private final JSONConverter converter;

    @RequestMapping(value = "/" + ViewsEnum.VIEW_DATA + ViewsEnum.EXTENSION, method = RequestMethod.GET)
    public void getViewData(
            @RequestParam(value = AttributesEnum.GAME_ID) String id,
            @RequestParam(value = AttributesEnum.AJAX_REQUEST_TYPE) String requestType,
            HttpServletResponse response) {
        Long gameId;
        try {
            gameId = Long.parseLong(id);
        } catch (NumberFormatException err) {
            return;
        }
        RequestType type = RequestType.valueOf(requestType);
        GameInstance game = Model.getInstance().getGameById(gameId);
        if (game == null) {
            return;
        }
        // Selection of generation type.
        JSONObject object = null;
        switch (type) {
        case GAME_FIELD:
            object = converter.generateGameMapMessage(game);
            break;
        case PLAYERS_ACTIONS:
            object = converter.generatePlayersActionsMessage(
                    game);
            break;
        }
        // Addition of JSON object to response.
        if (object != null) {
            try {
                response.setCharacterEncoding( "utf-8");
                PrintWriter writer = response.getWriter();
                writer.print(object.toJSONString());
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(ViewDataController.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
    }
}
