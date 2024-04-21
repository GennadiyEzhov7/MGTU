package com.mvarlamov.Music;

import com.mvarlamov.Music.data.dao.ArtistDAO;
import com.mvarlamov.Music.data.dao.UserDAO;
import com.mvarlamov.Music.data.model.Gender;
import com.mvarlamov.Music.data.model.Role;
import com.mvarlamov.Music.data.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class MusicApplicationTests {
	@Autowired
	public ArtistDAO artistDAO;

	@Test
	void contextLoads() {
		System.err.println(artistDAO.getRandom(5));
	}

	@Autowired
	public UserDAO userDAO;
	@Test
	public void userTest() {
		final String login = "testUser";
		User user = new User()
				.setLogin(login)
				.setName("test username")
				.setAge(111)
				.setGender(Gender.Female)
				.setPwdHash("test pwd hash")
				.setRole(Role.ADMIN);

		userDAO.create(user);

		User user1 = userDAO.getByLogin(login);

		Assert.isTrue(user.equals(user1), "Users are not equals!");
	}

}
