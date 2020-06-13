package ui.model;

import utils.Vector;

import java.awt.*;

public class HealthBar implements DrawableModel {

    private static final double DEFAULT_WIDTH = 200;
    private static final double DEFAULT_HEIGHT = 50;
    private static final double DEFAULT_OFFSET = 5;

    private Vector position;
    private double maxHealth;
    private double currentHealth;
    private double percentage;
    private double width;
    private double height;
    private double offset;

    public HealthBar(double maxHealth) {
        this(maxHealth, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public HealthBar(double maxHealth, double width, double height) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.width = width;
        this.height = height;

        this.offset = DEFAULT_OFFSET;
    }

    @Override
    public void update(double delta) {
        percentage = currentHealth / maxHealth;
        System.out.println(percentage);
    }

    @Override
    public void render(Graphics2D g) {
        Paint old_paint = g.getPaint();

        String healthString = String.valueOf((int) currentHealth);
        FontMetrics metrics = g.getFontMetrics();
        int stringWidth = metrics.stringWidth(healthString);

        g.setPaint(Color.GRAY);
        g.fillRect((int) position.x, (int) position.y, (int) width, (int) height);

        g.setPaint(Color.WHITE);
        g.drawString(healthString,
                (int) (position.x + offset),
                (float) (position.y + height / 2 - metrics.getHeight() / 2 + metrics.getAscent()));

        g.setPaint(Color.RED);
        g.fillRect((int) (position.x + offset * 2 + stringWidth),
                (int) (position.y + offset),
                (int) ((width - stringWidth - offset * 3) * percentage),
                (int) (height - offset * 2));

        g.setPaint(old_paint);
    }

    @Override
    public boolean isInBound(Rect bounds) {
        return false;
    }

    public void setHealth(double health) {
        currentHealth = health;
    }

    public void setPosition(Vector position) {
        this.position = position.copy();
    }

    public double getHealth() {
        return currentHealth;
    }

    public double getWidth() {
        return width;
    }
}