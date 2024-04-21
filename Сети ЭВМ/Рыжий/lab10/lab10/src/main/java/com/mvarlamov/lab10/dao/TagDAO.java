package com.mvarlamov.lab10.dao;

import com.mvarlamov.lab10.model.Dish;
import com.mvarlamov.lab10.model.Tag;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.List;

public class TagDAO {
    private final JdbcTemplate db;

    public TagDAO(JdbcTemplate db) {
        this.db = db;
    }

    public void createTag(Tag tag) {
        db.update(
                "INSERT INTO Tag (dishId, text) VALUES (?,?)",
                tag.getDishId(),
                tag.getText());
    }

    public List<Tag> getTags(Dish dish) {
        return db.query(
                "SELECT * FROM Tag WHERE dishId = ?",
                new BeanPropertyRowMapper<>(Tag.class),
                dish.getId());
    }

    public boolean exists(int id) {
        return db.queryForObject("SELECT id From Tag WHERE id = ?",
                new SingleColumnRowMapper<>(),
                id
        ) != null;
    }

    public void deleteTag(int id) {
        db.update("DELETE FROM Tag WHERE id = ?",
                id
        );
    }
}
