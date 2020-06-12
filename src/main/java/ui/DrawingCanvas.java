package ui;

import ui.model.Archer;
import ui.model.Arrow;
import ui.model.stickman.Arm;
import utils.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DrawingCanvas extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    private long fps = 0;
    private Vector mouseStart = new Vector();
    private Vector mouseCurrent = new Vector();
    private Vector arrowDirection = new Vector();
    private boolean dragging = false;

    private Vector offset = new Vector();
    private double groundHeight;

    private ArrayList<Arrow> arrows = new ArrayList<>();
    private Archer archer;
    private Arm arm = new Arm();

    public DrawingCanvas() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        groundHeight = 100;
    }

    public void update(double delta, long fps) {
        this.fps = fps;
        update(delta);
    }

    public void render() {
        repaint();
    }

    private void update(double delta) {
        if (dragging)
            arrowDirection = mouseStart.sub(mouseCurrent);

        Arrow last = null;
        for (Arrow arrow : arrows) {
            if (!arrow.isInBound(new Vector(-10000, getHeight() - groundHeight), 20000, 1000)) {
                arrow.update(delta);
                last = arrow;
            }
        }
        // follow the last arrow
        if (last != null) {
            offset.x = -(last.getPosition().x - getWidth() / 2.);
            offset.y = -(last.getPosition().y - getHeight() / 2.);

            // the ground should not move up
            if (offset.y < 0)
                offset.y = 0;
        }

        if (arm != null) {
            arm.setBase(new Vector(getWidth() / 2., getHeight()));
        }

        if (archer != null) {
            archer.update(delta);
            if (dragging)
                archer.draw(arrowDirection);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        innerRender(g2);

        g2.dispose();
    }

    private void innerRender(Graphics2D g) {
        drawFPS(g);
        // write the angle of the arrow
        g.drawString(String.valueOf(arrowDirection.flipY().angleDeg()), 0, 30);

        // Scroll all objects
        g.translate(offset.x, offset.y);

        // ground
        g.drawLine(-10000, (int) (getHeight() - groundHeight), 10000, (int) (getHeight() - groundHeight));

        for (Arrow arrow : arrows) {
            arrow.render(g);
        }

        // color the last arrow as green
        if (arrows.size() > 0) {
            Stroke old_stroke = g.getStroke();
            Paint old_paint = g.getPaint();

            g.setPaint(Color.GREEN);
            g.setStroke(new BasicStroke(5));
            arrows.get(arrows.size() - 1).render(g);

            g.setStroke(old_stroke);
            g.setPaint(old_paint);
        }

        if (arm != null)
            arm.render(g);
        if (archer != null)
            archer.render(g);
    }

    private void drawFPS(Graphics2D g) {
        g.drawString(String.valueOf(fps), 0, 10);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        switch (mouseEvent.getButton()) {
            case MouseEvent.BUTTON1: {
                dragging = true;
                mouseStart.setValue(mouseEvent.getPoint());
                mouseStart = mouseStart.sub(offset);

                mouseCurrent.setValue(mouseEvent.getPoint());
                mouseCurrent = mouseCurrent.sub(offset);
                break;
            }
            case MouseEvent.BUTTON2: {
                archer = new Archer(200);
                archer.setGroundPosition(new Vector(mouseEvent.getPoint()).sub(offset));
                break;
            }
            case MouseEvent.BUTTON3: {
                // not used
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (dragging) {
            dragging = false;
            if (archer != null) {
                Arrow arrow = archer.releaseArrow();
                arrow.setAcceleration(new Vector(0, 9.8).scale(0.1));

                arrows.add(arrow);

                archer.resetBowAndArrow();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        mouseCurrent.setValue(mouseEvent.getPoint());
        mouseCurrent = mouseCurrent.sub(offset);

        // dragging is not considered movement, so we need to update it in two places :(
        if (arm != null)
            arm.follow(mouseCurrent);
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
        Vector pos = new Vector(mouseEvent.getPoint()).sub(offset);
        if (arm != null)
            arm.follow(pos);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        int units = mouseWheelEvent.getUnitsToScroll();
        offset.x += units * 10;
    }
}
