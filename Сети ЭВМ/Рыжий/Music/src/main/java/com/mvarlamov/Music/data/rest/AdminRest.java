package com.mvarlamov.Music.data.rest;

import com.mvarlamov.Music.data.rest.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AdminRest {
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/test")
    public ResponseEntity<? extends Response> test() {
        return null;
    }
}
