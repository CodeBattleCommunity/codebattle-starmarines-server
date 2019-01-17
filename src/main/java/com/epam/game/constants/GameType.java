package com.epam.game.constants;

public enum GameType {
                        //TODO вынести надписи в файл
    PLAYER_TOURNAMENT("Бои игроков"),
	ADMIN_TOURNAMENT("Турниры"),
	TRAINING_LEVEL("Тренировки");

	private String message;

	public String getMessage(){
		return this.message;
	}

	GameType(String message) {
		this.message = message;
	}
}
