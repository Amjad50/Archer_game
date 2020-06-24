package ui.model;

import utils.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;

public class Blood implements DrawableModel {

    private Vector gravity = new Vector();
    private ArrayList<Ball> bloodBalls = new ArrayList<>();
    private Vector position;

    public Blood(Vector position) {
        this.position = position.copy();
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            Ball ball = new Ball(10 + r.nextInt(10));
            ball.setPosition(position.copy());
            ball.setVelocity(new Vector(r.nextInt(10), r.nextInt(10)));
            bloodBalls.add(ball);
        }
    }

    @Override
    public void update(double delta) {
        Ball ball;
        for (Iterator<Ball> iterator = bloodBalls.iterator(); iterator.hasNext(); ) {
            ball = iterator.next();
            ball.setAcceleration(gravity);

            ball.setRadius(ball.getRadius() * 0.99);

            if (ball.getRadius() < 1)
                iterator.remove();

            ball.update(delta);
        }

        Random r = new Random();
        if(r.nextInt(300) == 0) {
            ball = new Ball(10 + r.nextInt(10));
            ball.setPosition(position.copy());
            ball.setVelocity(new Vector(r.nextInt(2), r.nextInt(2)));
            bloodBalls.add(ball);
        }
    }

    @Override
    public void render(Graphics2D g) {
        Paint old_paint = g.getPaint();

        g.setPaint(Color.RED);
        try {
            for (Ball ball : bloodBalls) {
                ball.render(g);
            }
        }catch (ConcurrentModificationException e) {
            // FIXME: this is due to running two threads, one for render and one for update
        }

        g.setPaint(old_paint);

    }

    @Override
    public boolean isInBound(Rect bounds) {
        return false;
    }

    public void setGravity(Vector gravity) {
        this.gravity = gravity;
    }
}
