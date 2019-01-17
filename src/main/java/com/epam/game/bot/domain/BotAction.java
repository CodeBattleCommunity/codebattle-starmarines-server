package com.epam.game.bot.domain;

/**
 * Data transfer object for bot movings.
 * 
 * @author Evgeny_Tetuhin
 *
 */
public class BotAction {
    
    public Long from;
    public Long to;
    public int count;
    
    public BotAction(Long from, Long to, int count) {
        this.from = from;
        this.to = to;
        this.count = count;
    }
}
