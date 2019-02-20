package com.epam.game.dao;

import com.epam.game.constants.GameState;
import com.epam.game.constants.GameType;
import com.epam.game.constants.Settings;
import com.epam.game.domain.*;
import com.epam.game.gamemodel.model.GameInstance;
import com.epam.game.gamemodel.model.UserScore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.security.RolesAllowed;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Roman_Spiridonov
 */
@Repository
@CacheConfig(cacheNames = "settings")
@Slf4j
public class GameDAO {

	private static final String SETTINGS_CACHE_KEY = "settings";

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedTemplate;

	private RowMapper<Game> rowMapper = new RowMapper<Game>() {
		@Override
		public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
			Game game = new Game();
			game.setGameId(rs.getLong("GAME_ID"));
			game.setTitle(rs.getString("TITLE"));
			game.setDescription(rs.getString("DESCRIPTION"));
			game.setLogPath(rs.getString("LOG_PATH"));
			game.setNumberOfTurns(rs.getLong("NUMBER_OF_TURNS"));
			game.setState(GameState.valueOf(rs.getString("STATE")));

			//fail
			game.setType(GameType.valueOf(rs.getString("TYPE")));

			game.setWinnerId(rs.getLong("WINNER_ID"));
			game.setCreatorId(rs.getLong("CREATOR_ID"));
			game.setTimeCreated(rs.getTimestamp("TIME_CREATED"));
			return game;
		}
	};

	private RowMapper<UserScore> statMapper = new RowMapper<UserScore>() {
		@Override
		public UserScore mapRow(ResultSet rs, int i) throws SQLException {
			UserScore us = new UserScore();
			us.setPlace(rs.getInt("PLACE"));
			us.setTurnsSurvived(rs.getInt("NUMBER_OF_TURNS"));
			us.setUnitsCount(rs.getInt("UNIT_COUNT"));
			us.setGameId(rs.getLong("GAME_ID"));
			us.setType(GameType.valueOf(rs.getString("TYPE")));
			User tempUser = new User();
			tempUser.setUserName(rs.getString("BOT_NAME"));
			tempUser.setId(rs.getLong("USER_ID"));
			tempUser.setEmail(rs.getString("EMAIL"));
			us.setUser(tempUser);
			return us;
	}};

	private RowMapper<CommonStatistics> commonStatMapper = new RowMapper<CommonStatistics>() {
		@Override
		public CommonStatistics mapRow(ResultSet rs, int i) throws SQLException {
			CommonStatistics cs = new CommonStatistics();

			cs.setType(GameType.valueOf(rs.getString("GAME_TYPE")));
			cs.setGameCount(rs.getInt("GAME_COUNT"));

			cs.setZergName(rs.getString("ZERG_NAME"));
			cs.setZergCount(rs.getInt("ZERG_COUNT"));

			cs.setMostPlayedName(rs.getString("HARDEST_NAME"));
			cs.setMostPlayedCount(rs.getInt("HARDEST_COUNT"));

			cs.setFastestName(rs.getString("FASTEST_NAME"));
			cs.setFastestNumberOfTurns(rs.getInt("FASTEST_NUMBER_OF_TURNS"));

			return cs;
		}
	};

	@AllArgsConstructor
	private enum SettingsOption {
		READING_TIMEOUT(Long::valueOf),
		GAME_TURN_DELAY(Long::valueOf),
		TRAINIG_BOT_LOGINS(v -> Arrays.asList(v.split(","))),
		NEXT_GAME_TIME(v -> LocalDateTime.parse(v, DateTimeFormatter.ISO_DATE_TIME)),
		ERROR_RESPONSE_DELAY(Long::valueOf),
		STAT_ROWS_TO_SHOW(Integer::valueOf),
		REGISTRATION_IS_OPEN(Boolean::valueOf),
		MINIMAL_PLAYERS_NUMBER(Integer::valueOf),
		MAXIMAL_PLAYERS_NUMBER(Integer::valueOf),
		GAME_TURNS_LIMIT(Long::valueOf),
		USER_BATTLE_CREATION_ENABLED(Boolean::valueOf),
		LOCAL_DISASTER_PROBABILITY_PER_VERTEX(Double::valueOf),
		LOCAL_DISASTER_FACTOR(Double::valueOf),
		LOCAL_DISASTER_DAMAGE(Double::valueOf),
		LOCAL_DISASTER_TTL_TICKS(Integer::valueOf),
		INTER_PLANET_DISASTER_PROBABILITY_PER_EDGE(Double::valueOf),
		INTER_PLANET_DISASTER_FACTOR(Double::valueOf),
		INTER_PLANET_DISASTER_DAMAGE(Double::valueOf),
		INTER_PLANET_DISASTER_TTL_TICKS(Integer::valueOf),
		PORTAL_OPENING_PROBABILITY(Double::valueOf),
		PORTAL_OPENING_FACTOR_BY_PLANETS(Double::valueOf),
		PLAYER_ACTIONS_LIMIT_PER_COMMAND(Long::valueOf),
		PORTAL_TTL(Integer::valueOf),

		GAME_SOURCES_URL(String::valueOf),
		GAME_CLIENTS_URL(String::valueOf);

		@Getter
		private Function<String, Object> extractor;
		public static SettingsOption getByName(String option) {
			return Stream.of(values())
					.filter(opt -> opt.name().equals(option))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException(String.format("Missing '%s' key in settings", option)));
		}
	}

	private RowMapper<FlattenSettings> flattenSettingsMapper = (rs, rowNum) -> {
        Map<String, String> opts = new HashMap<>();
        Map<String, String> descriptions = new HashMap<>();

        do {
            String id = rs.getString("ID");
            opts.put(id, rs.getString("VAL"));
            descriptions.put(id, rs.getString("DESCRIPTION"));
        } while (rs.next());
        return new FlattenSettings(opts, descriptions);
    };

	private RowMapper<GameSettings> settingsMapper = new RowMapper<GameSettings>() {

		@SuppressWarnings("unchecked")
		@Override
		public GameSettings mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<SettingsOption, Object> settings = new HashMap<>();
			do {
				SettingsOption option = SettingsOption.getByName(rs.getString("ID"));
				String val = rs.getString("VAL");
				settings.put(option, option.getExtractor().apply(val));
			} while (rs.next());

			DisasterSettings disasterSettings = DisasterSettings.builder()
					.interPlanetDisasterProbability((Double) settings.get(SettingsOption.INTER_PLANET_DISASTER_PROBABILITY_PER_EDGE))
					.interPlanetDisasterFactor((Double) settings.get(SettingsOption.INTER_PLANET_DISASTER_FACTOR))
					.interPlanetDisasterDamage((Double) settings.get(SettingsOption.INTER_PLANET_DISASTER_DAMAGE))
					.interPlanetDisasterTtl((Integer) settings.get(SettingsOption.INTER_PLANET_DISASTER_TTL_TICKS))
					.localDisasterProbability((Double) settings.get(SettingsOption.LOCAL_DISASTER_PROBABILITY_PER_VERTEX))
					.localDisasterFactor((Double) settings.get(SettingsOption.LOCAL_DISASTER_FACTOR))
					.localDisasterDamage((Double) settings.get(SettingsOption.LOCAL_DISASTER_DAMAGE))
					.localDisasterTtl((Integer) settings.get(SettingsOption.LOCAL_DISASTER_TTL_TICKS))
					.build();

			PortalSettings portalSettings = PortalSettings.builder()
					.portalOpeningProbability((Double)settings.get(SettingsOption.PORTAL_OPENING_PROBABILITY))
					.portalFactor((Double) settings.get(SettingsOption.PORTAL_OPENING_FACTOR_BY_PLANETS))
					.portalTtl((Integer)settings.get(SettingsOption.PORTAL_TTL))
					.build();

			DocInfo docInfo = DocInfo.builder()
					.gameSourcesURL((String) settings.get(SettingsOption.GAME_SOURCES_URL))
					.gameClientsURL((String) settings.get(SettingsOption.GAME_CLIENTS_URL))
					.build();

			return GameSettings.builder()
					.clientTimeoutMs((Long) settings.get(SettingsOption.READING_TIMEOUT))
					.turnDelayMs((Long) settings.get(SettingsOption.GAME_TURN_DELAY))
					.trainigBotLogins((List<String>) settings.get(SettingsOption.TRAINIG_BOT_LOGINS))
					.nextGame((LocalDateTime) settings.get(SettingsOption.NEXT_GAME_TIME))
					.errorDelayMs((Long) settings.get(SettingsOption.ERROR_RESPONSE_DELAY))
					.startRowsToShow((Integer) settings.get(SettingsOption.STAT_ROWS_TO_SHOW))
					.registrationOpened((Boolean) settings.get(SettingsOption.REGISTRATION_IS_OPEN))
					.gameCreationEnabled((Boolean) settings.get(SettingsOption.USER_BATTLE_CREATION_ENABLED))
					.minPlayers((Integer) settings.get(SettingsOption.MINIMAL_PLAYERS_NUMBER))
					.maxPlayers((Integer) settings.get(SettingsOption.MAXIMAL_PLAYERS_NUMBER))
					.roundTurns((Long) settings.get(SettingsOption.GAME_TURNS_LIMIT))
					.playerActionsLimitPerCommand((Long) settings.get(SettingsOption.PLAYER_ACTIONS_LIMIT_PER_COMMAND))
					.disasterSettings(disasterSettings)
					.portalSettings(portalSettings)
					.docInfo(docInfo)
					.build();
		}
	};

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

	@Cacheable(SETTINGS_CACHE_KEY)
    public GameSettings getSettings() {
		return jdbcTemplate.queryForObject("select ID, VAL from SETTINGS", settingsMapper);
	}

	@CacheEvict(value = SETTINGS_CACHE_KEY, allEntries = true)
	@RolesAllowed("ROLE_ADMIN")
	public void restoreDefaultSettings() {
		jdbcTemplate.execute("update SETTINGS set VAL = DEF");
	}

	@CacheEvict(value = SETTINGS_CACHE_KEY, allEntries = true)
	@RolesAllowed("ROLE_ADMIN")
	public void applySettings(FlattenSettings settings) {
		settings.getOpts().forEach((key, value) -> jdbcTemplate.update("update SETTINGS set val = ? where id = ?", ps -> {
			ps.setString(1, value);
			ps.setString(2, key);
		}));
	}

	public FlattenSettings getFlattenSettings() {
	    return jdbcTemplate.queryForObject("select ID, VAL, DESCRIPTION from SETTINGS", flattenSettingsMapper);
    }

	@RolesAllowed("ROLE_ADMIN")
	@CacheEvict(value = "settings", allEntries = true)
	public void evictSettingsCache() {
	    log.warn("Evicting settings cache");
    }

    public Long getNumberOfPlayedGamesFor(User user) {
//	    "select count(*) from GAME_SESSION_STATISTICS where USER_ID = ?"
        final List<Long> longs = jdbcTemplate.queryForList("select count(*) from GAME_SESSION_STATISTICS where USER_ID = ? and TYPE <> '"+GameType.TRAINING_LEVEL.name()+"'", Long.class, user.getId());
        return longs.isEmpty() ? 0L : longs.get(0);
    }

    public Long getNumberOfWonGamesFor(User user) {
//	    "select count(*) from GAME_SESSION_STATISTICS where USER_ID = 10 and PLACE = 1"
        final List<Long> longs = jdbcTemplate.queryForList("select count(*) from GAME_SESSION_STATISTICS where USER_ID = ? and PLACE = 1",
                Long.class, user.getId());
        return longs.isEmpty() ? 0L : longs.get(0);
    }

    public void addGame(Game game) {
        doRequestForGame(
                "INSERT INTO GAME_STATISTICS (TITLE, TYPE, DESCRIPTION, WINNER_ID, CREATOR_ID, NUMBER_OF_TURNS, LOG_PATH, STATE, TIME_CREATED, GAME_ID ) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                game);
    }

    public void updateGame(final Game game, final GameInstance instance) {
	    jdbcTemplate.batchUpdate("INSERT INTO GAME_SESSION_STATISTICS " +
	                             "(CREATOR_ID, " +
	                             "GAME_ID, " +
	                             "USER_ID, " +
	                             "UNIT_COUNT, " +
	                             "NUMBER_OF_TURNS, " +
	                             "PLACE, " +
	                             "BOT_NAME, " +
	                             "TYPE, " +
	                             "EMAIL) " +
	                             "VALUES (?,?,?,?,?,?,?,?,?) ", new BatchPreparedStatementSetter() {
		    @Override
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
			    UserScore score = instance.getStatistics().get(i);
			    score.setGameId(game.getGameId());

			    ps.setLong(1, instance.getCreator().getId());
			    ps.setLong(2, score.getGameId());
			    ps.setLong(3, score.getUser().getId());
			    ps.setLong(4, score.getUnitsCount());
			    ps.setLong(5, score.getTurnsSurvived());
			    ps.setInt(6, score.getPlace());
			    ps.setString(7, score.getUser().getUserName());
			    ps.setString(8, score.getType().name());
			    ps.setString(9, score.getUser().getEmail());

		    }

		    @Override
		    public int getBatchSize() {
			    return instance.getStatistics().size();
		    }
	    });

	    doRequestForGame(
			    "UPDATE GAME_STATISTICS SET TITLE = ?, TYPE = ?, DESCRIPTION = ?, WINNER_ID = ?, CREATOR_ID = ?, NUMBER_OF_TURNS = ?, LOG_PATH = ?, STATE = ?, TIME_CREATED = ? WHERE GAME_ID = ?",
			    game);
    }

    public Game getById(Long gameId) {
        return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM GAME_STATISTICS WHERE GAME_ID = ?", rowMapper, gameId));
    }

    public void deleteById(Long gameId) {
        jdbcTemplate.update("DELETE FROM GAME_STATISTICS WHERE GAME_ID = ?", gameId);
    }

    public List<Game> getFinishedGames() {
        return jdbcTemplate.query("SELECT * FROM GAME_STATISTICS WHERE STATE = 'FINISHED'", rowMapper);
    }

	public List<Game> getStatistics() {
		GregorianCalendar gc = new GregorianCalendar();

		gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
		gc.set(GregorianCalendar.MINUTE, 0);
		gc.set(GregorianCalendar.SECOND, 0);
		gc.set(GregorianCalendar.MILLISECOND,0);
		return getStatistics(gc);
	}

	public List<Game> getStatistics(GregorianCalendar gc) {

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		Timestamp longTimeAgo = new Timestamp(gc.getTimeInMillis());
		parameters.addValue("longTimeAgo", longTimeAgo);
		parameters.addValue("rowsToShow", Settings.STAT_ROWS_TO_SHOW);
//		List<Game> recentGames = jdbcTemplate.query("select * from GAME_STATISTICS where TIME_CREATED notnull and TIME_CREATED > ? and TYPE = 'BATTLE' and STATE = 'FINISHED' order by TIME_CREATED desc limit 30", rowMapper, longTimeAgo);

		List<Game> recentGames = namedTemplate.query("select * from GAME_STATISTICS where GAME_ID in (select GAME_ID from GAME_SESSION_STATISTICS group by GAME_ID) and TYPE <> '" + GameType.TRAINING_LEVEL.name() + "' and TIME_CREATED notnull order by TIME_CREATED desc limit (:rowsToShow)",parameters, rowMapper);
		List<Long> ids = new ArrayList(recentGames.size());
		for(Game g : recentGames) {
			ids.add(g.getGameId());
		}
		parameters.addValue("ids", ids);
		if(ids.size() > 0) {
			List<UserScore> statistics = namedTemplate.query("select * from GAME_SESSION_STATISTICS where GAME_ID in (:ids)", parameters, statMapper);
			for(Game g : recentGames) {
				g.setStatistics(new LinkedList<UserScore>());
				for(UserScore us : statistics) {
					if(us.getGameId() == g.getGameId()) {
						g.getStatistics().add(us);
					}
				}
			}
//			for(int i = recentGames.size() - 1; i >= 0; i--) {
//				if(recentGames.get(i).getStatistics().size() == 0) {
//					recentGames.remove(i);
//
//				}
//			}
		}

		return recentGames;
	}

    public List<Game> getById(Collection<Long> ids) {
        List<Game> gamesList = new ArrayList<Game>();
        for (Long id : ids) {
            gamesList.add(getById(id));
        }
        return gamesList;
    }

    public List<Game> getFinishedTournaments() {
        return jdbcTemplate.query("SELECT * FROM GAME_STATISTICS WHERE STATE = 'FINISHED' AND TYPE <> 'TRAINING_LEVEL' ", rowMapper);
    }

    public void addPlayer(Long userId, Long gameId) {
        jdbcTemplate.update("DELETE FROM PLAYERS WHERE USER_ID=? AND GAME_ID=?", userId, gameId);
    }

    public void deletePlayer(Long userId, Long gameId) {
        jdbcTemplate.update("INSERT INTO PLAYERS (USER_ID, GAME_ID) VALUES(?, ?)", userId, gameId);
    }

    private void doRequestForGame(String request, Game game) {
        jdbcTemplate.update(request,
                game.getTitle(),
                game.getType().toString(),
                game.getDescription(),
                game.getWinnerId(),
                game.getCreatorId(),
                game.getNumberOfTurns(),
                game.getLogPath(),
                game.getState().toString(),
		        game.getTimeCreated(),
                game.getGameId());
    }




	public List<CommonStatistics> getCommonStatistics() {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		
		parameters.addValue("botNames", getSettings().getTrainigBotLogins());
		String query = "select \n" +
		               "commonCount.TYPE game_TYPE, \n" +
		               "commonCount.count GAME_COUNT, \n" +
		               "theZerg.botName ZERG_NAME,\n" +
		               "theZerg.unitCount ZERG_COUNT,\n" +
		               "theHardest.botName HARDEST_NAME,\n" +
		               "theHardest.count HARDEST_COUNT,\n" +
		               "theFastest.botName FASTEST_NAME,\n" +
		               "theFastest.numberOfTurns FASTEST_NUMBER_OF_TURNS\n" +
		               "from\n" +
		               "(select count(*), gs.TYPE from GAME_STATISTICS gs where STATE='FINISHED' group by gs.TYPE) as commonCount left join\n" +
		               "(select distinct (array_agg(UNIT_COUNT))[1]as unitCount, (array_agg(BOT_NAME))[1] as botName, TYPE from GAME_SESSION_STATISTICS where BOT_NAME not in (:botNames) and UNIT_COUNT in (select max(UNIT_COUNT) from GAME_SESSION_STATISTICS group by TYPE) group by TYPE) as theZerg\n" +
		               "on commonCount.TYPE = theZerg.TYPE left join\n" +
		               "\n" +
		               "--hardest = max game count\n" +
		               "(select (array_agg(count))[1] count, (array_agg(BOT_NAME))[1] botName, TYPE from (select * from (select count(*),BOT_NAME,TYPE from GAME_SESSION_STATISTICS where BOT_NAME not in (:botNames) group by BOT_NAME, TYPE) games order by count desc) gssx group by TYPE\n" +
		               "\n" +
		               ") as theHardest\n" +
		               "on commonCount.TYPE = theHardest.TYPE left join \n" +
		               "\n" +
		               "--fastest = max wins\n" +
		               "(select (array_agg(count))[1] numberOfTurns, (array_agg(BOT_NAME))[1] botName, TYPE from (select * from (select count(*),BOT_NAME,TYPE from GAME_SESSION_STATISTICS where BOT_NAME not in (:botNames) and PLACE = 1 group by BOT_NAME, TYPE) games order by count desc) gssx group by TYPE)\n" +
		               "\n" +
		               "\t  as theFastest\n" +
		               "\t on commonCount.TYPE = theFastest.TYPE";

		return namedTemplate.query(query, parameters, commonStatMapper);
	}
}
