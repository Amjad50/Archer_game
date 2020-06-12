package ui.model;

import utils.Helpers;
import utils.Vector;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bow implements DrawableModel {

    private static final int MAX_BOW_DRAW = 100;
    private static final int MIN_BOW_WIDTH = 10;
    private static final int MAX_BOW_WIDTH = 40;
    static final int DEFAULT_HEIGHT = 120;

    private double height;
    private double width;
    private double maxWholeWidth; // THe maximum possible width of the bow
    private double maxDraw;

    private Vector direction = new Vector();
    private Vector startPosition = new Vector();

    public Bow() {
        this(DEFAULT_HEIGHT);
    }

    public Bow(double height) {
        this.height = height;
        this.maxDraw = MAX_BOW_DRAW;
        this.maxWholeWidth = maxDraw + MAX_BOW_WIDTH;
    }

    private void updateWidth() {
        width = Helpers.map(this.direction.magnitude(), 0, maxDraw, MIN_BOW_WIDTH, MAX_BOW_WIDTH);
    }

    @Override
    public void update(double delta) {
        updateWidth();
    }

    @Override
    public void render(Graphics2D g) {
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
        g.drawArc((int) (startPosition.x - width), (int) (startPosition.y - height / 2), (int) width * 2, (int) height, 90, -180);
        // restore stroke
        g.setStroke(old_stroke);


        // draw the two lines, from the top and bottom of the bow to the current mouse location
        g.drawLine((int) startPosition.x, (int) (startPosition.y - height / 2), (int) (startPosition.x - direction.magnitude()), (int) startPosition.y);
        g.drawLine((int) startPosition.x, (int) (startPosition.y + height / 2), (int) (startPosition.x - direction.magnitude()), (int) startPosition.y);

        g.setTransform(old_transform);
    }

    @Override
    public boolean isInBound(Vector start, double w, double h) {
        return false;
    }

    public Vector setDirectionNormalized(Vector direction) {
        if (direction.magnitude() > maxDraw)
            this.direction = direction.remagnitude(maxDraw);
        else if (direction.magnitude() == 0)
            // fix problem of NaN vector
            // FIXME: on flipped archer, if its NaN vector, it will point the bow to the right
            //  instead of to the left which is correct <Vector(-1, 0)>
            this.direction = new Vector(1, 0);
        else
            this.direction = direction;

        return this.direction;
    }

    public void setStartPosition(Vector startPosition) {
        this.startPosition = startPosition;
    }

    public void setMaxWholeWidth(double maxWholeWidth) {
        this.maxWholeWidth = maxWholeWidth;
        this.maxDraw = maxWholeWidth - MAX_BOW_WIDTH;
    }

    public Vector getEndPosition() {
        updateWidth();
        return startPosition.add(direction.remagnitude(width));
    }

    public Vector getYarnEndPosition() {
        if (direction.magnitude() == 0)
            return new Vector();
        return startPosition.sub(new Vector(Math.cos(direction.angleRad()), Math.sin(direction.angleRad())).scale(direction.magnitude()));
    }
}
