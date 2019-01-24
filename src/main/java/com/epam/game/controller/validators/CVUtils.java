package com.epam.game.controller.validators;

/**
 * Custom Validation Utils.
 * 
 * @author Evgeny_Tetuhin
 *
 */
public class CVUtils {
    
    public static boolean isEmailValid(String email) {
        return email.matches("[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}"); 
    }
    
    public static boolean inRange(int num, int min, int max) {
        return num >= min && num <= max;
    }
}
