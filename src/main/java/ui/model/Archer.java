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

    public void draw(Vector power) {
        bowAndArrow.setDirectionNormalized(power);
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
            bowAndArrow.setStartPosition(wist.add(new Vector(headRadius, 0).scale((flipped) ? -1 : 1)));

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
}
