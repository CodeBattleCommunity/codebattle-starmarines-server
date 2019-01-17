package com.epam.game.gameinfrastructure.parser;

import com.epam.game.gamemodel.model.action.ActionsType;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for data from request
 * @author Andrey_Eremeev
 *
 */

public class ClientsDataObject {
    
    private ActionsType type;
    
    private Map<RequestXMLTag, String> params;

    public ClientsDataObject() {
        params = new HashMap<RequestXMLTag, String>();
    }

    public ActionsType getType() {
        return type;
    }

    public void setType(ActionsType type) {
        this.type = type;
    }

    public Map<RequestXMLTag, String> getParams() {
        return params;
    }

    public void setParams(Map<RequestXMLTag, String> params) {
        this.params = params;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ClientsDataObject)) {
            return false;
        }
        ClientsDataObject o = (ClientsDataObject) obj;
        if (o.getType() != type) {
            return false;
        }
        Map<RequestXMLTag, String> m = o.getParams();
        if (m.size() != params.size()) {
            return false;
        }
        for (Map.Entry<RequestXMLTag, String> entry : m.entrySet()) {
            if (params.get(entry.getKey()) != entry.getValue()) {
                return false;
            }
        }
        return true;
    }
    
}
