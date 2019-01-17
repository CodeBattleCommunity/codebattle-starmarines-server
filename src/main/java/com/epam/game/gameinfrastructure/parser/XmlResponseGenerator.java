package com.epam.game.gameinfrastructure.parser;

import com.epam.game.gamemodel.model.Vertex;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Map;

/**
 * generate xml-document from map of vertices
 * @author Andrey_Eremeev
 *
 */
public class XmlResponseGenerator {

    private DocumentBuilder builder;

    public XmlResponseGenerator() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * vertices to xml-Document
     * @param vertices - map of vertices
     * @return 
     */
    public Document generate(Map<Long, Vertex> vertices) {
        Document doc = builder.newDocument();
        Element response = doc.createElement(ResponseXMLTag.RESPONSE_ROOT.getValue());
        doc.appendChild(response);
        Element verticesElement = doc.createElement(ResponseXMLTag.VERTEXES.getValue());
        response.appendChild(verticesElement);
        for (Map.Entry<Long, Vertex> entry : vertices.entrySet()) {
            Vertex vertex = entry.getValue();
            Element vertexElement = doc.createElement(ResponseXMLTag.VERTEX.getValue());
            vertexElement.setAttribute(ResponseXMLTag.VERTEX_ID.getValue(), vertex.getId().toString());
            verticesElement.appendChild(vertexElement);
            Element ownerElement = doc.createElement(ResponseXMLTag.VERTEX_OWNER.getValue());
            if (vertex.getOwner() != null) {
                ownerElement.appendChild(doc.createTextNode(vertex.getOwner().getUserName()));
            }
            vertexElement.appendChild(ownerElement);        
            Element typeElement = doc.createElement(ResponseXMLTag.VERTEX_TYPE.getValue());
            typeElement.appendChild(doc.createTextNode(vertex.getType()
                    .toString()));
            vertexElement.appendChild(typeElement);
            Element unitsCountElement = doc.createElement(ResponseXMLTag.VERTEX_UNITS.getValue());
            unitsCountElement.appendChild(doc.createTextNode(String
                    .valueOf(vertex.getUnitsCount())));
            vertexElement.appendChild(unitsCountElement);
            if (vertex.getNeighbours().size() > 0) {
                Element neighboursElement = doc.createElement(ResponseXMLTag.NEIGHBOURS.getValue());
                vertexElement.appendChild(neighboursElement);
                for (Vertex neighbourVertex : vertex.getNeighbours()) {
                    Element neighbourVertexElement = doc
                            .createElement(ResponseXMLTag.NEIGHBOUR.getValue());
                    neighbourVertexElement
                            .appendChild(doc.createTextNode(neighbourVertex
                                    .getId().toString()));
                    neighboursElement.appendChild(neighbourVertexElement);
                }
            }
        }
        builder.reset();
        return doc;
    }
}
