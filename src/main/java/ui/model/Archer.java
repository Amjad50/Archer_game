package ui.model;

import ui.model.stickman.Stickman;
import utils.Vector;

import java.awt.*;

public class Archer extends Stickman {

    private BowAndArrow bowAndArrow;

    public Archer(double height) {
        super(height);

        bowAndArrow = new BowAndArrow(BowAndArrow.DEFAULT_ARROW_LENGTH, height * BOW_HEIGHT / BODY_HEIGHT);

        resetBowAndArrow();
    }

    public void draw(Vector power) {
        bowAndArrow.setDirectionNormalized(power);
    }

    public void resetBowAndArrow() {
        bowAndArrow.setDirectionNormalized(new Vector(1, 0));
    }

    @Override
    public void update(double delta) {
        super.update(delta);

        Vector farLeft = new Vector(-bodyHeight, 0);
        setRightHandPosition(farLeft);
        setLeftHandPosition(farLeft);

        if (bowAndArrow != null) {
            bowAndArrow.update(delta);
            bowAndArrow.setStartPosition(wist.add(new Vector(headRadius, 0)));

            setRightHandPosition(bowAndArrow.getBowEndPosition());
            setLeftHandPosition(bowAndArrow.getYarnEndPosition());
        }
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        bowAndArrow.render(g);
        Vector end = bowAndArrow.getYarnEndPosition();
        g.fillOval((int)end.x - 5, (int)end.y - 5, 10, 10);
    }
}
