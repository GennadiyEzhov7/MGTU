package com.mvarlamov.Music.data.rest.request;

import com.mvarlamov.Music.data.model.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequest {
    private String login;
    private String password;
    private Gender gender;
    private Integer age;
    private String name;
}
