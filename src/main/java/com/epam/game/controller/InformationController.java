package com.epam.game.controller;

import com.epam.game.constants.AttributesEnum;
import com.epam.game.constants.ViewsEnum;
import com.epam.game.dao.GameDAO;
import com.epam.game.domain.GameSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Controller for working with information page.
 * 
 * @author Roman_Spiridonov
 * 
 */
@Controller
@SessionAttributes(value = AttributesEnum.CLIENT)
@RequiredArgsConstructor
public class InformationController {

    private final GameDAO gameDAO;

    @RequestMapping(value = "/" + ViewsEnum.INFO_PAGE + ViewsEnum.EXTENSION, method = RequestMethod.GET)
    public String showInformation(Model model) {
        GameSettings settings = gameDAO.getSettings();
        model.addAttribute("docInfo", settings.getDocInfo());
        model.addAttribute("disasterSettings", settings.getDisasterSettings());
        model.addAttribute("portalSettings", settings.getPortalSettings());
        model.addAttribute("commonSettings", settings);
        return ViewsEnum.INFO_PAGE;
    }
}
