package com.epam.game.conf.security;

import com.epam.game.dao.UserDAO;
import com.epam.game.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameUserDetailsService implements UserDetailsService {
    private final UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByName = userDAO.getUserWith(username);
        if (userByName == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userByName;
    }
}
