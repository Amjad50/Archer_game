package model;

import javafx.scene.canvas.GraphicsContext;
import utils.Vector;

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
        velocity = velocity.add(acceleration.scale(delta * 30));
        position = position.add(velocity.scale(delta * 30));
    }

    @Override
    public void render(GraphicsContext gfx) {
        gfx.save();
        gfx.fillOval(position.x - radius, position.y - radius, radius * 2, radius * 2);
        gfx.restore();
    }

    @Override
    public boolean isInBound(double w, double h) {
        return position.x <= w && position.y <= h && position.x >= 0 && position.y >= 0;
    }

}
