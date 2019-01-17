package com.epam.game.bot.main;

/**
 * That class can be used to run logic of embedded bot as client application.
 *
 */
public class Main {
    
    public static void main(String[] args){
        String token = "tfye4caqhlwxcjx11xbrrkuhpwpkrjyj";
       
        
        String name = "garin";

         ConnectionHandler run = new ConnectionHandler(token, "localhost", 10040, name);
            Thread runn = new Thread(run);
            runn.start();
        
    }
}
