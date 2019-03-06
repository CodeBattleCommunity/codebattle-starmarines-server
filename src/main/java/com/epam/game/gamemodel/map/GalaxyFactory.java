package com.epam.game.gamemodel.map;

import com.epam.game.domain.GameSettings;
import com.epam.game.gamemodel.model.VertexType;
import com.epam.game.gamemodel.naming.impl.FileRandomNamingHandler;

import java.util.*;
import java.util.function.Function;

public class GalaxyFactory {
    
    public static int TRIANGLE_POTENT_BASES = 1;
    public static int TRIANGLE_POTENT_CENTER = 2;
    public static int TRIANGLE_RANDOM_PICK = 3;

    private static Map<Integer, Function<GameSettings, Galaxy>> generators;

    private final static EnumSet<VertexType> DEFAULT_VERTEX_TYPES = EnumSet.allOf(VertexType.class);
    
    static {
        generators = new HashMap<>();
        generators.put(TRIANGLE_POTENT_BASES, (gameSettings -> {
            Galaxy g = new TriangleGalaxy(DEFAULT_VERTEX_TYPES, gameSettings.getDisasterSettings(),gameSettings.getPortalSettings());
            g.setNamingHandler(new FileRandomNamingHandler());
            return g;
        }));

        List<VertexType> list = new LinkedList<VertexType>(EnumSet.allOf(VertexType.class));
        Collections.reverse(list);

        generators.put(TRIANGLE_POTENT_CENTER, gameSettings -> {
            Galaxy g = new TriangleGalaxy(list, gameSettings.getDisasterSettings(),gameSettings.getPortalSettings());
            g.setNamingHandler(new FileRandomNamingHandler());
            return g;
        });
    }

    public static Galaxy getDefault() {
        return new TriangleGalaxy();
    }
    
    public static Galaxy createGenerator(int generatorType, GameSettings gameSettings) {
         if(generatorType == TRIANGLE_RANDOM_PICK) {
             generatorType = (int) (Math.random()*2 + 1);
         }
         return generators.get(generatorType).apply(gameSettings);
    }
    
    public static Collection<Integer> getAvailableGenerators() {
        ArrayList<Integer> types = new ArrayList<>(generators.keySet());
        types.add(TRIANGLE_RANDOM_PICK);
        return types;
    }
}
