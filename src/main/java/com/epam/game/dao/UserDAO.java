package com.epam.game.dao;

import com.epam.game.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Roman_Spiridonov
 */
@Repository
public class UserDAO {

    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> rowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("ID"));
            user.setUserName(rs.getString("USER_NAME"));
            user.setLogin(rs.getString("LOGIN"));
            user.setPassword(rs.getString("PASSWORD"));
            user.setToken(rs.getString("TOKEN"));
            user.setEmail(rs.getString("EMAIL"));
            user.setBot(rs.getBoolean("IS_BOT"));
            user.setAuthorities(getUserAuthorities(user.getId()));
            return user;
        }
    };

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User getUserWith(String login, String password) {
        return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM USERS WHERE LOGIN = ? AND PASSWORD = ?", rowMapper, login, password));
    }

    public User getUserWith(Long userId) {
        return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM USERS WHERE ID = ?", rowMapper, userId));
    }

    public User getUserWith(String login) {
        return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM USERS WHERE LOGIN = ?", rowMapper, login));
    }

    public void addAuthorityToUser(Long userId, GrantedAuthority authority) {
        jdbcTemplate.update("INSERT INTO AUTHORITIES (USER_ID, AUTHORITY) VALUES(?, ?)", userId, authority.getAuthority());
    }

    public void addUser(User user) {
        jdbcTemplate.update("INSERT INTO USERS (USER_NAME, LOGIN, PASSWORD, EMAIL) VALUES(?, ?, ?, ?)",
                user.getUserName(), user.getLogin(), user.getPassword(), user.getEmail());
        User userFromDB = getUserWith(user.getLogin(), user.getPassword());
        if (userFromDB != null) {
            for (GrantedAuthority authority : user.getAuthorities()) {
                addAuthorityToUser(userFromDB.getId(), authority);
            }
        }
    }

    public List<User> getRealPlayers() {
        return jdbcTemplate.query("SELECT u.* FROM USERS u INNER JOIN AUTHORITIES a on u.id = a.user_id WHERE u.IS_BOT = false and lower(a.authority) not like '%admin%'", rowMapper);
    }

    public void updateUser(User user) {
        jdbcTemplate.update("UPDATE USERS SET USER_NAME = ?, LOGIN = ?, PASSWORD = ?, TOKEN = ?, EMAIL = ? WHERE ID = ?",
                user.getUserName(), user.getLogin(), user.getPassword(), user.getToken(), user.getEmail(), user.getId());
    }

    public String updateToken(long id) {
        return jdbcTemplate.queryForObject("UPDATE USERS SET TOKEN = uuid_generate_v4() WHERE ID = ? RETURNING token",
                new Object[] {id}, (rs, rowNum) -> rs.getString("token"));
    }

    private List<GrantedAuthority> getUserAuthorities(Long userId) {
        return jdbcTemplate.query("SELECT * FROM AUTHORITIES WHERE USER_ID = ?", new RowMapper<GrantedAuthority>() {
            @Override
            public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new SimpleGrantedAuthority(rs.getString("AUTHORITY"));
            }
        }, userId);
    }
}
