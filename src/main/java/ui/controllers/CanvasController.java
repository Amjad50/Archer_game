package ui.controllers;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public class CanvasController {
    private Pane container;
    private Canvas canvas;
    private GraphicsContext gfx;
    private AnimationTimer mainLoop;
    private long time_delay = 0;

    public CanvasController(Canvas canvas, Pane container) {
        this.container = container;
        this.canvas = canvas;
        this.gfx = canvas.getGraphicsContext2D();


        this.canvas.widthProperty().bind(this.container.widthProperty());
        this.canvas.heightProperty().bind(this.container.heightProperty());

        mainLoop = setupMainLoop();
    }

    public CanvasController startMainLoop() {
        mainLoop.start();
        return this;
    }

    public CanvasController setFPS(long fps) {
        time_delay = 1000_000_000 / fps;
        return this;
    }

    private void render() {
        gfx.save();
        gfx.restore();
    }

    private AnimationTimer setupMainLoop() {
        return new AnimationTimer() {
            long past = 0;

            private void realHandle(long now) {
                clearCanvas();
                render();
                drawFPS(1000_000_000 / (now - past));
            }

            @Override
            public void handle(long now) {
                if (now - past >= time_delay) {
                    realHandle(now);
                    past = now;
                }
            }
        };
    }

    private void drawFPS(long fps) {
        gfx.fillText(String.valueOf(fps), 0, 10);
    }

    private void clearCanvas() {
        gfx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
