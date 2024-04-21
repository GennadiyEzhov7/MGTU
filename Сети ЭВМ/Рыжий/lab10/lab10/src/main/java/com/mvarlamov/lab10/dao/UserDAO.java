package com.mvarlamov.lab10.dao;

import com.mvarlamov.lab10.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

public class UserDAO {
    private final JdbcTemplate db;

    public UserDAO(JdbcTemplate db) {
        this.db = db;
    }

    public void create(User user) {
        db.update("INSERT INTO User VALUES (?,?,?,?)",
                user.getName(),
                user.getLogin(),
                user.getPassword(),
                user.getRole().name());
    }

    public User getUserById(int id) {
        List<User> result = db.query(
                "SELECT * FROM User WHERE id = ?",
                new BeanPropertyRowMapper<>(User.class),
                id);

        if (result.isEmpty())
            return null;

        return result.get(0);
    }

    public User getUserByLogin(String login) {
        List<User> res = db.query(
                "SELECT * FROM User WHERE login = ?",
                new BeanPropertyRowMapper<>(User.class),
                login);

        if (res.isEmpty())
            return null;

        return res.get(0);
    }

    public User getUserBySessionToken(String token) {
        List<User> result = db.query(
                "SELECT * FROM User WHERE login = (SELECT userLogin FROM Session WHERE token = ?)",
                new BeanPropertyRowMapper<>(User.class),
                token);

        if (result.isEmpty())
            return null;

        return result.get(0);
    }
}
