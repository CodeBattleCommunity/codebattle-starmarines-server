package com.epam.game.controller;

import com.epam.game.constants.AttributesEnum;
import com.epam.game.constants.ViewsEnum;
import com.epam.game.dao.GameDAO;
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
        model.addAttribute("docInfo", gameDAO.getSettings().getDocInfo());
        return ViewsEnum.INFO_PAGE;
    }
}
