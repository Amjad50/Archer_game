package ui;

import ui.model.Arrow;
import ui.model.BowAndArrow;
import ui.model.Stickman;
import utils.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DrawingCanvas extends JPanel implements MouseListener, MouseMotionListener {

    private long fps = 0;
    private Vector mouseStart = new Vector();
    private Vector mouseCurrent = new Vector();
    private Vector arrowDirection = new Vector();
    private boolean dragging = false;

    private Arrow arrow;
    private BowAndArrow bowAndArrow;
    private Stickman stickman;

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
        }
        if (bowAndArrow != null) {
            bowAndArrow.setDirectionNormalized(arrowDirection);
            bowAndArrow.setStartPosition(mouseStart);
        }
        if (arrow != null && arrow.isInBound(getWidth(), getHeight()))
            arrow.update(delta);
        if (stickman != null)
            stickman.update(delta);
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

        if (bowAndArrow != null)
            bowAndArrow.render(g);
        if (arrow != null)
            arrow.render(g);
        if (stickman != null)
            stickman.render(g);
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
                mouseCurrent.setValue(mouseEvent.getPoint());
                bowAndArrow = new BowAndArrow();
                break;
            }
            case MouseEvent.BUTTON2: {
                // ignored
                break;
            }
            case MouseEvent.BUTTON3: {
                stickman = new Stickman(200);
                stickman.setGroundPosition(new Vector(mouseEvent.getPoint()));
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (dragging) {
            dragging = false;
            arrow = new Arrow(bowAndArrow.getArrowLength());
            arrow.setPosition(mouseStart);
            arrow.setVelocity(arrowDirection.scale(0.3));
            arrow.setAcceleration(new Vector(0, 9.8).scale(0.1));

            // remove the bow and arrow preview
            bowAndArrow = null;
        }
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
