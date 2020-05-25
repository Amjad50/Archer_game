package ui.model;

import java.awt.*;

public interface DrawableModel {
    void update(double delta);
    void render(Graphics2D g);
    boolean isInBound(double w, double h);
}
