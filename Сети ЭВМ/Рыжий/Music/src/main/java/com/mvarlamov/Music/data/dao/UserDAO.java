package com.mvarlamov.Music.data.dao;

import com.mvarlamov.Music.data.model.Role;
import com.mvarlamov.Music.data.model.User;
import com.mvarlamov.Music.data.rest.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {
    @Autowired
    JdbcTemplate template;

    public void create(UserRequest user) {
        User newUser = new User()
                .setAge(user.getAge())
                .setName(user.getName())
                .setGender(user.getGender())
                .setLogin(user.getLogin())
                .setPwdHash(user.getPassword())
                .setRole(Role.USER); //TODO

        create(newUser);
    }
    public void create(User user) {
        System.err.println(user);
        template.update(
                "INSERT INTO User_ (login, pwdHash, role, name, age, gender) VALUES(?,?,?,?,?,?)",
                user.getLogin(),
                user.getPwdHash(),
                user.getRole().name(),
                user.getName(),
                user.getAge(),
                user.getGender().name()
        );
    }

    public User getByLogin(String login) {
        try {
            return template.queryForObject(
                    "SELECT * FROM User_ WHERE login = ?",
                    new BeanPropertyRowMapper<>(User.class),
                    login
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
