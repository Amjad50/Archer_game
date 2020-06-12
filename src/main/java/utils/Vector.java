package utils;

import java.awt.*;

public class Vector {
    public double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector() {
        this(0, 0);
    }

    public Vector(Point point) {
        x = point.x;
        y = point.y;
    }

    public void setValue(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }

    public Vector sub(Vector other) {
        return new Vector(x - other.x, y - other.y);
    }

    public double angleRad() {
        return Math.atan(y / x) + (x < 0 ? Math.PI : 0);
    }

    public double angleDeg() {
        return angleRad() / Math.PI * 180;
    }

    public Vector flipY() {
        return new Vector(x, -y);
    }

    public Vector swipeXY() {
        return new Vector(y, x);
    }

    public void setValue(Point point) {
        x = point.x;
        y = point.y;
    }

    public void setValue(Vector other) {
        x = other.x;
        y = other.y;
    }

    public Vector scale(double v) {
        return new Vector(x * v, y * v);
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector remagnitude(double magnitude) {
        double old_mag = magnitude();
        if (old_mag == 0)
            return this.copy();
        return this.scale(magnitude / old_mag);
    }

    public Vector copy() {
        return new Vector(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }
}
