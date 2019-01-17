package com.epam.game.controller;

import com.epam.game.constants.AttributesEnum;
import com.epam.game.constants.ViewsEnum;
import com.epam.game.controller.forms.LoginForm;
import com.epam.game.dao.GameDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for working with login page.
 *
 * @author Roman_Spiridonov
 *
 */
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final GameDAO gameDAO;

    @RequestMapping( value = "/" + ViewsEnum.LOGIN + ViewsEnum.EXTENSION, method = RequestMethod.GET )
    public String showLoginForm( ModelMap model ) {
        model.addAttribute( AttributesEnum.LOGIN_FORM, new LoginForm() );
        model.addAttribute(AttributesEnum.REGISTRATION_IS_OPEN, gameDAO.getSettings().isRegistrationOpened());
        return ViewsEnum.LOGIN;
    }

    @RequestMapping( value = "/" + ViewsEnum.LOGOUT + ViewsEnum.EXTENSION, method = RequestMethod.GET )
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "redirect:" + ViewsEnum.LOGIN + ViewsEnum.EXTENSION;
    }
}
