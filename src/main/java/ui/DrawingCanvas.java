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

    private int xOffset = 0;

    private ArrayList<Arrow> arrows = new ArrayList<>();
    private Archer archer;
    private Arm arm = new Arm();

    public DrawingCanvas() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    public void render(double delta, long fps) {
        prepareRender(delta);
        this.fps = fps;
        repaint();
    }

    private void prepareRender(double delta) {
        if (dragging) {
            arrowDirection = mouseStart.sub(mouseCurrent);
        }

        for (Arrow arrow : arrows) {
            if(!arrow.isInBound(new Vector(-10000, getHeight()), 20000, 1000))
                arrow.update(delta);
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
        g.translate(xOffset, 0);

        for (Arrow arrow : arrows) {
            arrow.render(g);
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
                mouseStart.x -= xOffset;
                mouseCurrent.setValue(mouseEvent.getPoint());
                mouseCurrent.x -= xOffset;
                // this is to match 9/12 of the width of the stick man
                break;
            }
            case MouseEvent.BUTTON2: {
                archer = new Archer(200);
                archer.setGroundPosition(new Vector(mouseEvent.getPoint()).sub(new Vector(xOffset, 0)));
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
        mouseCurrent.x -= xOffset;

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
        Vector pos = new Vector(mouseEvent.getPoint());
        pos.x -= xOffset;
        if (arm != null)
            arm.follow(pos);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        int units = mouseWheelEvent.getUnitsToScroll();
        xOffset += units * 10;
    }
}
