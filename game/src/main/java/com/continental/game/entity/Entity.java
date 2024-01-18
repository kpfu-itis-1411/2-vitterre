package com.continental.game.entity;

import com.continental.game.ActionSet;
import com.continental.game.ServerGame;
import com.continental.math.Vector2D;
import lombok.Data;
import lombok.val;

import java.io.Serializable;
import java.util.UUID;

@Data
public abstract class Entity implements Serializable {

    private transient final ServerGame game;
    private final UUID uuid;

    private final double width;
    private final double height;

    private double x;
    private double y;

    private ActionSet actionSet;

    public Entity(ServerGame game, double width, double height, double x, double y) {
        this.game = game;
        this.uuid = UUID.randomUUID();
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        actionSet = new ActionSet();

        game.addEntity(this);
    }

    public double shiftX(double shiftFactor) {
        return x += shiftFactor;
    }

    public double shiftY(double shiftFactor) {
        return y += shiftFactor;
    }

    public double getLeftX() {
        return getX();
    }

    public double getBottomY() {
        return getY();
    }

    public double getRightX() {
        return getLeftX() + getWidth();
    }

    public double getTopY() {
        return getBottomY() + getHeight();
    }

    public double getCenterX() {
        return getLeftX() + getWidth() / 2;
    }

    public double getCenterY() {
        return getBottomY() + getHeight() / 2;
    }

    public abstract void tick();

    public abstract void handleCollision(Entity otherEntity);

    public boolean isColliding(final Entity otherEntity) {
        // Uses AABB collision
        return getX() < otherEntity.getX() + otherEntity.getWidth() && getX() + getWidth() > otherEntity.getX()
                && getY() < otherEntity.getY() + otherEntity.getHeight()
                && getY() + getHeight() > otherEntity.getY()
                && this != otherEntity;
    }

    public Vector2D getCollisionNormal(final Entity otherEntity) {
        val xOverlap = Math.min(this.getX() + this.getWidth(), otherEntity.getX() + otherEntity.getWidth())
                - Math.max(this.getX(), otherEntity.getX());

        val yOverlap = Math.min(this.getY() + this.getHeight(),
                                otherEntity.getY() + otherEntity.getHeight()
        ) - Math.max(this.getY(), otherEntity.getY());

        if (xOverlap > yOverlap) {
            return new Vector2D(0, Math.signum(otherEntity.getY() - this.getY()));
        } else if (xOverlap < yOverlap) {
            return new Vector2D(Math.signum(otherEntity.getX() - this.getX()), 0);
        } else {
            return new Vector2D(Math.signum(otherEntity.getX() - this.getY()),
                                Math.signum(otherEntity.getY() - this.getY())
            );
        }
    }
}
