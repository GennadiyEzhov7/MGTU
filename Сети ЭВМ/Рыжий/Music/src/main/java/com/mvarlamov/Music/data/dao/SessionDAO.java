package com.mvarlamov.Music.data.dao;

import com.mvarlamov.Music.data.model.Session;
import com.mvarlamov.Music.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SessionDAO {
    @Autowired
    JdbcTemplate template;

    public Session create(User user) {
        logout(user);
        Session session = new Session();
        session.setUserLogin(user.getLogin());
        session.setToken(UUID.randomUUID().toString());

        template.update(
                "INSERT INTO Session_ (userLogin, token) VALUES (?, ?)",
                session.getUserLogin(),
                session.getToken()
        );

        return session;
    }

    public void logout(User user) {
        template.update(
                "DELETE FROM Session_ WHERE userLogin = ?",
                user.getLogin()
        );
    }

    public Session getByToken(String token) {
        try {
            return template.queryForObject(
                    "SELECT * FROM Session_ WHERE token = ?",
                    new BeanPropertyRowMapper<>(Session.class),
                    token
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
