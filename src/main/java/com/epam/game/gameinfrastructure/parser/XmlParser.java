package com.epam.game.gameinfrastructure.parser;

import com.epam.game.gamemodel.model.action.ActionsType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Andrey_Eremeev
 *
 */
public class XmlParser implements ClientRequestParser {
    
    private DocumentBuilderFactory factory;
    
    private DocumentBuilder builder;

    
    public XmlParser() throws ParserConfigurationException, SAXException {
        factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setIgnoringComments(true);
        builder = factory.newDocumentBuilder();
    }

    public List<ClientsDataObject> parse(String str) throws SAXException, IOException {
        List<ClientsDataObject> dataObjectList = new ArrayList<ClientsDataObject>();
        if (str == null) {
            return dataObjectList;
        }
        ClientsDataObject dataObject;
        Document doc = builder.parse(new InputSource(new ByteArrayInputStream(str.getBytes())));
        String token = doc.getFirstChild().getFirstChild().getTextContent();
        Node actionsNode = doc.getFirstChild().getLastChild();
        if (RequestXMLTag.LOGIN.getValue().equalsIgnoreCase(actionsNode.getFirstChild().getNodeName())) {
            dataObject = new ClientsDataObject();
            dataObject.setType(ActionsType.LOGIN);
            dataObject.getParams().put(RequestXMLTag.TOKEN, token);
            dataObjectList.add(dataObject);
        }
        if (RequestXMLTag.ACTION.getValue().equalsIgnoreCase(actionsNode.getFirstChild().getNodeName())) {
            NodeList actions = actionsNode.getChildNodes();
            for (int i = 0; i < actions.getLength(); i++) {
                dataObject = new ClientsDataObject();
                NodeList actionsParam = actions.item(i).getChildNodes();
                dataObject.setType(ActionsType.MOVE);
                dataObject.getParams().put(RequestXMLTag.TOKEN, token);
                dataObject.getParams().put(RequestXMLTag.FROM, actionsParam.item(0).getTextContent());
                dataObject.getParams().put(RequestXMLTag.TO, actionsParam.item(1).getTextContent());
                dataObject.getParams().put(RequestXMLTag.UNITSCOUNT, actionsParam.item(2).getTextContent());
                dataObjectList.add(dataObject);
            }            
        }        
        
        return dataObjectList;
    }
}
