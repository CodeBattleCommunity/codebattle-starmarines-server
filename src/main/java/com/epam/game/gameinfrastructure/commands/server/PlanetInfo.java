package com.epam.game.gameinfrastructure.commands.server;

import com.epam.game.bot.domain.PlanetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/16/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanetInfo {
    private long id;
    private long droids;
    private String owner;
    private PlanetType type;
    private List<Long> neighbours;
}
