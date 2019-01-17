package com.epam.game.domain;

import com.epam.game.constants.GameType;

/**
 * Created by Sergey_Fedorov.
 */

 public class CommonStatistics {

	private GameType type;
	private int gameCount;
	private String zergName;
	private int zergCount;
	private String mostPlayedName;
	private int mostPlayedCount;
	private String fastestName;
	private int fastestNumberOfTurns;

	public GameType getType() {
		return type;
	}

	public void setType(GameType type) {
		this.type = type;
	}

	public int getGameCount() {
		return gameCount;
	}

	public void setGameCount(int gameCount) {
		this.gameCount = gameCount;
	}

	public String getZergName() {
		return zergName;
	}

	public void setZergName(String zergName) {
		this.zergName = zergName;
	}

	public int getZergCount() {
		return zergCount;
	}

	public void setZergCount(int zergCount) {
		this.zergCount = zergCount;
	}

	public String getMostPlayedName() {
		return mostPlayedName;
	}

	public void setMostPlayedName(String mostPlayedName) {
		this.mostPlayedName = mostPlayedName;
	}

	public int getMostPlayedCount() {
		return mostPlayedCount;
	}

	public void setMostPlayedCount(int mostPlayedCount) {
		this.mostPlayedCount = mostPlayedCount;
	}

	public String getFastestName() {
		return fastestName;
	}

	public void setFastestName(String fastestName) {
		this.fastestName = fastestName;
	}

	public int getFastestNumberOfTurns() {
		return fastestNumberOfTurns;
	}

	public void setFastestNumberOfTurns(int fastestNumberOfTurns) {
		this.fastestNumberOfTurns = fastestNumberOfTurns;
	}

	public String getTypeMessage() {
		return this.type.getMessage();
	}
}
