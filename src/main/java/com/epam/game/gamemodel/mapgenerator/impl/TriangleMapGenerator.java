package com.epam.game.gamemodel.mapgenerator.impl;

import com.epam.game.domain.User;
import com.epam.game.gamemodel.mapgenerator.MapGenerator;
import com.epam.game.gamemodel.model.Point;
import com.epam.game.gamemodel.model.Vertex;
import com.epam.game.gamemodel.model.VertexType;

import java.util.*;

/**
 * MapGenerator implementation. It has both default and parametrized
 * constructors.
 * 
 * @author Evgeny_Tetuhin
 * 
 */
public class TriangleMapGenerator extends MapGenerator {

    private long indexer = 0L;
    private int LAYER_WIDTH = 100;
    private int MINIMAL_LAYERS_COUNT = 3;
    private int MINIMAL_PLAYERS_COUNT = 2;

    private List<VertexType> layerTypes;

    /**
     * Creates generator which makes vertex layers of all VertexTypes
     * sequentially.
     */
    public TriangleMapGenerator() {
        this(EnumSet.allOf(VertexType.class));
    }

    /**
     * Creates generator which makes vertex layers of every VertexType specified
     * in parameter (including duplicates).
     * 
     * @param layerTypes
     */
    public TriangleMapGenerator(Collection<VertexType> layerTypes) {
        this.layerTypes = new LinkedList<VertexType>(layerTypes);
    }

    @Override
    public Map<Long, Vertex> generate(Map<Long, User> players) {
        if (players == null) {
            return null;
        }
        if (players.size() < MINIMAL_PLAYERS_COUNT) {
            throw new IllegalArgumentException("Not enough players to generate a map.");
        }
        if (layerTypes.size() < MINIMAL_LAYERS_COUNT) {
            throw new IllegalArgumentException("Not enough layers to generate a map.");
        }
        Map<Long, Vertex> result = new HashMap<Long, Vertex>();
        int currentLayerNumber = 0;
        Vertex core = new Vertex(++indexer, layerTypes.get(currentLayerNumber++));
        core.setName(nextName());
        core.setX(0);
        core.setY(0);

        List<Vertex> layers = generateInnerLayer(core, layerTypes.get(currentLayerNumber++), players.size(), LAYER_WIDTH);
        List<Vertex> prevLayer = layers;
        while (currentLayerNumber < layerTypes.size() - 1) {
            List<Vertex> newLayer = generateOuterLayer(prevLayer, layerTypes.get(currentLayerNumber++), LAYER_WIDTH, true);
            layers.addAll(newLayer);
            prevLayer = newLayer;
        }
        List<Vertex> basesLayerList = generateBases(prevLayer, layerTypes.get(currentLayerNumber++), players, LAYER_WIDTH);
        layers.addAll(basesLayerList);
        result.put(core.getId(), core);
        for (Vertex v : layers) {
            result.put(v.getId(), v);
        }

        return result;
    }

    private List<Vertex> generateBases(List<Vertex> prevLayer, VertexType vertexType, Map<Long, User> players, int width) {
        if (prevLayer == null || vertexType == null || players == null) {
            return null;
        }
        List<Vertex> result = new LinkedList<Vertex>();
        if (prevLayer.size() % players.size() != 0) {
            throw new IllegalArgumentException("Unable to set bases on the map. Outer layer size should be divisible by number of bases.");
        }
        int linksToPrev = prevLayer.size() / players.size();
        Point center = new Point(0, 0);
        Vertex newBase = null;
        Vertex aVertex;
        Point position;
        Iterator<Vertex> i = prevLayer.iterator();
        Iterator<User> player = players.values().iterator();
        while (i.hasNext()) {
            newBase = new Vertex(++indexer, vertexType);
            newBase.setName(nextName());
            try {
                newBase.addUnits(player.next(), 100);
            } catch (Exception e) {
                //
            }
            result.add(newBase);
            i.next();
            for (int k = 1; k < linksToPrev; k++) {
                aVertex = i.next();
                newBase.interconnect(aVertex);
                if (k == linksToPrev / 2) { // it is central underlying vertex.
                    position = aVertex.getPoint().changeDistance(center, width);
                    newBase.setPosition(position);
                }
            }
        }
        return result;
    }

    private List<Vertex> generateInnerLayer(Vertex core, VertexType vertexType, int verticesInLayer, int width) {
        if (core == null || vertexType == null || verticesInLayer == 0) {
            return null;
        }
        List<Vertex> result = new LinkedList<Vertex>();
        Vertex prev = null;
        Vertex cur = null;
        Point position = new Point(core.getX(), core.getY());
        position.setY(position.getY() - width);
        for (int i = 0; i < verticesInLayer; i++) {
            prev = cur;
            cur = new Vertex(++indexer, vertexType);
            cur.setName(nextName());
            cur.interconnect(core);
            if (prev != null) {
                prev.interconnect(cur);
            }
            cur.setPosition(position);
            result.add(cur);
            position = position.rotateAround(core.getPoint(), Math.PI * 2 / verticesInLayer);
        }
        result.get(0).interconnect(result.get(result.size() - 1));
        return result;
    }

    private List<Vertex> generateOuterLayer(List<Vertex> prevLayer, VertexType type, int width, boolean doubled) {
        if (prevLayer == null || type == null) {
            return null;
        }
        List<Vertex> result = new LinkedList<Vertex>();
        double rotateAngle = (doubled) ? Math.PI / prevLayer.size() : Math.PI * 2 / prevLayer.size();
        Vertex prevLayerA;
        Vertex prevLayerB;
        Vertex new1;
        Vertex new2 = null;
        Point position;
        Point center = new Point(0, 0);
        prevLayerA = prevLayer.get(prevLayer.size() - 1);
        position = prevLayerA.getPoint().changeDistance(center, width);
        for (Vertex v : prevLayer) {
            prevLayerB = v;
            new1 = new Vertex(++indexer, type);
            new1.setName(nextName());
            new1.setPosition(position);
            position = position.rotateAround(center, rotateAngle);
            if (new2 != null) {
                new1.interconnect(new2);
            }
            prevLayerA.interconnect(new1);
            result.add(new1);
            if (doubled) {
                new2 = new Vertex(++indexer, type);
                new2.setName(nextName());
                new2.setPosition(position);
                new2.interconnect(new1);
                position = position.rotateAround(center, rotateAngle);
                prevLayerA.interconnect(new2);
                prevLayerB.interconnect(new2);
                result.add(new2);
            } else {
                new2 = new1; // new2 is set only to connect with a next
                             // iteration.
            }
            prevLayerA = prevLayerB;
        }
        new2.interconnect(result.get(0));
        return result;
    }
    
    private String nextName(){
        if(namingHandler != null){
            return namingHandler.nextName();
        }
        return null;
    }
}
