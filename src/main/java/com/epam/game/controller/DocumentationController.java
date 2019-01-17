package com.epam.game.controller;

import com.epam.game.constants.AttributesEnum;
import com.epam.game.constants.ViewsEnum;
import org.springframework.stereotype.Controller;
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
public class DocumentationController {

    @RequestMapping(value = "/" + ViewsEnum.DOCUMENTATION + ViewsEnum.EXTENSION, method = RequestMethod.GET)
    public String showInformation() {
        return ViewsEnum.DOCUMENTATION;
    }
}
