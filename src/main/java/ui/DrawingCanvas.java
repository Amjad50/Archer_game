package ui;

import ui.model.*;
import ui.model.stickman.Arm;
import utils.Constants;
import utils.Helpers;
import utils.Measurement;
import utils.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DrawingCanvas extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    private final static double MIN_HEALTH_LOSS = 2;
    private final static double MAX_HEALTH_LOSS = 40;

    private long fps = 0;
    private Vector mouseStart = new Vector();
    private Vector mouseCurrent = new Vector();
    private Vector arrowDirection = new Vector();
    private boolean dragging = false;
    private boolean gameOver = false;

    private boolean goback = false;

    // true p1, false p2
    private boolean playerSelector = true;

    private Vector offset = new Vector();
    private double groundHeight;

    private ArrayList<Shot> shots = new ArrayList<>();
    private Archer p1, p2;
    private Arm arm = new Arm();
    private LocatorArrow p1Locator = new LocatorArrow();
    private LocatorArrow p2Locator = new LocatorArrow();
    private HealthBar p1Health = new HealthBar(100);
    private HealthBar p2Health = new HealthBar(100);

    private Blood blood;

    public DrawingCanvas() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        groundHeight = 100;

        p1 = new Archer(200);
        p2 = new Archer(200, true);
    }

    private Vector getGravity() {
        // this would update UPDATE_PS times per second, so we scale the gravity to match that.
        return Measurement.meter_to_pixel(new Vector(0, 9.8).scale(1. / Constants.UPDATE_PS));
    }

    public void update(double delta, long fps) {
        this.fps = fps;
        update(delta);
    }

    public void render() {
        repaint();
    }

    private void update(double delta) {
        if (gameOver) {
            offset = new Vector();
            return;
        }

        if (dragging)
            arrowDirection = mouseStart.sub(mouseCurrent);

        Arrow last = null;
        for (Shot shot : shots) {
            if (!shot.arrow.isInBound(new Rect(new Vector(-10000, getHeight() - groundHeight), 20000, 1000)) &&
                    !shot.arrow.isInBound(shot.target)
            ) {
                shot.arrow.update(delta);
                last = shot.arrow;
            } else if (shot.hit) {
                if (shot.arrow.isInBound(shot.target)) {
                    blood = new Blood(shot.arrow.getPosition());
                    blood.setGravity(getGravity().scale(0.1));

                    HealthBar bar = (shot.isTargetPlayer1) ? p1Health : p2Health;

                    if (bar != null) {
                        Archer archer = (shot.isTargetPlayer1) ? p1 : p2;

                        double offsetFromGround = Math.abs(shot.arrow.getPosition().y - p1.getGroundPosition().y);
                        double archerHeight = archer.getHeight();

                        // health would range from 2 to 40
                        double healthShouldLose = Helpers.map(offsetFromGround, 0, archerHeight, MIN_HEALTH_LOSS, MAX_HEALTH_LOSS);

                        bar.decreaseHealth(healthShouldLose);

                        if (bar.getHealth() == 0) {
                            gameOver = true;
                        }
                    }
                }
                shot.hit = false;
                goback = true;
            }
        }

        if (blood != null)
            blood.update(delta);

        // follow the last arrow
        if (last != null) {
            offset.x = -(last.getPosition().x - getWidth() / 2.);
            offset.y = -(last.getPosition().y - getHeight() / 2.);

            // the ground should not move up
            if (offset.y < 0)
                offset.y = 0;
        } else {
            if (goback) {
                Archer archer = playerSelector ? p1 : p2;
                double distance_from_archer = (-offset.x + getWidth() /2. - archer.getGroundPosition().x );
                double amount;

                if (distance_from_archer > 0)
                    amount = 2;
                else
                    amount = -2;

                offset.x += amount;

                if (Math.abs(distance_from_archer) < 20)
                    goback = false;
            }
        }

        if (arm != null) {
            arm.setBase(new Vector(getWidth() / 2., getHeight()));
        }

        if (p1 != null) {
            // update player position in case of window resize
            p1.setGroundPosition(new Vector(200, getHeight() - groundHeight));

            p1.update(delta);
            if (dragging && playerSelector)
                p1.draw(arrowDirection);
            if (p1Health != null) {
                p1Health.setPosition(new Vector(30, 30 - p1Health.getHeight() / 2));
                p1Health.update(delta);
                if (p1Locator != null) {
                    p1Locator.setPosition(new Vector(p1Health.getPosition().x + p1Health.getWidth() + p1Locator.getRadius() * 1.5, p1Health.getPosition().y + p1Health.getHeight() / 2));
                    p1Locator.setTarget(p1.getGroundPosition().add(offset));
                    p1Locator.update(delta);
                }
            }
        }

        if (p2 != null) {
            // update player position in case of window resize
            p2.setGroundPosition(new Vector(3500, getHeight() - groundHeight));

            p2.update(delta);
            if (dragging && !playerSelector)
                p2.draw(arrowDirection);
            if (p2Health != null) {
                p2Health.setPosition(new Vector(getWidth() - 30 - p2Health.getWidth(), 30 - p2Health.getHeight() / 2));
                p2Health.update(delta);

                if (p2Locator != null) {
                    p2Locator.setPosition(new Vector(p2Health.getPosition().x - p2Locator.getRadius() * 1.5, p2Health.getPosition().y + p2Health.getHeight() / 2));
                    p2Locator.setTarget(p2.getGroundPosition().add(offset));
                    p2Locator.update(delta);
                }
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
        if (gameOver) {
            renderGameOver(g);
            return;
        }

        drawFPS(g);

        Archer archer = (playerSelector) ? p1 : p2;

        // write the angle and power of the arrow
        if (dragging) {
            g.drawString("Angle = " + arrowDirection.flipY().angleDeg(), 0, 60);
            g.drawString("Power = " + (int) (archer.getPowerPercentage() * 100), 0, 60 + g.getFontMetrics().getHeight() + 10);
        }

        Paint old_paint = g.getPaint();
        FontMetrics metrics = g.getFontMetrics();

        if (p1Locator != null) {
            String string = "Player 1";
            int stringWidth = metrics.stringWidth(string);
            g.drawString(string,
                    (int) (p1Health.getPosition().x + p1Health.getWidth() / 2 - stringWidth / 2),
                    (int) (p1Health.getPosition().y - 10));
            // Color the arrow for (next player) player 1 to green
            if (playerSelector)
                g.setPaint(Color.GREEN);
            p1Locator.render(g);
        }
        g.setPaint(old_paint);
        if (p1Health != null)
            p1Health.render(g);

        if (p2Locator != null) {
            String string = "Player 2";
            int stringWidth = metrics.stringWidth(string);
            g.drawString(string,
                    (int) (p2Health.getPosition().x + p2Health.getWidth() / 2 - stringWidth / 2),
                    (int) (p2Health.getPosition().y - 10));

            // Color the arrow for (next player) player 2 to green
            if (!playerSelector)
                g.setPaint(Color.GREEN);
            p2Locator.render(g);
        }
        // restore
        g.setPaint(old_paint);

        if (p2Health != null)
            p2Health.render(g);

        // Scroll all objects
        g.translate(offset.x, offset.y);

        // ground
        g.drawLine(-10000, (int) (getHeight() - groundHeight), 10000, (int) (getHeight() - groundHeight));

        if (p1 != null) {
            p1.render(g);
        }
        if (p2 != null) {
            p2.render(g);
        }

        Stroke old_stroke = g.getStroke();
        old_paint = g.getPaint();

        for (Iterator<Shot> iterator = shots.iterator(); iterator.hasNext(); ) {
            Shot shot = iterator.next();

            // color the last arrow as green
            if (!iterator.hasNext()) {
                g.setPaint(Color.GREEN);
                g.setStroke(new BasicStroke(5));
            }

            shot.arrow.render(g);
        }
        // restore
        g.setStroke(old_stroke);
        g.setPaint(old_paint);


        if (blood != null)
            blood.render(g);

        if (arm != null)
            arm.render(g);
    }

    private void renderGameOver(Graphics2D g) {
        g.setFont(new Font("monospace", Font.PLAIN, 100));
        FontMetrics metrics = g.getFontMetrics();
        String string = "GAME OVER";
        int stringWidth = metrics.stringWidth(string);
        int gameOverStringY = getHeight() / 2 + metrics.getHeight() / 3;

        g.drawString(string, getWidth() / 2 - stringWidth / 2, gameOverStringY);
    }

    private void drawFPS(Graphics2D g) {
        g.drawString("FPS: " + fps, 0, 10);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        switch (mouseEvent.getButton()) {
            case MouseEvent.BUTTON1: {
                Vector mousePosition = new Vector(mouseEvent.getPoint()).sub(offset);
                Archer player = (playerSelector) ? p1 : p2;

                // only select if the player in java
                if (player.getGroundPosition().sub(mousePosition).magnitude() < Math.max(getWidth(), getHeight()) / 2.) {
                    dragging = true;
                    mouseStart.setValue(mousePosition);
                    mouseCurrent.setValue(mousePosition);
                }

                break;
            }
            case MouseEvent.BUTTON2:
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

            Archer archer = null;
            Rect target = null;
            if (playerSelector && p1 != null) {
                archer = p1;

                if (p2 != null)
                    target = p2.getBounds();
            } else if (!playerSelector && p2 != null) {
                archer = p2;

                if (p1 != null)
                    target = p1.getBounds();
            }

            playerSelector = !playerSelector;

            if (archer != null && target != null) {
                Arrow arrow = archer.releaseArrow();
                arrow.setAcceleration(getGravity().scale(0.1));

                shots.add(new Shot(arrow, target, playerSelector));

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
