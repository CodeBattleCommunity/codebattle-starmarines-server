package com.epam.game.bot.xml;

import com.epam.game.bot.domain.Planet;
import com.epam.game.bot.domain.PlanetType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parser implementation.
 *
 */
public class ModelParser extends DefaultHandler {

    private Map<Long, Planet> planets;
    private Map<Long, List<Long>> links;
    private List<String> errors;
    private Planet currentPlanet;
    private String currentTagName;
    private Logger log = Logger.getLogger(ModelParser.class.getName());
    
    public Map<Long, Planet> getPlanets(){
        return planets;
    }
    
    @Override
    public void startDocument() throws SAXException {
        planets = new HashMap<Long, Planet>();
        links = new HashMap<Long, List<Long>>();
        errors = new LinkedList<String>();
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentTagName = qName;
        if(TagNames.NODE.getValue().equals(qName)){
            currentPlanet = new Planet();
            String idValue = attributes.getValue(TagNames.ID.getValue());
            if(idValue != null){
                try{
                    Long id = Long.parseLong(idValue);
                    currentPlanet.setId(id);
                } catch(NumberFormatException e){
                    log.log(Level.SEVERE, "Error parsing id. ", e);
                }
            }
        }
    }
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(TagNames.OWNER.getValue().equals(currentTagName)){
            currentPlanet.setOwner(new String(ch, start, length));
        } else if(TagNames.TYPE.getValue().equals(currentTagName)){
            currentPlanet.setType(PlanetType.valueOf(new String(ch, start, length)));
        } else if(TagNames.UNITS.getValue().equals(currentTagName)){
            String unitsValue = new String(ch, start, length);
            try{
                Integer units = Integer.parseInt(unitsValue);
                currentPlanet.setDroids(units);
            } catch(NumberFormatException e){
                log.log(Level.SEVERE, "Error parsing droids count. ", e);
            }
        } else if(TagNames.NEIGHBOUR.getValue().equals(currentTagName)){
            Long id = currentPlanet.getId();
            String neighbourIdValue = new String(ch, start, length);
            try{
                Long neighbourId = Long.parseLong(neighbourIdValue);
                if(!links.containsKey(id)){
                    links.put(id, new LinkedList<Long>());
                }
                links.get(id).add(neighbourId);
            } catch(NumberFormatException e){
                log.log(Level.SEVERE, "Error parsing neighbour id. ", e);
            }
        } else if(TagNames.ERROR.getValue().equals(currentTagName)){
            String error = new String(ch, start, length);
            errors.add(error);
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(TagNames.NODE.getValue().equals(qName)){
            planets.put(currentPlanet.getId(), currentPlanet);
            currentPlanet = null;
        } else if(TagNames.LIST_ROOT.getValue().equals(qName)){
            for(Map.Entry<Long, List<Long>> entry : links.entrySet()){
                for(Long neighborId : entry.getValue()){
                    Planet p = planets.get(entry.getKey());
                    if(p != null){
                        p.setNeighbour(planets.get(neighborId));
                    }
                }
            }
        }
    }

    public List<String> getErrors() {
        return errors;
    }
}
