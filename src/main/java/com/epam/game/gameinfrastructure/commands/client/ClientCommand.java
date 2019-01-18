package com.epam.game.gameinfrastructure.commands.client;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/16/2019
 */
@Data
public class ClientCommand {
    private String token;
    private List<ClientAction> actions = new ArrayList<>();
}
