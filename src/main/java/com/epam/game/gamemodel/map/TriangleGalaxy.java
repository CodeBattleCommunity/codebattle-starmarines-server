package com.epam.game.gamemodel.map;

import com.epam.game.bot.domain.PlanetType;
import com.epam.game.domain.DisasterSettings;
import com.epam.game.domain.User;
import com.epam.game.gameinfrastructure.commands.server.DisasterInfo;
import com.epam.game.gameinfrastructure.commands.server.GalaxySnapshot;
import com.epam.game.gameinfrastructure.commands.server.PlanetInfo;
import com.epam.game.gamemodel.model.Edge;
import com.epam.game.gamemodel.model.Point;
import com.epam.game.gamemodel.model.Vertex;
import com.epam.game.gamemodel.model.VertexType;
import com.epam.game.gamemodel.model.disaster.Disaster;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.epam.game.gamemodel.model.disaster.DisasterType.BLACK_HOLE;
import static com.epam.game.gamemodel.model.disaster.DisasterType.METEOR;
import static java.util.Optional.ofNullable;

/**
 * MapGenerator implementation. It has both default and parametrized
 * constructors.
 * 
 * @author Evgeny_Tetuhin
 * 
 */
@Slf4j
public class TriangleGalaxy extends Galaxy {

    private long indexer = 0L;
    private int LAYER_WIDTH = 100;
    private int MINIMAL_LAYERS_COUNT = 3;
    private int MINIMAL_PLAYERS_COUNT = 2;

    private List<VertexType> layerTypes;

    private DisasterSettings disasterSettings;

    private Map<Long, Vertex> vertexes = new HashMap<>();

    private List<Edge> edges = new ArrayList<>();

    private Map<Edge, Disaster> interPlanetDisasters = new HashMap<>();

    private int maxInterPlanetDisasters;

    private Map<Long, Disaster> localPlanetDisasters = new HashMap<>();

    private int maxLocalDisasters;

    private Random seed = new Random();

    /**
     * Creates generator which makes vertex layers of all VertexTypes
     * sequentially.
     */
    TriangleGalaxy() {
        this(EnumSet.allOf(VertexType.class), DisasterSettings.DEFAULT);
    }

    /**
     * Creates generator which makes vertex layers of every VertexType specified
     * in parameter (including duplicates).
     * 
     * @param layerTypes
     */
    TriangleGalaxy(Collection<VertexType> layerTypes, DisasterSettings disasterSettings) {
        this.layerTypes = new LinkedList<>(layerTypes);
        this.disasterSettings = disasterSettings;
    }

    @Override
    public void generate(Map<Long, User> players) {
        if (players == null) {
            this.vertexes = null;
            return;
        }
        if (players.size() < MINIMAL_PLAYERS_COUNT) {
            throw new IllegalArgumentException("Not enough players to generate a map.");
        }
        if (layerTypes.size() < MINIMAL_LAYERS_COUNT) {
            throw new IllegalArgumentException("Not enough layers to generate a map.");
        }
        vertexes = new HashMap<>();
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
        vertexes.put(core.getId(), core);
        for (Vertex v : layers) {
            vertexes.put(v.getId(), v);
        }

        this.maxInterPlanetDisasters = (int) (disasterSettings.getInterPlanetDisasterFactor() * edges.size());
        this.maxLocalDisasters = (int) (disasterSettings.getLocalDisasterFactor() * vertexes.size());
    }

