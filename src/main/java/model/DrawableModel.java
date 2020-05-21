package model;

import javafx.scene.canvas.GraphicsContext;

public interface DrawableModel {
    void update(double delta);
    void render(GraphicsContext gfx);
    boolean isInBound(double w, double h);
}
