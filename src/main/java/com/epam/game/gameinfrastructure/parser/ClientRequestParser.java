package com.epam.game.gameinfrastructure.parser;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;


/**
 * Interface for parsers
 * @author Andrey_Eremeev
 *
 */
public interface ClientRequestParser {

    /**
     * It parsed string to list of 
     * @param str
     * @return
     * @throws SAXException
     * @throws IOException
     */
    public List<ClientsDataObject> parse(String str) throws SAXException, IOException;
}
