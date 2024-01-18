package com.continental.game;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ActionSet implements Serializable {

    private final List<Action> instantActions;
    private final List<Action> longTermActions;

    public ActionSet() {
        instantActions = new ArrayList<>();
        longTermActions = new ArrayList<>();
    }

}
