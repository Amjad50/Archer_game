package ui.model;

import utils.Vector;

import java.awt.*;

public class Ball implements DrawableModel {
    private Vector acceleration = new Vector();
    private Vector velocity = new Vector();
    private Vector position = new Vector();
    private double radius;

    public Ball(double r) {
        radius = r;
    }

    public void setVelocity(Vector v) {
        velocity = v;
    }

    public void setPosition(Vector v) {
        position = v;
    }

    public void setAcceleration(Vector v) {
        this.acceleration = v;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Vector getPosition() {
        return position;
    }

    @Override
    public void update(double delta) {
        velocity = velocity.add(acceleration.scale(delta));
        position = position.add(velocity.scale(delta));
    }

    @Override
    public void render(Graphics2D g) {
        g.fillOval((int) (position.x - radius), (int) (position.y - radius), (int) radius * 2, (int) radius * 2);
    }

    @Override
    public boolean isInBound(Rect bounds) {
        return position.x <= bounds.getTopLeft().x + bounds.getWidth() &&
                position.y <= bounds.getTopLeft().y + bounds.getHeight() &&
                position.x >= bounds.getTopLeft().x &&
                position.y >= bounds.getTopLeft().y;
    }
}
