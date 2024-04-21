package com.mvarlamov.lab10.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvarlamov.lab10.dao.CommentDAO;
import com.mvarlamov.lab10.dao.DishDAO;
import com.mvarlamov.lab10.dao.TagDAO;
import com.mvarlamov.lab10.model.Comment;
import com.mvarlamov.lab10.model.Dish;
import com.mvarlamov.lab10.model.Tag;
import com.mvarlamov.lab10.model.User;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserRestController {
    @Autowired
    DishDAO dishDAO;
    @Autowired
    CommentDAO commentDAO;

    @Autowired
    TagDAO tagDAO;

    public static final ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/posts/{id}")
    public ResponseEntity<JsonNode> get(
            @PathVariable int id
    ) {
        try {
            if (!dishDAO.exists(id))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        mapper.createObjectNode()
                                .put("status", "false")
                                .put("message", "Post doesnt exist")
                );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", "Post doesnt exist")
            );
        }

        try {
            return ResponseEntity.ok(mapper.readTree(mapper.writeValueAsString(dishDAO.get(id))));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", "Internal error")
            );
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<JsonNode> getList() {
        try {
            return ResponseEntity.ok(mapper.readTree(mapper.writeValueAsString(dishDAO.getList())));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", "Internal error")
            );
        }
    }

    @GetMapping("/posts/tag/{tag}")
    @ResponseBody
    public ResponseEntity<JsonNode> getDishistByTag(
            @PathVariable @NotNull String tag
    ) {
        try {
            Tag t = new Tag();
            t.setText(tag);
            List<Dish> res = dishDAO.getDishByTag(t);
            if (res.size() != 0)
                return ResponseEntity.ok(mapper.readTree(mapper.writeValueAsString(res)));

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", "Tag doesn't exist")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", "Internal error")
            );
        }
    }

    @PostMapping("/posts/{dishId}/comments")
    public ResponseEntity<JsonNode> addComment(
            @ModelAttribute @NotNull Comment comment,
            @PathVariable @NotNull int dishId
    ) {
        try {
            if (!dishDAO.exists(dishId))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        mapper.createObjectNode()
                                .put("status", "false")
                                .put("message", "Post not found")
                );

            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (comment.getUserLogin() == null || comment.getUserLogin().isEmpty()) {
                if (user.getLogin() != null)
                    comment.setUserLogin(user.getLogin());
                else
                    return ResponseEntity.badRequest().body(
                            mapper.createObjectNode()
                                    .put("status", "false")
                                    .put("message", "Username for anonymous user must be filled")
                    );
            }
            comment.setDishId(dishId);
            commentDAO.createComment(comment);
            return ResponseEntity.ok().body(
                    mapper.createObjectNode()
                            .put("status", "true")
                            .put("message", "Comment created")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", "Internal error")
            );
        }
    }
}
