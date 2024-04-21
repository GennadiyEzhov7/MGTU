package com.mvarlamov.lab10.dao;

import com.mvarlamov.lab10.utils.FilesStorageService;
import com.mvarlamov.lab10.model.Dish;
import com.mvarlamov.lab10.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.web.multipart.MultipartFile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishDAO {
    private final JdbcTemplate db;
    private final TagDAO tagDAO;

    @Autowired
    public CommentDAO commentDAO;
    @Autowired
    FilesStorageService storageService;

    public DishDAO(JdbcTemplate db, TagDAO tagDAO) {
        this.db = db;
        this.tagDAO = tagDAO;
    }

    public void validateImage(MultipartFile file) {
        if (file.getSize() > (2 * 1024 * 1024))
            throw new IllegalStateException("Image size is > than 2MB");

        if (file.getContentType() != null &&
                !file.getContentType().equals("image/png") &&
                !file.getContentType().equals("image/jpeg"))
            throw new IllegalStateException("Image format isn't png or jpeg");
    }

    public void updateDish(Dish dish) {
        String name = null;
        if (dish.getImage() != null) {
            validateImage(dish.getImage());
            name = dish.getImage().getOriginalFilename();
            storageService.save(dish.getImage(), dish.getImage().getOriginalFilename());
        }

        Dish dishOld = get(dish.getId());

        db.update(
                "UPDATE Dish SET title = ?, anons = ?, text = ?, imagePath= ? WHERE id = ?",
                dish.getTitle() == null ? dishOld.getTitle() : dish.getTitle(),
                dish.getAnons() == null ? dishOld.getAnons() : dish.getAnons(),
                dish.getText() == null ? dishOld.getText() : dish.getText(),
                name == null ? dishOld.getImagePath() : name,
                dish.getId()
        );

        int id = getLastDishId();

        if (dish.getTags() != null)
            dish.getTags().forEach(tag -> {
                tag.setDishId(id);
                tagDAO.createTag(tag);
            });
    }

    public void createDish(Dish dish) {
        String name = "";
        if (dish.getImage() != null) {
            validateImage(dish.getImage());
            name = dish.getImage().getOriginalFilename();
            storageService.save(dish.getImage(), dish.getImage().getOriginalFilename());
        }

        dish.setDateTime(System.currentTimeMillis());
        db.update(
                "INSERT INTO Dish(title, dateTime, anons, text, imagePath) values(?,?,?,?,?)",
                dish.getTitle(),
                dish.getDateTime().getTime(),
                dish.getAnons(),
                dish.getText(),
                name
        );

        int id = getLastDishId();

        if (dish.getTags() != null)
            dish.getTags().forEach(tag -> {
                tag.setDishId(id);
                tagDAO.createTag(tag);
            });
    }

    public boolean exists(int id) {
        try {
            return db.queryForObject(
                    "SELECT id FROM Dish WHERE id = ? ",
                    new SingleColumnRowMapper<>(),
                    id
            ) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteDish(int id) {
        db.execute("PRAGMA foreign_keys=ON");

        db.update(
                "DELETE FROM Dish  WHERE id = ?",
                id
        );
    }

    public Dish get(int id) {
        Dish res = db.queryForObject(
                "SELECT * FROM Dish WHERE id = ? ",
                new BeanPropertyRowMapper<>(Dish.class),
                id
        );
        if (res == null)
            return null;

        res.setTagsAsList(tagDAO.getTags(res));
        res.setComments(commentDAO.getComments(res));

        return res;
    }

    public List<Dish> getList() {
        List<Dish> res = db.query(
                "SELECT * FROM Dish",
                new BeanPropertyRowMapper<>(Dish.class)
        );

        for (Dish dish : res) {
            dish.setTagsAsList(tagDAO.getTags(dish));
            dish.setComments(commentDAO.getComments(dish));
        }

        return res;

    }

    public int getLastDishId() {
        Integer id = db.queryForObject(
                "SELECT id FROM Dish ORDER BY ID DESC LIMIT 1",
                new SingleColumnRowMapper<>(Integer.class));
        if (id != null)
            return id;
        return 1;
    }

    public List<Dish> getDishByTag(Tag tag) {
        List<Integer> res = db.query(
                "SELECT dishId FROM Tag where text = ?",
                new SingleColumnRowMapper<>(),
                tag.getText()
        );

        List<Dish> dishes = new ArrayList<>();
        for (Integer i : res) {
            Dish dish = get(i);
            if (dish != null)
                dishes.add(dish);
        }

        return dishes;
    }

    public class DishRowMapper implements RowMapper<Dish> {
        @Override
        public Dish mapRow(ResultSet rs, int rowNum) throws SQLException {
            Dish dish = new Dish();
            dish.setId(rs.getInt("id"));
            dish.setTitle(rs.getString("title"));
            dish.setAnons(rs.getString("anons"));
            dish.setText(rs.getString("text"));
            dish.setDateTime(rs.getLong("dateTime"));
            dish.setTagsAsList(tagDAO.getTags(dish));
            dish.setImagePath(rs.getString("imagePath"));
            return dish;
        }
    }

}
