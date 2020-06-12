package ui;

import ui.model.Archer;
import ui.model.Arrow;
import ui.model.LocatorArrow;
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

    // true p1, false p2
    private boolean playerSelector = false;

    private Vector offset = new Vector();
    private double groundHeight;

    private ArrayList<Arrow> arrows = new ArrayList<>();
    private Archer p1, p2;
    private Arm arm = new Arm();
    private LocatorArrow p1Locator = new LocatorArrow();
    private LocatorArrow p2Locator = new LocatorArrow();

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

        if (p1 != null) {
            p1.update(delta);
            if (dragging && playerSelector)
                p1.draw(arrowDirection);
            if (p1Locator != null) {
                p1Locator.setPosition(new Vector(getWidth() / 2. - 100, 30));
                p1Locator.setTarget(p1.getGroundPosition().add(offset));
                p1Locator.update(delta);
            }
        }

        if (p2 != null) {
            p2.update(delta);
            if (dragging && !playerSelector)
                p2.draw(arrowDirection);
            if (p2Locator != null) {
                p2Locator.setPosition(new Vector(getWidth() / 2. + 100, 30));
                p2Locator.setTarget(p2.getGroundPosition().add(offset));
                p2Locator.update(delta);
            }
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

        Paint old_paint = g.getPaint();

        if (p1Locator != null) {
            g.drawString("P1", (int) (p1Locator.getPosition().x - p1Locator.getRadius() * 2), (int) p1Locator.getPosition().y);
            // Color the arrow for (next player) player 1 to green
            if (!playerSelector)
                g.setPaint(Color.GREEN);
            p1Locator.render(g);
        }
        g.setPaint(old_paint);

        if (p2Locator != null) {
            g.drawString("P2", (int) (p2Locator.getPosition().x - p2Locator.getRadius() * 2), (int) p2Locator.getPosition().y);

            // Color the arrow for (next player) player 2 to green
            if (playerSelector)
                g.setPaint(Color.GREEN);
            p2Locator.render(g);
        }

        // restore
        g.setPaint(old_paint);

        // Scroll all objects
        g.translate(offset.x, offset.y);

        // ground
        g.drawLine(-10000, (int) (getHeight() - groundHeight), 10000, (int) (getHeight() - groundHeight));

        if (p1 != null)
            p1.render(g);
        if (p2 != null)
            p2.render(g);

        for (Arrow arrow : arrows) {
            arrow.render(g);
        }

        // color the last arrow as green
        if (arrows.size() > 0) {
            Stroke old_stroke = g.getStroke();
            old_paint = g.getPaint();

            g.setPaint(Color.GREEN);
            g.setStroke(new BasicStroke(5));
            arrows.get(arrows.size() - 1).render(g);

            g.setStroke(old_stroke);
            g.setPaint(old_paint);
        }

        if (arm != null)
            arm.render(g);
    }

    private void drawFPS(Graphics2D g) {
        g.drawString(String.valueOf(fps), 0, 10);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        switch (mouseEvent.getButton()) {
            case MouseEvent.BUTTON1: {
                dragging = true;
                playerSelector = !playerSelector;
                mouseStart.setValue(mouseEvent.getPoint());
                mouseStart = mouseStart.sub(offset);

                mouseCurrent.setValue(mouseEvent.getPoint());
                mouseCurrent = mouseCurrent.sub(offset);
                break;
            }
            case MouseEvent.BUTTON2: {
                p1 = new Archer(200);
                p1.setGroundPosition(new Vector(mouseEvent.getPoint()).sub(offset));
                break;
            }
            case MouseEvent.BUTTON3: {
                p2 = new Archer(200);
                p2.setGroundPosition(new Vector(mouseEvent.getPoint()).sub(offset));
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (dragging) {
            dragging = false;

            Archer archer = null;
            if (playerSelector && p1 != null) {
                archer = p1;
            } else if (!playerSelector && p2 != null) {
                archer = p2;
            }

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

        // make the mouseStart offset from the screen and not the world dimensions
        mouseStart.x -= units * 10;
    }
}
