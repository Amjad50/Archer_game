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

    @Override
    public void update(double delta) {
        velocity = velocity.add(acceleration.scale(delta));
        position = position.add(velocity.scale(delta));
    }

    @Override
    public void render(Graphics2D g) {
        g.fillOval((int)(position.x - radius), (int)(position.y - radius), (int)radius * 2, (int)radius * 2);
    }


    @Override
    public boolean isInBound(double w, double h) {
        return position.x <= w && position.y <= h && position.x >= 0 && position.y >= 0;
    }
}
