package utils;

public class Vector {
    public double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
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
        y = -y;
        // used for chaining.
        return this;
    }
}
