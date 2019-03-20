package com.epam.game.controller;

import com.epam.game.constants.AttributesEnum;
import com.epam.game.constants.ViewsEnum;
import com.epam.game.controller.forms.ProfileForm;
import com.epam.game.controller.validators.ProfileValidator;
import com.epam.game.dao.GameDAO;
import com.epam.game.dao.UserDAO;
import com.epam.game.domain.User;
import com.epam.game.gamemodel.model.GameInstance;
import com.epam.game.gamemodel.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Controller for working with profile page.
 * 
 * @author Roman_Spiridonov
 * 
 */
@Controller
@SessionAttributes(value = AttributesEnum.CLIENT)
public class ProfileController {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private GameDAO gameDAO;
    @Autowired
    private ProfileValidator profileValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private Model gameModel;

    @RequestMapping(value = "/" + ViewsEnum.PROFILE + ViewsEnum.EXTENSION, method = RequestMethod.GET)
    public String showProfileForm(@AuthenticationPrincipal User user, ModelMap model) {
        ProfileForm profileForm = (ProfileForm)model.get(AttributesEnum.PROFILE_FORM);
        if(profileForm == null){
            profileForm = new ProfileForm();
            profileForm.setUserName(user.getUserName());
            profileForm.setEmail(user.getEmail());
            profileForm.setPhone(user.getPhone());
        }
        model.addAttribute(AttributesEnum.PROFILE_FORM, profileForm);
        model.addAttribute(AttributesEnum.TOURNAMENTS_NUMBER, gameDAO.getNumberOfPlayedGamesFor(user).toString());
        model.addAttribute(AttributesEnum.TOURNAMENTS_WINS, gameDAO.getNumberOfWonGamesFor(user).toString());
        model.addAttribute(AttributesEnum.TOKEN, user.getToken());
        return ViewsEnum.PROFILE;
    }

    @RequestMapping(value = "/" + ViewsEnum.PROFILE + ViewsEnum.EXTENSION, method = RequestMethod.POST)
    public String saveProfileForm(@AuthenticationPrincipal User client, ModelMap model,
                                  @ModelAttribute ProfileForm profileForm, BindingResult result) {
        this.profileValidator.validate(profileForm, result);
        User user = userDAO.getUserWith(client.getId());
        if(!profileForm.getNewPassword().isEmpty() || !profileForm.getOldPassword().isEmpty()){
            if(!passwordEncoder.matches(profileForm.getOldPassword(),user.getPassword())) {
                result.rejectValue("oldPassword", "oldPassword.incorrect.profileForm.oldPassword");
            } else {
                user.setPassword(passwordEncoder.encode(profileForm.getNewPassword()));
            }
        }
        if (result.hasErrors()) {
            return showProfileForm(client, model);
        }
        user.setUserName(profileForm.getUserName());
        user.setLogin(profileForm.getUserName());
        user.setPhone(profileForm.getPhone());
        user.setEmail(profileForm.getEmail());
        userDAO.updateUser(user);
        model.addAttribute(AttributesEnum.MESSAGE, "label.profileForm.success");
        return showProfileForm(client, model);
    }

    @RequestMapping(value = "/" + ViewsEnum.GENERATE_TOKEN
            + ViewsEnum.EXTENSION, method = RequestMethod.GET)
    public String generateNewToken(@AuthenticationPrincipal User client, ModelMap model) {
        GameInstance game = gameModel.getByUser(client.getId());
        if (game == null) {
            User user = userDAO.getUserWith(client.getId());
            String token = userDAO.updateToken(user.getId());
            client.setToken(token);
            return "redirect:" + ViewsEnum.PROFILE + ViewsEnum.EXTENSION;
        } else {
            model.addAttribute(AttributesEnum.ERROR_TOKEN,
                    "errGenerateToken.profile.token");
            return showProfileForm(client, model);
        }
    }
}