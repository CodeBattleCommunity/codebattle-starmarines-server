package com.epam.game.gameinfrastructure.parser;

/**
 * Tags for generating response.
 * 
 * @author Evgeny_Tetuhin
 * 
 */
public enum ResponseXMLTag {

    RESPONSE_ROOT("response"), VERTEXES("planets"), VERTEX("planet"), VERTEX_ID("id"), VERTEX_OWNER("owner"),
    VERTEX_TYPE("type"), VERTEX_UNITS("droids"), NEIGHBOURS("neighbours"), NEIGHBOUR("neighbour");

    private String value;

    private ResponseXMLTag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
