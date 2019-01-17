package com.epam.game.gameinfrastructure.parser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SAXParserWrapper implements ClientRequestParser {

    @Override
    public List<ClientsDataObject> parse(String str) throws SAXException, IOException {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        PlayerRequestParser requestParser = new PlayerRequestParser();
        SAXParser parser;
        try {
            parser = parserFactory.newSAXParser();
            InputStream stream = new ByteArrayInputStream(str.getBytes());
            parser.parse(stream, requestParser);
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return requestParser.getResults();
    }
}