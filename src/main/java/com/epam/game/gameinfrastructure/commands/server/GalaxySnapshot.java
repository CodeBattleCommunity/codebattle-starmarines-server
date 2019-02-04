package com.epam.game.gameinfrastructure.commands.server;

import com.epam.game.gamemodel.model.Edge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/16/2019
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class GalaxySnapshot {
    @Builder.Default
    private List<PlanetInfo> planets = new ArrayList<>();
    @Builder.Default
    private List<DisasterInfo> disasters = new ArrayList<>();
    @Builder.Default
    private List<Edge> portals = new ArrayList<>();
    @Builder.Default
    private List<String> errors = new ArrayList<>();
}
