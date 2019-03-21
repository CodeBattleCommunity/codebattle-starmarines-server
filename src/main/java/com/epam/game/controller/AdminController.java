package com.epam.game.controller;

import com.epam.game.constants.AttributesEnum;
import com.epam.game.constants.ViewsEnum;
import com.epam.game.dao.GameDAO;
import com.epam.game.dao.UserDAO;
import com.epam.game.domain.FlattenSettings;
import com.epam.game.domain.User;
import com.epam.game.gamemodel.model.GameInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author Igor_Petrov@epam.com
 * Created at 2/20/2019
 */
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final GameDAO gameDAO;

    private final UserDAO userDAO;

    @Autowired
    private com.epam.game.gamemodel.model.Model model;

    @GetMapping("/" + ViewsEnum.ADMIN + ViewsEnum.EXTENSION)
    public String getGameSettings(Model model) {
        populateSettingsModel(model);
        populateUsers(model);
        return ViewsEnum.ADMIN;
    }

    @PostMapping("/" + ViewsEnum.ADMIN)
    public String applySettings(@ModelAttribute FlattenSettings settings, Model model) {
        gameDAO.applySettings(settings);
        populateSettingsModel(model);
        populateUsers(model);
        return ViewsEnum.ADMIN;
    }

    @PostMapping("/" + ViewsEnum.ADMIN + "/default")
    public String restoreDefaults(Model model) {
        gameDAO.restoreDefaultSettings();
        populateSettingsModel(model);
        populateUsers(model);
        return "redirect:/" + ViewsEnum.ADMIN + ViewsEnum.EXTENSION;
    }

    @PostMapping("/" + ViewsEnum.ADMIN + "/user/cleanup")
    public String cleanupUser(@ModelAttribute("userId") Long userId) {
        User player = userDAO.getUserWith(userId);
        GameInstance playerGame = model.getByToken(player.getToken());
        if (playerGame != null) {
            playerGame.deleteUser(player.getId());
        }
        return "redirect:/" + ViewsEnum.ADMIN + ViewsEnum.EXTENSION;
    }

    private void populateSettingsModel(Model model) {
        model.addAttribute(AttributesEnum.SETTINGS, gameDAO.getFlattenSettings());
    }

    private void populateUsers(Model model) {
        List<User> realPlayers = userDAO.getRealPlayers();
        model.addAttribute(AttributesEnum.USERS, realPlayers);
    }
}
