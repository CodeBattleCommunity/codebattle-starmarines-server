package com.epam.game.gameinfrastructure.parser;

import com.epam.game.gamemodel.model.action.ActionsType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PlayerRequestParser extends DefaultHandler {

    private String currentTagName;
    private ClientsDataObject currentDataObject;
    private String requestToken;
    private List<ClientsDataObject> clientObjects;

    public List<ClientsDataObject> getResults() {
        return clientObjects;
    }

    @Override
    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub
        super.startDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentTagName = qName;
        if (RequestXMLTag.ACTIONS.getValue().equals(currentTagName)) {
            clientObjects = new LinkedList<ClientsDataObject>();
        } else if (RequestXMLTag.ACTION.getValue().equals(currentTagName)) {
            currentDataObject = new ClientsDataObject();
            currentDataObject.setType(ActionsType.MOVE);
            currentDataObject.getParams().put(RequestXMLTag.TOKEN, requestToken);
        } else if (RequestXMLTag.LOGIN.getValue().equals(currentTagName)) {
            currentDataObject = new ClientsDataObject();
            currentDataObject.setType(ActionsType.LOGIN);
            currentDataObject.getParams().put(RequestXMLTag.TOKEN, requestToken);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length);
        if (currentDataObject != null && currentDataObject.getType().equals(ActionsType.MOVE)) {
            Map<RequestXMLTag, String> params = currentDataObject.getParams();
            if (RequestXMLTag.FROM.getValue().equals(currentTagName)) {
                params.put(RequestXMLTag.FROM, value);
            } else if (RequestXMLTag.TO.getValue().equals(currentTagName)) {
                params.put(RequestXMLTag.TO, value);
            } else if (RequestXMLTag.UNITSCOUNT.getValue().equals(currentTagName)) {
                params.put(RequestXMLTag.UNITSCOUNT, value);
            }
        } else if (RequestXMLTag.TOKEN.getValue().equals(currentTagName)) {
            requestToken = value;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentTagName != null && currentTagName.equals(qName)) {
            currentTagName = null;
        } 
        if (RequestXMLTag.ACTION.getValue().equals(qName) || RequestXMLTag.LOGIN.getValue().equals(qName)) {
            clientObjects.add(currentDataObject);
        } else if (RequestXMLTag.ACTIONS.getValue().equals(qName)) {
            if(clientObjects.size() == 0){
                ClientsDataObject loginDummy = new ClientsDataObject();
                loginDummy.setType(ActionsType.LOGIN);
                loginDummy.getParams().put(RequestXMLTag.TOKEN, requestToken);
                clientObjects.add(loginDummy);
            }
        }
    }

    @Override
    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub
        super.endDocument();
    }
}
