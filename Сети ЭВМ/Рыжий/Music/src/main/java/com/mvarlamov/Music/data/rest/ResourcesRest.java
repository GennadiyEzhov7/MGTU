package com.mvarlamov.Music.data.rest;

import com.mvarlamov.Music.data.dao.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/content")
public class ResourcesRest {
    @Value("${songsSaveFolder}")
    String songsSaveFolder;

    @Autowired
    SessionDAO sessionDAO;

    @RequestMapping("/song/{id}")
    public void getSong(@PathVariable("id") String fileName, @RequestParam String token, HttpServletResponse response) {
        if (sessionDAO.getByToken(token) == null) {
                response.setStatus(403);
                return;
        }

        try {
            Path path = Paths.get(songsSaveFolder, fileName);
            if (!Files.exists(path)) {
                response.setStatus(404);
                return;
            }
            response.setContentType("audio/mp3");
            response.setContentLength((int) Files.size(path));
            Files.copy(path, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception ignored) {
            response.setStatus(500);
        }
    }

    @Value("${imagesSaveFolder}")
    String imagesSaveFolder;

    @PreAuthorize("hasAuthority('ANON')")
    @RequestMapping("/image/{id}")
    public void getPicture(@PathVariable("id") String id, HttpServletResponse response) {
        try {
            Path path = Paths.get(imagesSaveFolder, id);
            if (!Files.exists(path)) {
                response.setStatus(404);
                return;
            }
            response.setContentType("image/jpeg");
            response.setContentLength((int) Files.size(path));
            Files.copy(path, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception ignored) {
            response.setStatus(500);
        }
    }
}
