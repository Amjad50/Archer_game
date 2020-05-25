package ui.model;

import utils.Vector;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Arrow implements DrawableModel {
    private Vector acceleration = new Vector();
    private Vector velocity = new Vector();
    private Vector position = new Vector();
    private double length;

    public Arrow(double length) {
        this.length = length;
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
        // save the transformation to be restored later
        AffineTransform tmp = g.getTransform();
        // rotate the canvas around the tail of the arrow
        g.rotate(velocity.angleRad(), position.x, position.y);
        // draw the arrow pointing the left of position (it will rotated based on the angle of velocity)
        g.drawLine((int)position.x, (int)position.y, (int) (position.x - length), (int)position.y);
        // restore the saved transformation
        g.setTransform(tmp);
    }


    @Override
    public boolean isInBound(double w, double h) {
        return position.x <= w && position.y <= h && position.x >= 0 && position.y >= 0;
    }
}
