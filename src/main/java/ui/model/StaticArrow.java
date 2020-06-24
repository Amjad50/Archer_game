package ui.model;

import java.awt.*;

public class StaticArrow extends Arrow {
    public StaticArrow(double length) {
        super(length);
    }

    @Override
    public void update(double delta) {
        velocity = velocity.add(acceleration.scale(delta));
        position = position.add(velocity.scale(delta));
    }

    @Override
    public void render(Graphics2D g) {
        update_arrow_shaft();
        super.render(g);
    }
}
