package ui.model;

import utils.Vector;

import java.awt.*;

public class Rect implements DrawableModel {
    private Vector topLeft;
    private Vector bottomRight;

    public Rect(Vector topLeft, Vector bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Rect(Vector topLeft, double width, double height) {
        this.topLeft = topLeft;
        this.bottomRight = topLeft.add(new Vector(width, height));
    }

    public Vector getTopLeft() {
        return topLeft;
    }

    public Vector getBottomRight() {
        return bottomRight;
    }

    public void setTopLeft(Vector topLeft) {
        this.topLeft = topLeft;
    }

    public void setBottomRight(Vector bottomRight) {
        this.bottomRight = bottomRight;
    }

    public double getWidth() {
        return bottomRight.sub(topLeft).x;
    }

    public double getHeight() {
        return bottomRight.sub(topLeft).y;
    }

    public void setWidth(double width) {
        bottomRight.x = topLeft.x + width;
    }

    public void setHeight(double height) {
        bottomRight.y = topLeft.y + height;
    }

    @Override
    public void update(double delta) {
    }

    @Override
    public void render(Graphics2D g) {
        g.drawRect((int) topLeft.x, (int) topLeft.y, (int) getWidth(), (int) getHeight());
    }

    @Override
    public boolean isInBound(Rect bounds) {
        double tW = this.getWidth();
        double tH = this.getHeight();
        double oW = bounds.getWidth();
        double oH = bounds.getWidth();

        if (oW <= 0 || oH <= 0 || tW <= 0 || tH <= 0) {
            return false;
        }

        double tX = this.topLeft.x;
        double tY = this.topLeft.y;
        double oX = bounds.topLeft.x;
        double oY = bounds.topLeft.y;

        oW += oX;
        oH += oY;
        tW += tX;
        tH += tY;

        //      overflow || intersect
        return ((oW < oX || oW > tX) &&
                (oH < oY || oH > tY) &&
                (tW < tX || tW > oX) &&
                (tH < tY || tH > oY));
    }
}
