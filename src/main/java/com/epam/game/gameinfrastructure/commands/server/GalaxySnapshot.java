package com.epam.game.gameinfrastructure.commands.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class GalaxySnapshot {
    @Builder.Default
    private List<PlanetInfo> planets = new ArrayList<>();
    @Builder.Default
    private List<String> errors = new ArrayList<>();
}
