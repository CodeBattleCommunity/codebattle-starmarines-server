package com.epam.game.controller;

import com.epam.game.authorization.TokenGenerator;
import com.epam.game.constants.AttributesEnum;
import com.epam.game.constants.ViewsEnum;
import com.epam.game.controller.forms.SignUpForm;
import com.epam.game.controller.validators.SignUpValidator;
import com.epam.game.dao.GameDAO;
import com.epam.game.dao.UserDAO;
import com.epam.game.domain.Authority;
import com.epam.game.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for working with Sign Up page
 * 
 * @author Roman_Spiridonov
 * 
 */
@Controller
@SessionAttributes(value = AttributesEnum.CLIENT)
public class SignUpController {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private GameDAO gameDAO;
    @Autowired
    private SignUpValidator signUpValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/" + ViewsEnum.SIGN_UP + ViewsEnum.EXTENSION, method = RequestMethod.GET)
    public String showSignUpForm(ModelMap model) {
        SignUpForm signUpForm = new SignUpForm();
        model.addAttribute(AttributesEnum.SIGN_UP_FORM, signUpForm);
        model.addAttribute(AttributesEnum.REGISTRATION_IS_OPEN, gameDAO.getSettings().isRegistrationOpened());
        return ViewsEnum.SIGN_UP;
    }

    @RequestMapping(value = "/" + ViewsEnum.SIGN_UP + ViewsEnum.EXTENSION, method = RequestMethod.POST)
    public String onSubmit(@ModelAttribute SignUpForm signUpForm,
                           BindingResult result, ModelMap model) {
        if (!gameDAO.getSettings().isRegistrationOpened()) {
            return ViewsEnum.SIGN_UP;
        }
        this.signUpValidator.validate(signUpForm, result);
        if (result.hasErrors()) {
            return ViewsEnum.SIGN_UP;
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(Authority.ROLE_USER);
        User userToDB = new User();
        userToDB.setAuthorities(authorities);
        userToDB.setLogin(signUpForm.getName());
        userToDB.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        userToDB.setToken(TokenGenerator.generate());
        userToDB.setUserName(signUpForm.getName());
        userToDB.setEmail(signUpForm.getEmail());
        userToDB.setPhone(signUpForm.getPhone());
        userDAO.addUser(userToDB);

        UserDetails userDetails = userDetailsService.loadUserByUsername(signUpForm.getName());
        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(userDetails, signUpForm.getPassword(), userDetails.getAuthorities());

        authenticationManager.authenticate(authentication);

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return "redirect:" + ViewsEnum.DOCUMENTATION + ViewsEnum.EXTENSION;
    }
}
