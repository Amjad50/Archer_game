package ui.model.stickman;

import ui.model.DrawableModel;
import utils.Vector;

import java.awt.*;
import java.util.ArrayList;

public class Arm implements DrawableModel {
    private ArrayList<ArmLegSegment> segments = new ArrayList<>();
    private Vector base = new Vector();

    public void follow(Vector target) {
        if (segments.size() == 0)
            return;

        ArmLegSegment last = segments.get(segments.size() - 1);

        // Follow from the tip to the end of the arm
        last.follow(target);
        if (segments.size() > 1)
            for (int i = segments.size() - 2; i >= 0; i--) {
                segments.get(i).follow(segments.get(i + 1).getStart());
            }

        // Put the arm at the base (just moving)
        segments.get(0).setStart(base).updateEnd();
        if (segments.size() > 1)
            for (int i = 1; i < segments.size(); i++) {
                segments.get(i).setStart(segments.get(i - 1).getEnd()).updateEnd();
            }

    }

    public void addArm(double length) {
        segments.add(new ArmLegSegment(length));
    }

    public void setBase(Vector base) {
        this.base = base.copy();
    }

    @Override
    public void update(double delta) {
    }

    @Override
    public void render(Graphics2D g) {
        for (ArmLegSegment segment : segments) {
            segment.render(g);
        }
    }

    @Override
    public boolean isInBound(double w, double h) {
        return false;
    }

    public Vector getTipPosition() {
        if(segments.size() == 0)
            return new Vector();
        else
            // The end position of the last arm segment
            return segments.get(segments.size() - 1).getEnd();
    }
}
