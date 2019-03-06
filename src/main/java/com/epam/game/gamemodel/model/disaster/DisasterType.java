package com.epam.game.gamemodel.model.disaster;

import java.util.*;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/28/2019
 */
public enum DisasterType {

    METEOR,
    BLACK_HOLE;

    public static List<DisasterType> LOCAL_DISASTERS = Collections.unmodifiableList(new ArrayList<DisasterType>() {{
        add(METEOR);
    }});

    public static List<DisasterType> INTER_PLANET_DISASTERS = Collections.unmodifiableList(new ArrayList<DisasterType>() {{
        add(BLACK_HOLE);
    }});
}
