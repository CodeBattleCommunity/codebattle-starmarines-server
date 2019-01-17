/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.game.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Sergey_Fedorov
 */
  @Controller
public class VerifyPageController {



    @RequestMapping(value = "/b6114a316be6.html", method= RequestMethod.GET )
    public String ShowVerifyPage() {
                return "verifyPage";
    }

}
