package ui.model;

import utils.Vector;

import java.awt.*;

public class BowAndArrow implements DrawableModel {

    private static final double DEFAULT_ARROW_LENGTH = 160;

    private Bow bow;
    private Arrow arrow;
    private Vector direction;
    private Vector startPosition;

    public BowAndArrow() {
        this(DEFAULT_ARROW_LENGTH);
    }

    public BowAndArrow(double arrowLength) {
        arrow = new Arrow(arrowLength);
        bow = new Bow();
    }

    @Override
    public void update(double delta) {

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

    public double getArrowLength() {
        return arrow.getLength();
    }
}
