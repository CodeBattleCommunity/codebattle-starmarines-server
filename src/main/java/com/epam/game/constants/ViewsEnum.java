package com.epam.game.constants;

public final class ViewsEnum {

	public static final String SIGN_UP = "signUp";
	public static final String LOGIN   = "login";
	public static final String PROFILE = "profile";
    public static final String DOCUMENTATION = "documentation";
    public static final String CURRENT_GAME = "currentGame";
    public static final String OPEN_GAMES = "showGames";
    public static final String LOGOUT = "logout";
    public static final String JOIN_TO_GAME = "joinToGame";
    public static final String START_GAME = "startGame";
    public static final String TRAINING_LEVEL = "training";
    public static final String LEAVE_GAME = "leaveGame";
    public static final String VIEW_DATA = "game/viewData";
    public static final String GENERATE_TOKEN = "generateToken";
    public static final String STATISTICS = "statistics";
    public static final String GAME_STATISTICS_AJAX = "gameStatistics";
	public static final String GAMES_STATISTICS = "gamesStatistics";
    public static final String HISTORY = "history";
    public static final String DELETE_GAME = "deleteGame";
    
    /* new views */
    public static final String INFO_PAGE = "info";
    public static final String SETTINGS = "settings";
    public static final String BATTLE = "battle";
    public static final String NO_COOKIE = "noCookie";

    public static final String GAME_CONTROL = "gameControl";
    public static final String GOD_MODE = "godPage";
    public static final String DELETE_FROM_GAME = "deletePlayer";
    public static final String CREATE_NEW_GAME = "createGame";
    public static final String SHOW_GAME_TABLE = "gameTable";
    public static final String GAME_LIST_AJAX = "gameListAsync";
    public static final String CHECK_GAME_AJAX = "checkGame";
    public static final String BROADCAST = "gameBroadcast";

    public static final String CRAWLER_VERIFY_PAGE = "b6114a316be6";


    public static final String EXTENSION = ".html";

    public static final String[] PUBLIC_PAGES = {LOGIN, SIGN_UP, SETTINGS, LOGOUT, NO_COOKIE, INFO_PAGE, CRAWLER_VERIFY_PAGE};

    public static boolean isPublicPage(String page) {
        for (String publicPage : PUBLIC_PAGES) {
            if((publicPage + EXTENSION).equals(page)) {
                return true;
            }
        }
        return false;
    }

    private ViewsEnum() {
    }
}
