package com.epam.game.controller;

import com.epam.game.constants.AttributesEnum;
import com.epam.game.constants.ViewsEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for working with information page.
 * 
 * @author Roman_Spiridonov
 * 
 */
@Controller
@SessionAttributes(value = AttributesEnum.CLIENT)
public class NoCookieController {

    @RequestMapping(value = "/" + ViewsEnum.NO_COOKIE + ViewsEnum.EXTENSION, method = RequestMethod.GET)
    public String showInformation(HttpServletRequest request, HttpServletResponse response) {
        if(request.isRequestedSessionIdFromCookie()){
            try {
                response.sendRedirect(request.getContextPath() + "/");
            } catch ( IOException ex ) {
                return ViewsEnum.LOGIN;
            }
        }
        return ViewsEnum.NO_COOKIE;
    }
}
