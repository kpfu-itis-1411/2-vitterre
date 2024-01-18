package com.continental.game.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Direction {
    LEFT(-1),
    RIGHT(1);

    private final int sign;
}
