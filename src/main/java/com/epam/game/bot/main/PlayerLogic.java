package com.epam.game.bot.main;

import com.epam.game.bot.domain.BotAction;
import com.epam.game.bot.domain.Planet;

import java.util.List;
import java.util.Map;

/**
 * Interface for player's logic.
 * 
 * @author Evgeny_Tetuhin
 *
 */
public interface PlayerLogic {

    /**
     * Implements some strategy for playing the game. Generates actions list by current dispositions and name of player.
     * 
     * @param situation - {@link Map} of planets
     * @param whoWeAre - name of player
     * @return - actions to be performed
     */
    List<BotAction> whatToDo(Map<Long, Planet> situation, String whoWeAre);
}
