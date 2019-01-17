package com.epam.game.controller;

import com.epam.game.constants.AttributesEnum;
import com.epam.game.constants.ViewsEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(value = AttributesEnum.CLIENT)
public class HistoryController {

    @RequestMapping(value = "/" + ViewsEnum.HISTORY + ViewsEnum.EXTENSION, method = RequestMethod.GET)
    public String showHistory() {
        return ViewsEnum.HISTORY;
    }
}
