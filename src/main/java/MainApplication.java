import ui.DrawingCanvas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainApplication extends JFrame {

    private static final int FPS = 60;

    DrawingCanvas canvas;
    Timer gameLoop;

    MainApplication() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        canvas = new DrawingCanvas();
        gameLoop = initGameLoop();
        setContentPane(canvas);
    }

    Timer initGameLoop() {
        // 60 FPS
        return new Timer(1000 / FPS, new ActionListener() {
            long past = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                long now = System.nanoTime();
                long fps =  1000_000_000 / (now - past);
                past = System.nanoTime();
                canvas.repaint();
                canvas.render((now - past) / 1E9 * FPS, fps);
            }
        });
    }

    void start() {
        gameLoop.start();
        setVisible(true);
    }
}
