package com.epam.game.gameinfrastructure.requessthandling;

import com.epam.game.gameinfrastructure.commands.client.ClientCommand;
import com.epam.game.gameinfrastructure.commands.server.GalaxySnapshot;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/16/2019
 */
@Component
public class CommandConverter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SneakyThrows
    public String buildResponse(GalaxySnapshot snapshot, List<String> errors) {
        return OBJECT_MAPPER.writeValueAsString(snapshot.setErrors(errors));
    }

    @SneakyThrows
    public String buildErrorResponse(List<String> errors) {
        GalaxySnapshot galaxySnapshot = GalaxySnapshot.builder()
                .errors(errors)
                .build();
        return OBJECT_MAPPER.writeValueAsString(galaxySnapshot);
    }

    public ClientCommand convertToClientCommand(String str) throws IOException {
        return OBJECT_MAPPER.readValue(str, ClientCommand.class);
    }
}
