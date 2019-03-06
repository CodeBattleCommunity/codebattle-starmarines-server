package com.epam.game.gameinfrastructure.commands.server;

import com.epam.game.gamemodel.model.Edge;
import com.epam.game.gamemodel.model.Portal;
import com.epam.game.gamemodel.model.Vertex;
import com.epam.game.gamemodel.model.disaster.Disaster;
import com.epam.game.gamemodel.model.disaster.DisasterType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/29/2019
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Builder
public class DisasterInfo {
    private DisasterType type;
    private Long planetId;
    private Long sourcePlanetId;
    private Long targetPlanetId;

    public static DisasterInfo of(Disaster disaster) {
        Class<?> targetClass = disaster.getTarget().getClass();

        DisasterInfoBuilder builder = DisasterInfo.builder()
                .type(disaster.getType());

        if (targetClass.isAssignableFrom(Vertex.class)) {
            Disaster<Vertex> vertex = disaster;
            builder.planetId(vertex.getTarget().getId());
        } else if (targetClass.isAssignableFrom(Edge.class) || targetClass.isAssignableFrom(Portal.class)) {
            Disaster<Edge> edge = disaster;
            builder.sourcePlanetId(edge.getTarget().getSource());
            builder.targetPlanetId(edge.getTarget().getTarget());
        }
        
        return builder.build();
    }
}
