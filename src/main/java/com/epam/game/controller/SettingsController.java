/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.game.controller;

import com.epam.game.constants.AttributesEnum;
import com.epam.game.constants.RequestType;
import com.epam.game.constants.ViewsEnum;
import com.epam.game.json.JSONConverter;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sergey_Fedorov
 */
@Controller
@RequiredArgsConstructor
public class SettingsController {

    private final JSONConverter converter;

    @RequestMapping(value = "/" + ViewsEnum.SETTINGS + ViewsEnum.EXTENSION, method = RequestMethod.GET)
    public void getParameter(@RequestParam( value = AttributesEnum.AJAX_REQUEST_TYPE ) String requestType, HttpServletResponse response ) {
        RequestType type = RequestType.valueOf( requestType );

        JSONObject object = null;
        
        switch ( type ) {
            case TURN_DURATION:
                object = converter.generateTurnDurationMessage();
                break;
            case NEXT_GAME_TIME:
                object = converter.generateNextGameTimeMessage();
                break;
        }
        // Addition of JSON object to response.
        if ( object != null ) {
            try {
                PrintWriter writer = response.getWriter();
                writer.print( object.toJSONString() );
                writer.flush();
                writer.close();
            } catch ( IOException ex ) {
                Logger.getLogger( ViewDataController.class.getName() ).log(
                        Level.SEVERE, null, ex );
            }
        }
    }


}
