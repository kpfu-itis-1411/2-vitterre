package com.continental.game.entity;

import com.continental.game.GameSettings;
import com.continental.game.ServerGame;
import lombok.Getter;

@Getter
public class BulletEntity extends Entity implements DirectionalEntity {

    private final PlayerEntity playerEntity;
    private int age = 0;
    private final double velocity;

    public BulletEntity(ServerGame game,
                        PlayerEntity playerEntity,
                        double x,
                        double y,
                        double velocity,
                        Direction direction
    ) {
        super(game, 2.0 / 8.0, 0.1, x, y);

        this.playerEntity = playerEntity;

        switch (direction) {
            case LEFT -> setX(x - getWidth());
            case RIGHT -> setX(x);
        }

        setY(y - getHeight());

        this.age = 0;
        this.velocity = velocity;
    }

    @Override
    public Direction getDirection() {
        return velocity > 0 ? Direction.RIGHT : Direction.LEFT;
    }

    @Override
    public void setDirection(Direction direction) {
    }

    @Override
    public void tick() {
        if (age >= GameSettings.BULLET_TIME_TO_LIVE) {
            getGame().removeEntity(getUuid());
        }

        shiftX(velocity);
        age++;
    }

    @Override
    public void handleCollision(Entity otherEntity) {
        if (otherEntity instanceof PlatformEntity) {
            getGame().removeEntity(getUuid());
        }
    }
}
