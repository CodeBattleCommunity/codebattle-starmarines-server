package com.epam.game.controller.validators;

import com.epam.game.constants.Settings;
import com.epam.game.controller.forms.CreateGameForm;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class CreateGameValidator implements Validator {

    @Override
    public boolean supports(Class<?> arg0) {
        return CreateGameForm.class.equals(arg0);
    }

    @Override
    public void validate(Object arg0, Errors err) {
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "title", "title.empty.createGameForm.title");
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "description", "description.empty.createGameForm.description");
        CreateGameForm createGameForm = (CreateGameForm) arg0;
        if (createGameForm.getTitle().length() > Settings.MAXIMAL_TITLE_LENGTH) {
            err.rejectValue("title", "errLength.createGameForm.title");
        }
    }

}