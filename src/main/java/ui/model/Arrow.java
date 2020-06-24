package ui.model;

import utils.Vector;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Arrow implements DrawableModel {

    private static final double ARROW_SHAFT_LENGTH = 10;

    protected Vector acceleration = new Vector();
    protected Vector velocity = new Vector();
    protected Vector position = new Vector();
    private Vector topArrowShaft = new Vector();
    private Vector bottomArrowShaft = new Vector();
    private double length;

    public Arrow(double length) {
        this.length = length;
    }

    public void setVelocity(Vector v) {
        velocity = v;
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

        update_arrow_shaft();
    }

    protected void update_arrow_shaft() {
        Vector arrowShaft = new Vector(-1, 1).remagnitude(ARROW_SHAFT_LENGTH);
        topArrowShaft = position.add(arrowShaft);
        bottomArrowShaft = position.add(arrowShaft.flipY());
    }

    @Override
    public void render(Graphics2D g) {
        // save the transformation to be restored later
        AffineTransform tmp = g.getTransform();
        // rotate the canvas around the tail of the arrow
        g.rotate(velocity.angleRad(), position.x, position.y);
        // draw the arrow pointing the left of position (it will rotated based on the angle of velocity)
        g.drawLine((int) position.x, (int) position.y, (int) (position.x - length), (int) position.y);
        g.drawLine((int) position.x, (int) position.y, (int) topArrowShaft.x, (int) topArrowShaft.y);
        g.drawLine((int) position.x, (int) position.y, (int) bottomArrowShaft.x, (int) bottomArrowShaft.y);
        // restore the saved transformation
        g.setTransform(tmp);
    }

    @Override
    public boolean isInBound(Rect bounds) {
        return position.x <= bounds.getTopLeft().x + bounds.getWidth() &&
                position.y <= bounds.getTopLeft().y + bounds.getHeight() &&
                position.x >= bounds.getTopLeft().x &&
                position.y >= bounds.getTopLeft().y;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector v) {
        position = v;
    }
}
