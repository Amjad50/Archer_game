package ui.model;

import ui.model.stickman.Stickman;
import utils.Vector;

import java.awt.*;

public class Archer extends Stickman {

    private BowAndArrow bowAndArrow;
    private boolean first_time = true;
    private boolean flipped = false;

    public Archer(double height) {
        this(height, false);
    }

    public Archer(double height, boolean flipped) {
        super(height);

        this.bowAndArrow = new BowAndArrow(BowAndArrow.DEFAULT_ARROW_LENGTH, height * BOW_HEIGHT / BODY_HEIGHT);
        this.flipped = flipped;

        resetBowAndArrow();
    }

    public void draw(Vector direction) {
        bowAndArrow.setDirectionNormalized(direction);
    }

    public void resetBowAndArrow() {
        bowAndArrow.setDirectionNormalized(new Vector((flipped) ? -1 : 1, 0));
    }

    @Override
    public void update(double delta) {
        super.update(delta);

        // running this everytime makes the arm laggy, one time is good (not perfect).
        if (first_time) {
            Vector farLeft = new Vector(-bodyHeight, 0);
            setRightHandPosition(farLeft);
            setLeftHandPosition(farLeft);
            first_time = false;
        }

        if (bowAndArrow != null) {
            bowAndArrow.update(delta);

            Vector torso = shoulderCenter.sub(wist).scale(getPowerPercentage());
            Vector start = wist.add(torso);
            start = start.add(new Vector(headRadius, 0).scale((flipped) ? -1 : 1));

            // use the power percentage to know how long should the arm stretch
            double newMag = rightArm.getLength() / 2 * getPowerPercentage();


            bowAndArrow.setStartPosition(start.add(bowAndArrow.getDirection().remagnitude(newMag)));

            setRightHandPosition(bowAndArrow.getBowEndPosition());
            setLeftHandPosition(bowAndArrow.getYarnEndPosition());
        }
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        bowAndArrow.render(g);
        Vector end = bowAndArrow.getYarnEndPosition();
        g.fillOval((int) end.x - 5, (int) end.y - 5, 10, 10);
    }

    public Arrow releaseArrow() {
        Arrow arrow = new Arrow(bowAndArrow.getArrowLength());
        arrow.setPosition(bowAndArrow.getYarnEndPosition());
        arrow.setVelocity(bowAndArrow.getDirection());

        return arrow;
    }

    public double getPowerPercentage() {
        return bowAndArrow.getDirection().magnitude() / bowAndArrow.getMaxDraw();
    }
}
