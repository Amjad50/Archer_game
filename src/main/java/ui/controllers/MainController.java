package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class MainController {

    final int CIRCLE_WIDTH = 20;

    @FXML
    private Pane canvasContainer;
    @FXML
    private Canvas canvas;
    private CanvasController canvasController;

    @FXML
    private void initialize() {
        canvasController = new CanvasController(canvas, canvasContainer);

        canvasController.setFPS(60).startMainLoop();
    }
}

