package com.mvarlamov.lab10.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mvarlamov.lab10.dao.CommentDAO;
import com.mvarlamov.lab10.dao.DishDAO;
import com.mvarlamov.lab10.dao.TagDAO;
import com.mvarlamov.lab10.model.Dish;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminRestController {
    @Autowired
    DishDAO dishDAO;
    @Autowired
    CommentDAO commentDAO;

    @Autowired
    TagDAO tagDAO;
    public static final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/posts")
    public ResponseEntity<ObjectNode> posts(
            @ModelAttribute @NotNull Dish dish
    ) {
        try {
            dishDAO.createDish(dish);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    mapper.createObjectNode()
                            .put("status", "true")
                            .put("message", "Post created")
                            .put("postId", dishDAO.getLastDishId())
            );
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", e.getMessage())
            );
        }
    }

    @PostMapping("/posts/{id}")
    public ResponseEntity<ObjectNode> update(
            @ModelAttribute @NotNull Dish dish,
            @PathVariable int id
    ) {
        if (!dishDAO.exists(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", "Post doesn't exist")
            );

        try {
            dish.setId(id);
            dishDAO.updateDish(dish);
            return ResponseEntity.ok().body(
                    mapper.createObjectNode()
                            .put("status", "true")
                            .put("message", "Post updated")
            );
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", e.getMessage())
            );
        }
    }

    @DeleteMapping("/posts/{dishId}/comments/{commentId}")
    @ResponseBody
    public ResponseEntity<ObjectNode> deleteComment(
            @PathVariable @NotNull int dishId,
            @PathVariable @NotNull int commentId
    ) {

        if (!dishDAO.exists(dishId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", "Post doesn't exist")
            );

        if (!commentDAO.exists(commentId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", "Comment doesn't exist")
            );

        try {
            commentDAO.deleteComment(commentId);
            return ResponseEntity.ok().body(
                    mapper.createObjectNode()
                            .put("status", "true")
                            .put("message", "Comment deleted")
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", e.getMessage())
            );
        }
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ObjectNode> delete(
            @PathVariable int id
    ) {
        if (!dishDAO.exists(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", "Post doesn't exist")
            );

        try {
            dishDAO.deleteDish(id);
            return ResponseEntity.ok().body(
                    mapper.createObjectNode()
                            .put("status", "true")
                            .put("message", "Post id = " + id + " deleted")
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", e.getMessage())
            );
        }
    }
}
