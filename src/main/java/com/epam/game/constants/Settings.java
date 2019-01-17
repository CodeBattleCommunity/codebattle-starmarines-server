package com.epam.game.constants;

public class Settings {

    /**
     * A port to listen for players requests.
     */
    public static final int PORT = 10040;

    /**
     * Delay before error response sending. Used to reduce server loading.
     */
    public static final long ERROR_RESPONSE_DELAY = 500;

    public static final int MAXIMAL_TITLE_LENGTH = 200;

	public static final int DAYS_TO_SHOW = 1;

	public static final int STAT_ROWS_TO_SHOW = 30;
    public static final boolean REGISTRATION_IS_OPEN = false;
}
