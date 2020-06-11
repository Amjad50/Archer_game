package ui.model;

import utils.Vector;

import java.awt.*;

public interface DrawableModel {
    void update(double delta);
    void render(Graphics2D g);
    boolean isInBound(Vector start, double w, double h);
}
