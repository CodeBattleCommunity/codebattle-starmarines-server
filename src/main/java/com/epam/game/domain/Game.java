package com.epam.game.domain;

import com.epam.game.constants.GameState;
import com.epam.game.constants.GameType;
import com.epam.game.gamemodel.model.UserScore;

import java.sql.Timestamp;
import java.util.List;

/**
 * Class for representing data from table "GAME_STATISTICS_AJAX"
 * 
 * @author Roman_Spiridonov
 * 
 */
public class Game {

	private long            gameId;
	private String          title;
	private GameType type;
	private String          description;
	private long            winnerId;
	private long            creatorId;
	private long            numberOfTurns;
	private String          logPath;
	private GameState state;
	private Timestamp       timeCreated;
	private List<UserScore> statistics;

	public long getGameId() {
		return gameId;
	}

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(long winnerId) {
        this.winnerId = winnerId;
    }

    public long getNumberOfTurns() {
        return numberOfTurns;
    }

    public void setNumberOfTurns(long numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

	public Timestamp getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Timestamp timeCreated) {
		this.timeCreated = timeCreated;
	}

	public List<UserScore> getStatistics() {
		return statistics;
	}

	public void setStatistics(List<UserScore> statistics) {
		this.statistics = statistics;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Title: " + title);
		sb.append("\nDescription: " + description);
		sb.append("\nCreated: " + timeCreated);
//		sb.append("\nStatistics: " + statistics);
		sb.append("\n\n");
//		private long            winnerId;
//		private List<UserScore> statistics;
		return sb.toString();
	}
}
