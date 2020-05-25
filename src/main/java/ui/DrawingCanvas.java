package ui;

import utils.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DrawingCanvas extends JPanel implements MouseListener, MouseMotionListener {
    long fps = 0;
    private Vector mouseStart = new Vector(0d, 0d);
    private Vector mouseCurrent = new Vector(0d, 0d);
    private boolean dragging = false;

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
    }

    private void drawFPS(Graphics2D g) {
        g.drawString(String.valueOf(fps), 0, 10);
    }

    private void drawLineAndAngle(Graphics2D g) {
        if (!dragging)
            return;
        g.drawLine((int) mouseStart.x, (int) mouseStart.y, (int) mouseCurrent.x, (int) mouseCurrent.y);
        g.drawString(String.valueOf(mouseCurrent.sub(mouseStart).flipY().angleDeg()), 0, 30);
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
