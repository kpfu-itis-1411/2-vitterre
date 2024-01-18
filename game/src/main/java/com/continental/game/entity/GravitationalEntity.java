package com.continental.game.entity;

import com.continental.game.GameSettings;

public interface GravitationalEntity extends KineticEntity {

    default double getGravitationalForce() {
        return GameSettings.GLOBAL_GRAVITY;
    }

    default void applyGravity() {
        shiftYVel(getGravitationalForce());
    }
}
