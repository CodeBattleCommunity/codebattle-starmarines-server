package com.epam.game.gameinfrastructure.requessthandling;

import com.epam.game.bot.domain.PlanetType;
import com.epam.game.domain.User;
import com.epam.game.gameinfrastructure.commands.client.ClientCommand;
import com.epam.game.gameinfrastructure.commands.server.GalaxySnapshot;
import com.epam.game.gameinfrastructure.commands.server.PlanetInfo;
import com.epam.game.gamemodel.model.Vertex;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/16/2019
 */
@Component
public class CommandConverter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SneakyThrows
    public String buildResponse(Map<Long, Vertex> vertices, List<String> errors) {
        List<PlanetInfo> planets = vertices.values().stream()
                .map(vertex -> PlanetInfo.builder()
                        .id(vertex.getId())
                        .droids(vertex.getUnitsCount())
                        .owner(ofNullable(vertex.getOwner()).map(User::getUserName).orElse(null))
                        .type(PlanetType.byName(vertex.getType().toString()))
                        .neighbours(vertex.getNeighbours().stream().map(Vertex::getId).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
        return OBJECT_MAPPER.writeValueAsString(new GalaxySnapshot(planets, errors));
    }

    @SneakyThrows
    public String buildErrorResponse(List<String> errors) {
        GalaxySnapshot galaxySnapshot = GalaxySnapshot.builder()
                .errors(errors)
                .build();
        return OBJECT_MAPPER.writeValueAsString(galaxySnapshot);
    }

    @SneakyThrows
    public ClientCommand convertToClientCommand(String str)  {
        return OBJECT_MAPPER.readValue(str, ClientCommand.class);
    }
}
