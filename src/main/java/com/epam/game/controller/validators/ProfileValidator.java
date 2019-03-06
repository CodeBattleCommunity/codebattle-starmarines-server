package com.epam.game.controller.validators;

import com.epam.game.controller.forms.ProfileForm;
import com.epam.game.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class ProfileValidator implements Validator {

    private static final int MIN_PASSWORD_LENGTH = 5;
    private static final int MAX_PASSWORD_LENGTH = 30;
    private static final int MAX_EMAIL_LENGTH = 200;
    private final int MAX_USERNAME_LENGTH = 30;
    private final int MIN_USERNAME_LENGTH = 3;

    @Autowired
    private UserDAO userDAO;

    @Override
    public boolean supports(Class<?> arg0) {
        return ProfileForm.class.equals(arg0);
    }

    @Override
    public void validate(Object formObject, Errors err) {
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "userName", "userName.empty.profileForm.userName");
        ProfileForm form = (ProfileForm) formObject;
        if (err.getFieldErrorCount("userName") == 0) {
            if (!CVUtils.inRange(form.getUserName().length(), MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH)) {
                Object[] args = { MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH };
                err.rejectValue("userName", "userName.outOfRange.profileForm.userName", args, null);
            }
        }

        if (!CVUtils.isEmailValid(form.getEmail()) || form.getEmail().length() > MAX_EMAIL_LENGTH) {
            err.rejectValue("email", "email.invalid.profileForm.email");
        }
        if (!form.getNewPassword().isEmpty() || !form.getOldPassword().isEmpty()) {
            if (form.getOldPassword().isEmpty()) {
                err.reject("oldPassword", "oldPassword.empty.profileForm.oldPassword");
            }
            if (!CVUtils.inRange(form.getNewPassword().length(), MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH)) {
                Object[] args = { MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH };
                err.rejectValue("newPassword", "newPassword.outOfRange.profileForm.newPassword", args, null);
            }
        }
    }
}
