package com.mvarlamov.Music.data.dao;

import com.mvarlamov.Music.data.model.Artist;
import com.mvarlamov.Music.data.model.Song;
import com.mvarlamov.Music.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SongDAO {
    @Autowired
    JdbcTemplate template;

    public List<Song> getUserLiked(User user) {
        return template.query(
                "SELECT * " +
                        "FROM SONG " +
                        "INNER JOIN LIKED_SONGS " +
                        "ON SONG.id = LIKED_SONGS.songId " +
                        "WHERE LIKED_SONGS.userLogin = ?",
                songRowMapper(),
                user.getLogin()
        );
    }

    public List<Song> getArtistSongs(Artist artist) {
        return template.query(
                "SELECT * FROM Song WHERE artistId = ?",
                songRowMapperWithLikedDetection(),
                artist.getId()
        );
    }

    public void create(Song song) {
        template.update(
                "INSERT INTO Song (name, genre, albumId, artistId, fileName) VALUES (?, ?, ?, ?, ?)",
                song.getName(),
                song.getGenre(),
                song.getAlbumId(),
                song.getArtistId(),
                song.getFileName()
        );
    }

    public void addToUserLiked(int songId, User user) {
        template.update(
                "INSERT INTO LIKED_SONGS (userLogin, songId) VALUES (?, ?)",
                user.getLogin(),
                songId
        );
    }

    public List<Song> getRandom(int max) {
        return template.query(
                "SELECT * FROM\n" +
                        "( SELECT * FROM Song\n" +
                        "ORDER BY dbms_random.value )\n" +
                        "WHERE rownum < ?",
                songRowMapperWithLikedDetection(),
                max
        );
    }

    public List<Song> getForUser(User user, int max) {
        List<User> users = template.query(
                "SELECT * FROM " +
                        "(SELECT * FROM User " +
                        "WHERE User.age >= ? AND WHERE User.age <= ? AND User.gender = ? " +
                        "ORDER BY dbms_random.value) " +
                        "WHERE rownum < ?",
                new BeanPropertyRowMapper<>(User.class),
                user.getAge(),
                user.getAge() + 10,
                user.getGender(),
                max
        );

        List<Song> res = new ArrayList<>();
        for (User u : users) {
            res.addAll(getUserLiked(u));
        }

        res = res.stream().filter(song -> Math.random() < 0.5).collect(Collectors.toList());

        if (res.size() < 5)
            res.addAll(getRandom(5 - res.size()));

        return res;
    }

    public void removeFromUserLiked(int songId, User user) {
        template.update(
                "DELETE FROM Liked_songs WHERE userLogin = ? AND songId = ?",
                user.getLogin(),
                songId
        );
    }

    public Song getById(int id) {
        try {
            return template.queryForObject(
                    "SELECT * FROM Song WHERE id = ?",
                    songRowMapperWithLikedDetection(),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Song getByIdAndUser(int id, User user) {
        try {
            return template.queryForObject(
                    "SELECT * " +
                            "FROM Song " +
                            "INNER JOIN Liked_songs " +
                            "ON Song.id = Liked_songs.songId " +
                            "WHERE Liked_songs.userLogin = ? " +
                            "AND Song.id = ?",
                    songRowMapper(),
                    user.getLogin(),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Song> getByName(String name) {
        return template.query(
                "SELECT * FROM Song WHERE name LIKE ? OR name LIKE ? OR name LIKE ?",
                songRowMapperWithLikedDetection(),
                String.format("%%%s%%", name),
                String.format("%s%%", name),
                String.format("%%%s", name)
        );
    }

    @Value("${songsPath}")
    private String songsPath;

    @Autowired
    ArtistDAO artistDAO;

    public RowMapper<Song> songRowMapperWithLikedDetection() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return (rs, rowNum) -> new Song()
                .setId(rs.getInt("id"))
                .setName(rs.getString("name"))
                .setGenre(rs.getString("genre"))
                .setArtistId(rs.getInt("artistId"))
                .setArtistName(artistDAO.getById(rs.getInt("artistId")).getName())
                .setAlbumId(rs.getInt("albumId"))
                .setLiked(user != null && (getByIdAndUser(rs.getInt("id"), user) != null))
                .setFileName(songsPath + rs.getString("fileName"));
    }

    public RowMapper<Song> songRowMapper() {
        return (rs, rowNum) -> new Song()
                .setId(rs.getInt("id"))
                .setName(rs.getString("name"))
                .setGenre(rs.getString("genre"))
                .setArtistId(rs.getInt("artistId"))
                .setArtistName(artistDAO.getById(rs.getInt("artistId")).getName())
                .setAlbumId(rs.getInt("albumId"))
                .setLiked(true)
                .setFileName(songsPath + rs.getString("fileName"));
    }
}
