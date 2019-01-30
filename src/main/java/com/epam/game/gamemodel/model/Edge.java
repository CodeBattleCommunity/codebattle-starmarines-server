package com.epam.game.gamemodel.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/29/2019
 */
@Data
@AllArgsConstructor
public class Edge {
    private long source;
    private long target;

    public static Edge of(long source, long target) {
        return new Edge(source, target);
    }
}
