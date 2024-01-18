package com.continental.game.entity;

public interface KineticEntity {

    void setX(double newX);

    void setY(double newY);

    void setXVel(double newXVel);

    void setYVel(double newYVel);

    double getX();

    double getXVel();

    double getY();

    double getYVel();

    default double shiftX(double dX) {
        setX(getX() + dX);
        return getX();
    }

    default double shiftXVel(final double dXVel) {
        setXVel(getXVel() + dXVel);
        return getXVel();
    }

    default double shiftY(final double dY) {
        setY(getY() + dY);
        return getY();
    }

    default double shiftYVel(final double dYVel) {
        setYVel(getYVel() + dYVel);
        return getYVel();
    }

    default void applyVelocity() {
        shiftX(getXVel());
        shiftY(getYVel());
    }
}
