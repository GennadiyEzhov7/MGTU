package com.mvarlamov.lab10;

import com.mvarlamov.lab10.dao.UserDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootTest
class Lab10ApplicationTests {
	@Autowired
	DataSource dataSource;

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}



	@Test
	void contextLoads() {
		UserDAO userDAO = new UserDAO(jdbcTemplate());
		System.err.println(userDAO.getUserByLogin("admin"));
	}

}
