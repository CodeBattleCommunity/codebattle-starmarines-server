package com.epam.game.bot.xml;

/**
 * Tag names enumeration
 * 
 */
public enum TagNames {
    
    LIST_ROOT("planets"), NODE("planet"), ID("id"), OWNER("owner"), UNITS("droids"), TYPE("type"), NEIGHBOURS_LIST("neighbours"), NEIGHBOUR("neighbour"), ERROR("error");
    
    private String value;
    
    private TagNames(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
