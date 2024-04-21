package com.mvarlamov.lab10.configuration;

import com.mvarlamov.lab10.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class BeanConfig {
    @Autowired
    DataSource dataSource;

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public UserDAO userDAO() {
        return new UserDAO(jdbcTemplate());
    }

    @Bean
    public SessionDAO sessionDAO() {
        return new SessionDAO(jdbcTemplate());
    }

    @Bean
    public TagDAO tagDAO() {
        return new TagDAO(jdbcTemplate());
    }

    @Bean
    public CommentDAO commentDAO() {
        return new CommentDAO(jdbcTemplate());
    }

    @Bean
    public DishDAO dishDAO() {
        return new DishDAO(jdbcTemplate(), tagDAO());
    }
}
