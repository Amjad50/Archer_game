package ui.model.stickman;

import utils.Vector;

import java.awt.*;

public class ArmLegSegment {
    private Vector end = new Vector();
    private Vector start = new Vector();
    private double length;
    private double angle;

    public ArmLegSegment(double length) {
        this.length = length;
    }

    public void follow(Vector target) {
        Vector result = target.sub(start);
        angle = result.angleRad();
        start = target.add(result.remagnitude(length).scale(-1));

        end = target.copy();
    }

    public Vector getEnd() {
        return end;
    }

    public ArmLegSegment setStart(Vector v) {
        start = v.copy();
        return this;
    }

    // TODO: should it implement DrawableModel?
    public void render(Graphics2D g) {
        g.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
    }

    public Vector getStart() {
        return start;
    }

    public void updateEnd() {
        end = start.add(new Vector(Math.cos(angle), Math.sin(angle)).scale(length));
    }
}
