package com.epam.game.controller.validators;

import com.epam.game.controller.forms.SignUpForm;
import com.epam.game.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for SignUp form.
 * 
 * @author Roman_Spiridonov
 * 
 */
@Service
public class SignUpValidator implements Validator {

    private static final int MAX_USERNAME_LENGTH = 30;
    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MAX_PASSWORD_LENGTH = 30;
    private static final int MIN_PASSWORD_LENGTH = 5;
    private static final int MIN_EMAIL_LENGTH = 5;
    private static final int MAX_EMAIL_LENGTH = 200;    
    @Autowired
    private UserDAO userDAO;

    @Override
    public boolean supports(Class<?> arg0) {
        return SignUpForm.class.equals(arg0);
    }

    @Override
    public void validate(Object arg0, Errors err) {
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "name",
                "name.empty.signUpForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "password",
                "password.empty.signUpForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "repassword",
                "repassword.empty.signUpForm.repassword");
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "email",
                "email.empty.signUpForm.email");
        SignUpForm signUpForm = (SignUpForm) arg0;
        if (err.getFieldErrorCount("name") == 0) {
            if (!CVUtils.inRange(signUpForm.getName().length(), MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH)) {
                err.rejectValue("name", "error.signUp.name.outOfBounds");
            }
        }
        if (err.getFieldErrorCount("email") == 0) {
            if ( !CVUtils.isEmailValid(signUpForm.getEmail()) ) {
                err.rejectValue("email", "email.invalid.signUpForm.email");
            }
        }
        if (err.getFieldErrorCount("repassword") == 0) {
            if (signUpForm.getRepassword().compareTo(signUpForm.getPassword()) != 0) {
                err.rejectValue("repassword", "dont.match.signUpForm.repassword");
            }
        }
        if (err.getFieldErrorCount("password") == 0) {
            if (!CVUtils.inRange(signUpForm.getPassword().length(), MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH)) {
                err.rejectValue("password", "error.signUp.password.outOfBounds");
            } else {
                if (signUpForm.getPassword().compareTo(
                        signUpForm.getRepassword()) != 0) {
                    err.rejectValue("password",
                            "dont.match.signUpForm.password");
                }
            }
        }
        if (!err.hasErrors()) {
            if (userDAO.getUserWith(signUpForm.getName()) != null) {
                err.rejectValue("name", "existing.login.signUpForm.login");
            }
        }
    }
    
    
}
