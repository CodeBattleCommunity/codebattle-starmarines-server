package com.epam.game.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Garin on 17.08.2014.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ForbiddenException extends RuntimeException {
}