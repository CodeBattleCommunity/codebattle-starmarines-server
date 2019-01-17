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

    public static boolean isCorrectPhoneNumber(String phone) {
        return phone.matches("[\\+]{0,1}(\\(?[0-9]\\)?[\\s-]?){5,12}");
    }

    public static boolean isGoodEmailPart(String email) {
        return email.matches("[\\w\\._\\d]+") && inRange(email.length(), 4,200);
    }
}