    @Override
    public GalaxySnapshot makeSnapshot() {
        List<PlanetInfo> planets = vertexes.values().stream()
                .map(vertex -> PlanetInfo.builder()
                        .id(vertex.getId())
                        .droids(vertex.getUnitsCount())
                        .owner(ofNullable(vertex.getOwner()).map(User::getUserName).orElse(null))
                        .type(PlanetType.byName(vertex.getType().toString()))
                        .neighbours(vertex.getNeighbours().stream().map(Vertex::getId).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
        List<DisasterInfo> disasters = makeDisastersSnapshot()
                .stream()
                .map(DisasterInfo::of)
                .collect(Collectors.toList());
        return new GalaxySnapshot(planets, disasters, new ArrayList<>());
    }

    private void cleanupExpiredDisasters(Map<?, Disaster> disasters) {
        disasters.entrySet().removeIf(disasterEntry -> disasterEntry.getValue().countDownTtl() > 0);
    }

    @Override
    public List<Disaster> generateDisasters() {
        cleanupExpiredDisasters(interPlanetDisasters);
        cleanupExpiredDisasters(localPlanetDisasters);

        vertexes.forEach((id, vertex) -> {
            if (!vertex.isBasePlanet() && maxLocalDisasters > localPlanetDisasters.size() && Math.random() < disasterSettings.getLocalDisasterProbability()) {
                localPlanetDisasters.put(vertex.getId(), new Disaster<>(METEOR, vertex, disasterSettings.getLocalDisasterTtl(), disasterSettings.getLocalDisasterDamage()));
            }
        });

        edges.forEach(edge -> {
            if (maxInterPlanetDisasters > interPlanetDisasters.size() && Math.random() < disasterSettings.getInterPlanetDisasterProbability()) {
                interPlanetDisasters.put(edge, new Disaster<>(BLACK_HOLE, edge, disasterSettings.getInterPlanetDisasterTtl(), disasterSettings.getInterPlanetDisasterDamage()));
            }
        });

        return makeDisastersSnapshot();
    }

    private List<Disaster> makeDisastersSnapshot() {
        return Stream.concat(
                interPlanetDisasters.values().stream(),
                localPlanetDisasters.values().stream()
        ).collect(Collectors.toList());
    }

    @Override
    public int moveUnits(User player, Vertex from, Vertex to, int unitsCount) throws Exception {
        Edge moveEdge = Edge.of(from.getId(), to.getId());
        if (interPlanetDisasters.containsKey(moveEdge)) {
            unitsCount = interPlanetDisasters.get(moveEdge).calculateUnits(unitsCount);
        }
        from.decreaseUnits(unitsCount, false);
        to.addUnits(player, unitsCount);
        return unitsCount;
    }

    @Override
    public void recalculateVertex(Vertex v) {
        if (localPlanetDisasters.containsKey(v.getId())) {
            int damage = localPlanetDisasters.get(v.getId()).calculateUnits(v.getUnitsCount());
            try {
                v.decreaseUnits(damage, true);
            } catch (Exception e) {
                log.error("Failed to decrease units by disaster", e);
            }
        }
        v.recalculate();
    }

    @Override
    public Map<Long, Vertex> getPlanets() {
        return vertexes;
    }

    private List<Vertex> generateBases(List<Vertex> prevLayer, VertexType vertexType, Map<Long, User> players, int width) {
        if (prevLayer == null || vertexType == null || players == null) {
            return null;
        }
        List<Vertex> result = new LinkedList<>();
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
            newBase.setBasePlanet(true);
            try {
                newBase.addUnits(player.next(), 100);
            } catch (Exception e) {
                //
            }
            result.add(newBase);
            i.next();
            for (int k = 1; k < linksToPrev; k++) {
                aVertex = i.next();
                edges.add(newBase.interconnect(aVertex));
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
        List<Vertex> result = new LinkedList<>();
        Vertex prev = null;
        Vertex cur = null;
        Point position = new Point(core.getX(), core.getY());
        position.setY(position.getY() - width);
        for (int i = 0; i < verticesInLayer; i++) {
            prev = cur;
            cur = new Vertex(++indexer, vertexType);
            cur.setName(nextName());
            edges.add(cur.interconnect(core));
            if (prev != null) {
                edges.add(prev.interconnect(cur));
            }
            cur.setPosition(position);
            result.add(cur);
            position = position.rotateAround(core.getPoint(), Math.PI * 2 / verticesInLayer);
        }
        edges.add(result.get(0).interconnect(result.get(result.size() - 1)));
        return result;
    }

    private List<Vertex> generateOuterLayer(List<Vertex> prevLayer, VertexType type, int width, boolean doubled) {
        if (prevLayer == null || type == null) {
            return null;
        }
        List<Vertex> result = new LinkedList<>();
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
                edges.add(new1.interconnect(new2));
            }
            edges.add(prevLayerA.interconnect(new1));
            result.add(new1);
            if (doubled) {
                new2 = new Vertex(++indexer, type);
                new2.setName(nextName());
                new2.setPosition(position);
                edges.add(new2.interconnect(new1));
                position = position.rotateAround(center, rotateAngle);
                edges.add(prevLayerA.interconnect(new2));
                edges.add(prevLayerB.interconnect(new2));
                result.add(new2);
            } else {
                new2 = new1; // new2 is set only to connect with a next
                             // iteration.
            }
            prevLayerA = prevLayerB;
        }
        edges.add(new2.interconnect(result.get(0)));
        return result;
    }
    
    private String nextName(){
        if(namingHandler != null){
            return namingHandler.nextName();
        }
        return null;
    }
}
