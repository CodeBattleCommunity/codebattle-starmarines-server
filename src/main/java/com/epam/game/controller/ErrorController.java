package com.epam.game.controller;

import com.epam.game.controller.exceptions.ForbiddenException;
import com.epam.game.controller.exceptions.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Shows stubs for different errors
 * Created by Evgeny_Tetuhin on 8/5/2014.
 */
@ControllerAdvice
@Controller
public class ErrorController {

    private static Logger log = Logger.getLogger(ErrorController.class.getName());

    @ExceptionHandler(Exception.class)
    public String handleError(HttpServletRequest req, Exception exception) {
        log.log(Level.SEVERE, "Request: " + req.getRequestURL() + " raised " + exception);
	    exception.printStackTrace();
        return "systemError";
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleResourceNotFoundException() {
        return "notFound";
    }

    @RequestMapping("notFound.html")
    public String notFound(HttpServletRequest req, Exception exception){
        throw new NotFoundException();
    }

    @ExceptionHandler(ForbiddenException.class)
    public String handleForbiddenException() {
        return "forbidden";
    }
}
