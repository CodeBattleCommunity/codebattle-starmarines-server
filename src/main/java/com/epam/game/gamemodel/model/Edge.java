package com.epam.game.gamemodel.model;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/29/2019
 */
@Data
@EqualsAndHashCode(of = {"source", "target"})
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Accessors(chain = true)
public class Edge {
    protected final long source;
    protected final long target;

    public static Edge of(long source, long target) {
        return new Edge(source, target);
    }
}
