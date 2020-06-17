package ui.model;

import utils.Vector;

import java.awt.*;

public class HealthBar implements DrawableModel {

    private static final double DEFAULT_WIDTH = 200;
    private static final double DEFAULT_HEIGHT = 20;
    private static final double DEFAULT_OFFSET = 5;

    private static final double DEFAULT_DECREASE_RATE = 0.0006;

    private Vector position;
    private double maxHealth;
    private double currentHealth;
    private double percentage;
    private double width;
    private double height;
    private double offset;

    private double slowDecreaseHealth;
    private double slowDecreasePercentage;

    public HealthBar(double maxHealth) {
        this(maxHealth, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public HealthBar(double maxHealth, double width, double height) {
        assert maxHealth > 0;

        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.width = width;
        this.height = height;

        this.offset = DEFAULT_OFFSET;
    }

    @Override
    public void update(double delta) {
        percentage = currentHealth / maxHealth;
        slowDecreasePercentage = slowDecreaseHealth / maxHealth;

        double change = maxHealth * DEFAULT_DECREASE_RATE;

        if (slowDecreaseHealth - change > currentHealth && currentHealth != 0)
            slowDecreaseHealth -= change;
        else
            slowDecreaseHealth = currentHealth;
    }

    @Override
    public void render(Graphics2D g) {
        Paint old_paint = g.getPaint();

        String healthString = String.valueOf((int) Math.ceil(currentHealth));
        FontMetrics metrics = g.getFontMetrics();
        int stringWidth = metrics.stringWidth(healthString);

        g.setPaint(Color.GRAY);
        g.fillRect((int) position.x, (int) position.y, (int) width, (int) height);

        g.setPaint(Color.WHITE);
        g.drawString(healthString,
                (int) (position.x + offset),
                (float) (position.y + height / 2 - metrics.getHeight() / 2 + metrics.getAscent()));

        // under the health bar and the one that shows the slow decreasing health effect
        g.setPaint(Color.PINK);
        g.fillRect((int) (position.x + offset * 2 + stringWidth),
                (int) (position.y + offset),
                (int) ((width - stringWidth - offset * 3) * slowDecreasePercentage),
                (int) (height - offset * 2));

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

    public void decreaseHealth(double amount) {
        currentHealth -= amount;
        if (currentHealth < 0)
            currentHealth = 0;
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

    public double getHeight() {
        return height;
    }

    public Vector getPosition() {
        return position;
    }
}
