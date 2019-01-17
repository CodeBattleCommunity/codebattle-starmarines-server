package com.epam.game.bot.domain;

/**
 * Enumeration of strategies for planets. Supposed to be used in the advanced bot logic.
 * 
 * @author Evgeny_Tetuhin
 *
 */
public enum Behaviors {
    
    /**
     * The planet gathers resources and send them only when
     * nearest planets become NEEDY.
     */
    STORAGE,
    
    /**
     * The planet maintains such number of units, which will be
     * regenerated to maximum after next turn. Regenerated units
     * will be sended to the STORAGE or to other planets during the next turn.
     */
    GENERATOR,
    
    /**
     * The planet has empty neighbors and tries to occupy them.
     */
    EXPLORER,
    
    /**
     * The planet has enemies within it's powers and tries to attack them.
     */
    INVADER,
    
    /**
     * The planet has enemies with superior powers. Neighbors are sending units to that planet.
     */
    NEEDY,
    
    /**
     * The planet has enemies and has not any confederates. TONIGHT WE DINE IN HELL!11
     */
    SPARTAN
    
}
