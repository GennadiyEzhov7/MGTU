package com.mvarlamov.lab10.dao;

import com.mvarlamov.lab10.model.User;
import org.springframework.jdbc.core.JdbcTemplate;

public class SessionDAO {
    final private JdbcTemplate db;

    public SessionDAO(JdbcTemplate db) {
        this.db = db;
    }

    public void createSession(User user, String token) {
        db.update(
                "INSERT INTO Session (userLogin, token) VALUES (?, ?)",
                user.getLogin(),
                token);
    }

    public void removeSession(String token) {
        db.update(
                "DELETE FROM Session WHERE token = ?",
                token);
    }
}
