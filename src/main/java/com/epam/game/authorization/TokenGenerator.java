
package com.epam.game.authorization;

import java.util.Random;

/**
 * Generates unique tokens.
 * 
 * @author Evgeny_Tetuhin
 *
 */
public class TokenGenerator {
    
    static int LENGTH = 32;
    static char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
    
    /**
     * Sets length used in string generating.
     * 
     * @param length
     */
    public static void setLength(int length) {
        if(length >= 0){
            LENGTH = length;
        } else{
            throw new IllegalArgumentException("Length can not be 0 or negative value.");
        }
    }
    
    /**
     * Sets allowed characters used in string generating.
     * 
     * @param characters
     */
    public static void setAlphabet(String characters) {
        if(characters.length() > 0){
            ALPHABET = characters.toCharArray();
        } else{
            throw new IllegalArgumentException("String is too short.");
        }
    }
    
    /**
     * Generates random string. Default values is 16 for string length and [a-z,0-9] for allowed characters.
     * 
     * @return
     */
    public static String generate() {
        StringBuilder result = new StringBuilder();
        Random rand = new Random(System.currentTimeMillis());
        for(int i = 0; i < LENGTH; i++){
            result.append(ALPHABET[rand.nextInt(LENGTH)]);
        }
        return result.toString();
    }
}
