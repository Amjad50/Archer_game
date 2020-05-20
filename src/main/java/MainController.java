import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

import java.util.concurrent.atomic.AtomicReference;

public class MainController {

    final int CIRCLE_WIDTH = 20;

    @FXML
    private Pane canvasContainer;
    @FXML
    private Canvas canvas;

    @FXML
    private void initialize() {
        canvas.widthProperty().bind(canvasContainer.widthProperty());
        canvas.heightProperty().bind(canvasContainer.heightProperty());

        AtomicReference<Double> x = new AtomicReference<>(0d);
        AtomicReference<Double> y = new AtomicReference<>(0d);

        GraphicsContext gfx = canvas.getGraphicsContext2D();

        canvas.setOnMouseMoved(e -> {
            x.set(e.getSceneX());
            y.set(e.getSceneY());
        });

        new AnimationTimer() {
            @Override
            public void handle(long l) {
                gfx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gfx.fillOval(x.get() - CIRCLE_WIDTH / 2f, y.get() - CIRCLE_WIDTH / 2f, CIRCLE_WIDTH, CIRCLE_WIDTH);
            }
        }.start();
    }
}

