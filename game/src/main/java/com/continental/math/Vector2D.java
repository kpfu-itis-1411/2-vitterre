package com.continental.math;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Vector2D {

    private double x;
    private double y;

    public void setX(final double x) {
        this.x = x;
    }

    public double incX(final double dX) {
        return this.x += dX;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public double incY(final double dY) {
        return this.y += dY;
    }
}
