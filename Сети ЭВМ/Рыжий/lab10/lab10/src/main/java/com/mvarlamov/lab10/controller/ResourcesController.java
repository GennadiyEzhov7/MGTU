package com.mvarlamov.lab10.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvarlamov.lab10.utils.FilesStorageServiceImpl;
import com.sun.istack.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourcesController {
    final ObjectMapper mapper = new ObjectMapper();
    @GetMapping("/images/{imageId}")
    public ResponseEntity<Resource> getImage(
            @PathVariable @NotNull String imageId
    ) {
        try {
            Resource resource = new FilesStorageServiceImpl().load(imageId);
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
