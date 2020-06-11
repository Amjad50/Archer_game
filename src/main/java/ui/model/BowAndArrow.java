package ui.model;

import utils.Vector;

import java.awt.*;

public class BowAndArrow implements DrawableModel {

    public static final double DEFAULT_ARROW_LENGTH = 130;
    public static double DEFAULT_BOW_HEIGHT = Bow.DEFAULT_HEIGHT;

    private Bow bow;
    private Arrow arrow;
    private Vector direction = new Vector();
    private Vector startPosition = new Vector();

    public BowAndArrow() {
        this(DEFAULT_ARROW_LENGTH, Bow.DEFAULT_HEIGHT);
    }

    public BowAndArrow(double arrowLength, double bowHeight) {
        arrow = new Arrow(arrowLength);
        bow = new Bow(bowHeight);
        bow.setMaxWholeWidth(arrowLength - 20);
    }

    @Override
    public void update(double delta) {
        bow.update(delta);
    }

    @Override
    public void render(Graphics2D g) {
        bow.render(g);

        Vector newHead = direction.sub(startPosition).scale(-1).add(direction.remagnitude(arrow.getLength()));
        arrow.setPosition(newHead);
        arrow.setVelocity(direction);

        arrow.render(g);
    }

    @Override
    public boolean isInBound(double w, double h) {
        return false;
    }

    public void setDirectionNormalized(Vector direction) {
        this.direction = bow.setDirectionNormalized(direction);
    }

    public void setStartPosition(Vector position) {
        startPosition = position;
        bow.setStartPosition(startPosition);
    }

    public Vector getBowEndPosition() {
        return bow.getEndPosition();
    }

    public Vector getYarnEndPosition() {
        return bow.getYarnEndPosition();
    }

    public double getArrowLength() {
        return arrow.getLength();
    }
}
