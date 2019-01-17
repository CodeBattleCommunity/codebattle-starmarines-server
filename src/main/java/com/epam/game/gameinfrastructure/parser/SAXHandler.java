package com.epam.game.gameinfrastructure.parser;

import com.epam.game.gamemodel.model.action.Action;
import com.epam.game.gamemodel.model.action.impl.MoveAction;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Not used
 * @author Andrey_Eremeev
 *
 */
public class SAXHandler extends DefaultHandler{
    
    private Queue<Action> actions;
    
    private MoveAction action;
    
    private String currentTag;
    
    private int uid;
    
    private String tmpValue;
    
    private Map<RequestXMLTag, String> actionsParam;
    
    SAXHandler() {
        super();
    }

    @Override
    public void startDocument() throws SAXException {
        actions = new LinkedList<Action>();
    }
    
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        currentTag = qName;
//        System.out.println(RequestXMLTag.UID.);
//        if(RequestXMLTag.UID.equals(qName)) {
//            currentTag = RequestXMLTag.UID;
//
//        }
        if (RequestXMLTag.ACTION.equals(qName)) {
//            action = new MoveAction();
//            TODO add setter
//            action.setUid(uid);
        }
        

    }
    
    public void endElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (RequestXMLTag.ACTION.getValue().equalsIgnoreCase(qName)) {
            actions.add(action);
        }
        if (RequestXMLTag.LOGIN.getValue().equalsIgnoreCase(qName)) {
//            Action action = ActionFactory.getInstance(ActionsType.LOGIN, actionsParam);
//            actions.add(action);
        }
            
    }
    
    
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
//        tmpValue = new String(ch, start, length);
        
        if (RequestXMLTag.TOKEN.getValue().equalsIgnoreCase(currentTag)) {
            
        }
        
        if (RequestXMLTag.FROM.getValue().equalsIgnoreCase(currentTag)) {
//            TODO 
//            action.setFROM();
        }     
                
        
        if (RequestXMLTag.TO.getValue().equalsIgnoreCase(currentTag)) {
//            TODO 
//            action.setTO();
        }
    }
}
