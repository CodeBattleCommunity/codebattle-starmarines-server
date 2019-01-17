package com.epam.game.controller.dtos;

import com.epam.game.domain.Game;
import com.epam.game.domain.User;
import com.epam.game.gamemodel.model.GameInstance;

/**
 * Additional information about a game. Just a DTO.
 */
public class GameInfo {

    private Game gameStatistics;
    private GameInstance gameObject;
    private User creator;

    public Game getGameStatistics() {
        return gameStatistics;
    }

    public void setGameStatistics(Game gameStatistics) {
        this.gameStatistics = gameStatistics;
    }

    public GameInstance getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameInstance gameObject) {
        this.gameObject = gameObject;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
