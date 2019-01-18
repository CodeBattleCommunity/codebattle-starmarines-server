package com.epam.game.gameinfrastructure.commands.client;

import lombok.Data;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/16/2019
 */
@Data
public class ClientAction {
    private long from;
    private long to;
    private int unitsCount;
}
