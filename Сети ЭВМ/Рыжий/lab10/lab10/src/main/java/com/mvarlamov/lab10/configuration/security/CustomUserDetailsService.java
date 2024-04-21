package com.mvarlamov.lab10.configuration.security;

import com.mvarlamov.lab10.dao.UserDAO;
import com.mvarlamov.lab10.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.getUserByLogin(username);
        if (user == null)
            throw new UsernameNotFoundException("User not found with login '" + username + "'");

        return user;
    }
}
