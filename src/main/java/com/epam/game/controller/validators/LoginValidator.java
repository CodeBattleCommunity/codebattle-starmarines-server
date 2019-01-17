package com.epam.game.controller.validators;

import com.epam.game.controller.forms.LoginForm;
import com.epam.game.dao.UserDAO;
import com.epam.game.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class LoginValidator implements Validator {

    private Logger log = Logger.getLogger(LoginValidator.class.getName());

    @Autowired
    UserDAO userDAO;

    @Override
    public boolean supports(Class<?> arg0) {
        return LoginForm.class.equals(arg0);
    }

    @Override
    public void validate(Object arg0, Errors err) {
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "userName",
                "userName.empty.loginForm.userName");
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "password",
                "password.empty.signUpForm.password");
        if(!err.hasErrors()){
            LoginForm loginForm = (LoginForm) arg0;
            try {
                User user = this.userDAO.getUserWith(loginForm.getUserName());
                if (user == null) {
                    err.rejectValue("userName", "userName.error.loginForm.userName");
                } else if ( !loginForm.getPassword().equals(user.getPassword()) ){
                    err.rejectValue("password", "password.error.loginForm.password");
                }
            } catch (Exception e) {
                err.rejectValue("userName", "userName.error.loginForm.systemError");
                log.log(Level.SEVERE, "Some user cannot login, cause: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
