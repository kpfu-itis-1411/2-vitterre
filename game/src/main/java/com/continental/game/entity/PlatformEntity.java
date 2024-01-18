package com.continental.game.entity;

import com.continental.game.ServerGame;

public class PlatformEntity extends Entity {

    public PlatformEntity(ServerGame game, double width, double height, double x, double y) {
        super(game, width, height, x, y);
    }

    @Override
    public void tick() {
    }

    @Override
    public void handleCollision(Entity otherEntity) {
    }
}
