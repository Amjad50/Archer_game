package ui.model;

import utils.Vector;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class LocatorArrow implements DrawableModel {

    private static final double DEFAULT_RADIUS = 30;
    private static final double[] ROTATION_ANGLES = {0, 2 * Math.PI / 3, -2 * Math.PI / 3};

    private Vector position = new Vector();
    private Vector target = null;
    private Vector direction;
    private double radius;

    private Vector[] vectorPoints;

    public LocatorArrow() {
        this(DEFAULT_RADIUS);
    }

    public LocatorArrow(double radius) {
        this.radius = radius;

        vectorPoints = new Vector[3];
    }

    @Override
    public void update(double delta) {
        if (target != null) {
            direction = target.sub(position);

            // build triangle (points A, B, C)
            // https://stackoverflow.com/questions/11449856/draw-a-equilateral-triangle-given-the-center

            // center is `this.position`
            for (int i = 0; i < vectorPoints.length; i++) {
                vectorPoints[i] = new Vector(
                        position.x + Math.cos(ROTATION_ANGLES[i]) * radius,
                        position.y - Math.sin(ROTATION_ANGLES[i]) * radius
                );
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        AffineTransform old_transformation = g.getTransform();

        if (target != null) {
            g.rotate(direction.angleRad(), position.x, position.y);
            int[] xPoints = new int[vectorPoints.length];
            int[] yPoints = new int[vectorPoints.length];
            for (int i = 0; i < vectorPoints.length; i++) {
                xPoints[i] = (int) vectorPoints[i].x;
                yPoints[i] = (int) vectorPoints[i].y;
            }
            g.drawOval((int) vectorPoints[0].x - 10, (int) vectorPoints[0].y - 10, 20, 20);

            g.fillPolygon(xPoints, yPoints, vectorPoints.length);
        }

        g.setTransform(old_transformation);
    }

    @Override
    public boolean isInBound(Vector start, double w, double h) {
        return false;
    }

    public void setTarget(Vector target) {
        this.target = target;
    }

    public Vector getDirection() {
        return direction;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public double getRadius() {
        return radius;
    }
}
