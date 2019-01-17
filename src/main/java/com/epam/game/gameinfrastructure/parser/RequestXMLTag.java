package com.epam.game.gameinfrastructure.parser;

/**
 * All allowed tags of request 
 * @author Andrey_Eremeev
 *
 */
public enum RequestXMLTag {
    TOKEN("token"), ACTIONS("actions"), ACTION("action"), FROM("from"), TO("to"), UNITSCOUNT("unitscount"), LOGIN("login"), REQUEST("request");
    
    private String value;
    
    private RequestXMLTag(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
