package model;

import javafx.scene.canvas.GraphicsContext;

public interface DrawableModel {
    void update();
    void render(GraphicsContext gfx);
    boolean isInBound(double w, double h);
}
