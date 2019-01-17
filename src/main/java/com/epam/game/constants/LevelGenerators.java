package com.epam.game.constants;

import com.epam.game.gamemodel.mapgenerator.MapGenerator;
import com.epam.game.gamemodel.mapgenerator.impl.TriangleMapGenerator;
import com.epam.game.gamemodel.model.VertexType;
import com.epam.game.gamemodel.naming.impl.FileRandomNamingHandler;

import java.util.*;

public class LevelGenerators {
    
    public static int TRIANGLE_POTENT_BASES = 1;
    public static int TRIANGLE_POTENT_CENTER = 2;
    public static int TRIANGLE_RANDOM_PICK = 3;

    private static Map<Integer, MapGenerator> generators;
    
    static {
        generators = new HashMap<Integer, MapGenerator>();
        
        MapGenerator g = new TriangleMapGenerator(EnumSet.allOf(VertexType.class));
        g.setNamingHandler(new FileRandomNamingHandler());
        generators.put(TRIANGLE_POTENT_BASES, g);

        List<VertexType> list = new LinkedList<VertexType>(EnumSet.allOf(VertexType.class));
        Collections.reverse(list);
        g = new TriangleMapGenerator(list);
        g.setNamingHandler(new FileRandomNamingHandler());
        generators.put(TRIANGLE_POTENT_CENTER, g);
    }
    
    public static MapGenerator getGenerator(int generatorType) {
         if(generatorType == TRIANGLE_RANDOM_PICK) {
             generatorType = (int) (Math.random()*2 + 1);
         }
         return generators.get(generatorType);
    }
    
    public static Collection<Integer> getAvailableGenerators() {
        ArrayList<Integer> types = new ArrayList<Integer>(generators.keySet());
        types.add(TRIANGLE_RANDOM_PICK);
        return types;
    }
}
