package com.mvarlamov.lab10.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comment {
    private int id;
    private int dishId;
    private String text;
    private String userLogin;

    public Comment(){}
}
