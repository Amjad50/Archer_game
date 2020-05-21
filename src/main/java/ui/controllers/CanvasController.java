package ui.controllers;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import model.Ball;
import utils.Vector;

public class CanvasController {
    private Pane container;
    private Canvas canvas;
    private GraphicsContext gfx;
    private AnimationTimer mainLoop;
    private long time_delay = 0;

    private Ball ball;

    private Vector mouseStart = new Vector(0d, 0d);
    private Vector mouseCurrent = new Vector(0d, 0d);
    private boolean dragging = false;

    public CanvasController(Canvas canvas, Pane container) {
        this.container = container;
        this.canvas = canvas;
        this.gfx = canvas.getGraphicsContext2D();


        this.canvas.widthProperty().bind(this.container.widthProperty());
        this.canvas.heightProperty().bind(this.container.heightProperty());

        mainLoop = setupMainLoop();
        setupListeners();
    }

    private void setupListeners() {
        canvas.setOnMousePressed(e -> {
            dragging = true;
            mouseStart.setValue(e.getX(), e.getY());
            mouseCurrent.setValue(e.getX(), e.getY());

        });
        canvas.setOnMouseDragged(e -> mouseCurrent.setValue(e.getX(), e.getY()));
        canvas.setOnMouseReleased(e -> {
            dragging = false;
            ball = new Ball(20);
            ball.setPosition(mouseStart);
            ball.setVelocity(mouseCurrent.sub(mouseStart).scale(0.1));
            ball.setAcceleration(new Vector(0, 9.8).scale(0.1));
        });
    }

    public CanvasController startMainLoop() {
        mainLoop.start();
        return this;
    }

    public CanvasController setFPS(long fps) {
        time_delay = 1000_000_000 / fps;
        return this;
    }

    private void update() {
        if (ball != null && ball.isInBound(canvas.getWidth(), canvas.getHeight()))
            ball.update();
        else
            ball = null;
    }

    private void render() {
        gfx.save();
        drawLineAndAngle();
        if (ball != null)
            ball.render(gfx);
        gfx.restore();
    }

    private void drawLineAndAngle() {
        if (!dragging)
            return;
        gfx.strokeLine(mouseStart.x, mouseStart.y, mouseCurrent.x, mouseCurrent.y);
        gfx.fillText(String.valueOf(mouseCurrent.sub(mouseStart).flipY().angleDeg()), 0, 30);
    }

    private AnimationTimer setupMainLoop() {
        return new AnimationTimer() {
            long past = 0;

            private void realHandle(long now) {
                update();
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
