package ui.model.stickman;

import ui.model.DrawableModel;
import utils.Pair;
import utils.Vector;

import java.awt.*;

public class Stickman implements DrawableModel {

    // Measurements
    protected static final int HEAD_RADIUS = 1;
    protected static final int ARM_LEG_SECTION_LENGTH = 3;
    protected static final int TORSO_HEIGHT = 4;
    protected static final int BODY_HEIGHT = HEAD_RADIUS * 2 + TORSO_HEIGHT + ARM_LEG_SECTION_LENGTH * 2;
    protected static final double BOW_HEIGHT = 9;

    private Vector groundPosition = new Vector();

    // parts
    private double bodyWidth;
    protected double bodyHeight;
    protected double headRadius;

    protected Vector wist = new Vector();
    private Vector leftLeg = new Vector();
    private Vector rightLeg = new Vector();
    private Vector shoulderCenter = new Vector();
    private HumanArm leftArm;
    private HumanArm rightArm;


    public Stickman(double height) {
        this.bodyHeight = height;
        rightArm = new HumanArm(bodyHeight / BODY_HEIGHT * ARM_LEG_SECTION_LENGTH);
        leftArm = new HumanArm(bodyHeight / BODY_HEIGHT * ARM_LEG_SECTION_LENGTH);

        // build the body for the first time
        update(1);
    }

    // FIXME: remove all these hardcoded values
    @Override
    public void update(double delta) {
        headRadius = (int) (bodyHeight / BODY_HEIGHT * HEAD_RADIUS);
        bodyWidth = (int) (headRadius * 2);

        Vector shouldersWistOffset = new Vector(0, bodyHeight / BODY_HEIGHT * TORSO_HEIGHT);
        Vector legWistOffset = new Vector(0, bodyHeight / BODY_HEIGHT * ARM_LEG_SECTION_LENGTH * 2);
        Vector armShoulderOffset = legWistOffset.swipeXY();
        Vector halfBodyOffsetWidth = new Vector(bodyWidth / 2.0, 0);

        wist = groundPosition.sub(legWistOffset);

        rightLeg = groundPosition.add(halfBodyOffsetWidth.scale(2));
        leftLeg = groundPosition.sub(halfBodyOffsetWidth.scale(2));

        shoulderCenter = wist.sub(shouldersWistOffset);

        rightArm.setBase(shoulderCenter);
        leftArm.setBase(shoulderCenter);
    }

    @Override
    public void render(Graphics2D g) {
        Stroke old_stroke = g.getStroke();

        // The stroke is 1/5 of the head width
        g.setStroke(new BasicStroke((float) (headRadius / 2.5), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g.drawLine((int) rightLeg.x, (int) rightLeg.y, (int) wist.x, (int) wist.y);
        g.drawLine((int) leftLeg.x, (int) leftLeg.y, (int) wist.x, (int) wist.y);

        g.drawLine((int) wist.x, (int) wist.y, (int) shoulderCenter.x, (int) shoulderCenter.y);
        g.drawOval((int) (shoulderCenter.x - headRadius), (int) (shoulderCenter.y - headRadius * 2),
                (int) headRadius * 2, (int) headRadius * 2);

        rightArm.render(g);
        leftArm.render(g);

        g.setStroke(old_stroke);
    }

    // TODO: implement properly
    @Override
    public boolean isInBound(Vector start, double w, double h) {
        return false;
    }

    public void setGroundPosition(Vector position) {
        groundPosition = position;
    }

    public void setLeftHandPosition(Vector v) {
        leftArm.follow(v);
    }

    public void setRightHandPosition(Vector v) {
        rightArm.follow(v);
    }

    public Pair<Vector> getHandsPosition() {
        return new Pair<>(rightArm.getTipPosition(), leftArm.getTipPosition());
    }
}
