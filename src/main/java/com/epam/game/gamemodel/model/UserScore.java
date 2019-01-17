package com.epam.game.gamemodel.model;

import com.epam.game.constants.GameType;
import com.epam.game.domain.User;

public class UserScore {
    
    private User user;
    
    private int turnsSurvived;
    
    private int place;
    
    private int unitsCount;

	private long gameId;

	private GameType type;

	public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getTurnsSurvived() {
        return turnsSurvived;
    }

    public void setTurnsSurvived(int turnsSurvived) {
        this.turnsSurvived = turnsSurvived;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getUnitsCount() {
        return unitsCount;
    }

    public void setUnitsCount(int unitsCount) {
        this.unitsCount = unitsCount;
    }

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public GameType getType() {
		return type;
	}

	public void setType(GameType type) {
		this.type = type;
	}
}
