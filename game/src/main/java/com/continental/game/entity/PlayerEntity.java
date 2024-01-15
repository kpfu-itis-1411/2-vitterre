package com.continental.game.entity;

import com.continental.game.GameSettings;
import com.continental.game.ServerGame;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
@Setter
public class PlayerEntity extends Entity implements DirectionalEntity, GravitationalEntity {

    private Direction direction;
    private double xVel;
    private double yVel;

    private final PlayerTeam team;

    public PlayerEntity(ServerGame game, PlayerTeam team, double x, double y, Direction direction) {
        super(game, 1.1, 2, x, y);

        this.team = team;
        this.direction = direction;

        xVel = 0;
        yVel = 0;
    }

    @Override
    public String toString() {
        return "PLAYER";
    }

    @Override
    public void tick() {
        for (val action : getActionSet().getInstantActions()) {
            switch (action) {
                case JUMP -> {
                    if (getYVel() != 0) {
                        break;
                    }
                    shiftYVel(GameSettings.JUMP_VELOCITY);
                }
                case SHOOT -> {
                    if (Direction.LEFT.equals(direction)) {
                        new BulletEntity(getGame(), this, getLeftX(), getTopY() - (4.3 / 8.0),
                                         -GameSettings.BULLET_VELOCITY,
                                         direction
                        );
                    } else if (Direction.RIGHT.equals(direction)) {
                        new BulletEntity(getGame(), this, getRightX(), getTopY() - (4.3 / 8.0),
                                         GameSettings.BULLET_VELOCITY,
                                         direction
                        );
                    } else {
                        System.out.println("Unknown direction type");
                    }
                }
                default -> System.out.println("Unknown instant action");
            }
        }

        getActionSet().getInstantActions().clear();

        for (val action : getActionSet().getLongTermActions()) {
            switch (action) {
                case MOVE_LEFT -> {
                    setDirection(Direction.LEFT);
                    shiftX(-GameSettings.WALK_VELOCITY);
                }
                case MOVE_RIGHT -> {
                    setDirection(Direction.RIGHT);
                    shiftX(GameSettings.WALK_VELOCITY);
                }
                default -> System.out.println("Unknown long term action");
            }
        }

        applyGravity();
        applyVelocity();
    }

    @Override
    public void handleCollision(Entity otherEntity) {
        switch (otherEntity) {
            case PlatformEntity p -> {
                val collisionNormal = getCollisionNormal(otherEntity);

                if (collisionNormal.getX() > 0) {
                    setX(otherEntity.getX() - getWidth());
                    setXVel(0);
                } else if (collisionNormal.getX() < 0) {
                    setX(otherEntity.getX() + otherEntity.getWidth());
                    setXVel(0);
                }

                if (collisionNormal.getY() > 0) {
                    setY(otherEntity.getY() - getHeight());
                    setYVel(0);
                } else if (collisionNormal.getY() < 0) {
                    setY(otherEntity.getY() + otherEntity.getHeight());
                    setYVel(0);
                }
            }
            case BulletEntity b -> {
                if (!b.getPlayerEntity().getTeam().equals(getTeam())) {
                    die();
                }
            }
            default -> System.out.println("Unknown entity type");
        }
    }

    private void die() {
        getGame().removeEntity(getUuid());
    }
}
