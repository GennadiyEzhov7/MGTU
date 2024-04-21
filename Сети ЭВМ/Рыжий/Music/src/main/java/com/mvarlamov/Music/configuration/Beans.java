package com.mvarlamov.Music.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvarlamov.Music.data.dao.ArtistDAO;
import com.mvarlamov.Music.data.dao.SessionDAO;
import com.mvarlamov.Music.data.dao.SongDAO;
import com.mvarlamov.Music.data.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class Beans {
    @Autowired
    Environment environment;

/*    @Bean
    public UserDAO userDAO() {
        return new UserDAO();
    }

    @Bean
    public ArtistDAO artistDAO() {
        return new ArtistDAO();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public SongDAO songDAO() {
        return new SongDAO();
    }

    @Bean
    public SessionDAO sessionDAO() {
        return new SessionDAO();
    }*/

    @Bean
    public String hostPath() {
        return String.format(
                "%s:%s",
                environment.getProperty("server.host"),
                environment.getProperty("server.port")
        );
    }
}
