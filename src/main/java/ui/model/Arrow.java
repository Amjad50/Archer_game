package ui.model;

import utils.Vector;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Arrow implements DrawableModel {

    private static final double ARROW_SHAFT_LENGTH = 20;

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

    public double getLength() {
        return length;
    }

    @Override
    public void update(double delta) {
        velocity = velocity.add(acceleration.scale(delta));
        position = position.add(velocity.scale(delta));
    }

    @Override
    public void render(Graphics2D g) {
        Vector arrowShaft = new Vector(-1, 1).remagnitude(ARROW_SHAFT_LENGTH);
        Vector topArrowShaft = position.add(arrowShaft);
        Vector bottomArrowShaft = position.add(arrowShaft.flipY());
        // save the transformation to be restored later
        AffineTransform tmp = g.getTransform();
        // rotate the canvas around the tail of the arrow
        g.rotate(velocity.angleRad(), position.x, position.y);
        // draw the arrow pointing the left of position (it will rotated based on the angle of velocity)
        g.drawLine((int)position.x, (int)position.y, (int) (position.x - length), (int)position.y);
        g.drawLine((int)position.x, (int)position.y, (int)topArrowShaft.x, (int)topArrowShaft.y);
        g.drawLine((int)position.x, (int)position.y, (int)bottomArrowShaft.x, (int)bottomArrowShaft.y);
        // restore the saved transformation
        g.setTransform(tmp);
    }

    @Override
    public boolean isInBound(Vector start, double w, double h) {
        return position.x <= start.x + w && position.y <= start.y + h && position.x >= start.x && position.y >= start.y;
    }
}
