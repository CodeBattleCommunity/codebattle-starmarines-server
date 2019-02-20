package com.epam.game.controller;

import com.epam.game.constants.AttributesEnum;
import com.epam.game.constants.ViewsEnum;
import com.epam.game.dao.GameDAO;
import com.epam.game.domain.FlattenSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Igor_Petrov@epam.com
 * Created at 2/20/2019
 */
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final GameDAO gameDAO;

    @GetMapping("/" + ViewsEnum.ADMIN + ViewsEnum.EXTENSION)
    public String getGameSettings(Model model) {
        populateSettingsModel(model);
        return ViewsEnum.ADMIN;
    }

    @PostMapping("/" + ViewsEnum.ADMIN)
    public String applySettings(@ModelAttribute FlattenSettings settings, Model model) {
        gameDAO.applySettings(settings);
        populateSettingsModel(model);
        return ViewsEnum.ADMIN;
    }

    @PostMapping("/" + ViewsEnum.ADMIN + "/default")
    public String restoreDefaults(Model model) {
        gameDAO.restoreDefaultSettings();
        populateSettingsModel(model);
        return ViewsEnum.ADMIN;
    }

    private void populateSettingsModel(Model model) {
        model.addAttribute(AttributesEnum.SETTINGS, gameDAO.getFlattenSettings());
    }
}
