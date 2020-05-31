package ui.model;

import utils.Helpers;
import utils.Vector;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bow implements DrawableModel {

    private static final int MAX_BOW_DRAW = 100;
    private static final int MIN_BOW_WIDTH = 10;
    private static final int MAX_BOW_WIDTH = 40;
    private static final int DEFAULT_HEIGHT = 120;

    private Vector direction = new Vector();
    private Vector startPosition = new Vector();


    @Override
    public void update(double delta) {
    }

    @Override
    public void render(Graphics2D g) {
        int bowWidth = (int) Helpers.map(this.direction.magnitude(), 0, MAX_BOW_DRAW, MIN_BOW_WIDTH, MAX_BOW_WIDTH),
                bowHeight = DEFAULT_HEIGHT;
        // save the old transformation to be restored later
        AffineTransform old_transform = g.getTransform();
        // rotate the view around mouseStart with the angle of the arrowDirection
        // the bow and the arrow, would be drawn in horizontal manner
        // but when we rotate the view, they will be rotated
        g.rotate(direction.angleRad(), startPosition.x, startPosition.y);

        // save the old stroke width and style
        Stroke old_stroke = g.getStroke();
        // set the stroke to 5 to draw the bow material
        g.setStroke(new BasicStroke(5));
        // draw an arc half a circle from the top of the circle to the bottom, covering the right side of the circle
        g.drawArc((int) (startPosition.x - bowWidth), (int) startPosition.y - bowHeight / 2, bowWidth * 2, bowHeight, 90, -180);
        // restore stroke
        g.setStroke(old_stroke);


        // draw the two lines, from the top and bottom of the bow to the current mouse location
        g.drawLine((int) startPosition.x, (int) startPosition.y - bowHeight / 2, (int) (startPosition.x - direction.magnitude()), (int) startPosition.y);
        g.drawLine((int) startPosition.x, (int) startPosition.y + bowHeight / 2, (int) (startPosition.x - direction.magnitude()), (int) startPosition.y);

        g.setTransform(old_transform);
    }

    @Override
    public boolean isInBound(double w, double h) {
        return false;
    }

    public Vector setDirectionNormalized(Vector direction) {
        if (direction.magnitude() > MAX_BOW_DRAW)
            this.direction = direction.remagnitude(MAX_BOW_DRAW);
        else
            this.direction = direction;

        return this.direction;
    }

    public void setStartPosition(Vector startPosition) {
        this.startPosition = startPosition;
    }
}
