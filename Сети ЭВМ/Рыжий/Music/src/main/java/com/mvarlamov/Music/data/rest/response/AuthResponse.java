package com.mvarlamov.Music.data.rest.response;
import lombok.Getter;

@Getter
public class AuthResponse extends Response {
    private final String token;
    public AuthResponse(String message, boolean status, String token) {
        super(message, status);
        this.token = token;
    }
}
