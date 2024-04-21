package com.mvarlamov.Music.data.rest.response;

import com.mvarlamov.Music.data.model.Action;
import lombok.Getter;

import java.util.List;

@Getter
public class ActionsResponse extends Response{
    private final List<Action> actions;
    public ActionsResponse(String message, boolean status, List<Action> actions) {
        super(message, status);
        this.actions = actions;
    }
}
