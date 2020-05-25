package ui;

import ui.model.Arrow;
import utils.Helpers;
import utils.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;

public class DrawingCanvas extends JPanel implements MouseListener, MouseMotionListener {

    private static final int MAX_BOW_DRAW = 100;
    private static final int MIN_BOW_WIDTH = 10;
    private static final int MAX_BOW_WIDTH = 60;

    private long fps = 0;
    private Vector mouseStart = new Vector();
    private Vector mouseCurrent = new Vector();
    private Vector arrowDirection = new Vector();
    private boolean dragging = false;

    private Arrow arrow;

    public DrawingCanvas() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void render(double delta, long fps) {
        prepareRender(delta);
        this.fps = fps;
        repaint();
    }

    private void prepareRender(double delta) {
        if (dragging) {
            arrowDirection = mouseStart.sub(mouseCurrent);
            if (arrowDirection.magnitude() > MAX_BOW_DRAW)
                arrowDirection = arrowDirection.remagnitude(MAX_BOW_DRAW);
        }
        if (arrow != null && arrow.isInBound(getWidth(), getHeight()))
            arrow.update(delta);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        innerRender(g2);

        g2.dispose();
    }

    private void innerRender(Graphics2D g) {
        drawFPS(g);
        drawLineAndAngle(g);
        if (arrow != null)
            arrow.render(g);
    }

    private void drawFPS(Graphics2D g) {
        g.drawString(String.valueOf(fps), 0, 10);
    }

    private void drawLineAndAngle(Graphics2D g) {
        if (!dragging)
            return;

        int bowWidth = (int) Helpers.map(arrowDirection.magnitude(), 0, MAX_BOW_DRAW, MIN_BOW_WIDTH, MAX_BOW_WIDTH),
                bowHeight = 120;

        // write the angle of the arrow
        g.drawString(String.valueOf(arrowDirection.flipY().angleDeg()), 0, 30);

        // save the old transformation to be restored later
        AffineTransform old_transform = g.getTransform();
        // rotate the view around mouseStart with the angle of the arrowDirection
        // the bow and the arrow, would be drawn in horizontal manner
        // but when we rotate the view, they will be rotated
        g.rotate(arrowDirection.angleRad(), mouseStart.x, mouseStart.y);

        // save the old stroke width and style
        Stroke old_stroke = g.getStroke();
        // set the stroke to 5 to draw the bow material
        g.setStroke(new BasicStroke(5));
        // draw an arc half a circle from the top of the circle to the bottom, covering the right side of the circle
        g.drawArc((int) (mouseStart.x - bowWidth), (int) mouseStart.y - bowHeight / 2, bowWidth * 2, bowHeight, 90, -180);
        // restore stroke
        g.setStroke(old_stroke);

        // draw the two lines, from the top and bottom of the bow to the current mouse location
        g.drawLine((int) mouseStart.x, (int) mouseStart.y - bowHeight / 2, (int) (mouseStart.x - arrowDirection.magnitude()), (int) mouseStart.y);
        g.drawLine((int) mouseStart.x, (int) mouseStart.y + bowHeight / 2, (int) (mouseStart.x - arrowDirection.magnitude()), (int) mouseStart.y);

        // TODO: remove/change the offset of the arrow
        // draw the arrow
        // the length of the arrow is from the rightmost of the bow until the mouse location (plus an offset to make
        // the arrow larger a bit to exceed the bow head)
        Arrow arrow = new Arrow(MAX_BOW_DRAW + MAX_BOW_WIDTH + 20);
        // the head is at the rightmost of the bow (plus an offset to make the arrow beyond that by a little)
        arrow.setPosition(new Vector(mouseStart.x + arrow.getLength() - arrowDirection.magnitude(), mouseStart.y));
        // pointing to the right
        arrow.setVelocity(new Vector(1, 0));

        // render the built arrow object
        arrow.render(g);
        // restore the transformation
        g.setTransform(old_transform);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        dragging = true;
        mouseStart.setValue(mouseEvent.getPoint());
        mouseCurrent.setValue(mouseEvent.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        dragging = false;
        arrow = new Arrow(50);
        arrow.setPosition(mouseStart);
        arrow.setVelocity(arrowDirection.scale(0.3));
        arrow.setAcceleration(new Vector(0, 9.8).scale(0.1));
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        mouseCurrent.setValue(mouseEvent.getPoint());
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    }
}
