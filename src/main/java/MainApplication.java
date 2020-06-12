import ui.DrawingCanvas;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class MainApplication extends JFrame {

    private static final int FPS = 90;
    private static final int FPS_60 = 60;

    DrawingCanvas canvas;
    TimerTask task;
    Timer timer;

    MainApplication() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        canvas = new DrawingCanvas();
        task = initGameLoop();
        timer = new Timer();
        setContentPane(canvas);
    }

    void startGameLoop() {
        if (task != null)
            timer.scheduleAtFixedRate(task, 0, 1000/FPS);
    }

    TimerTask initGameLoop() {
        return new TimerTask() {
            long past = 0;

            @Override
            public void run() {
                long now = System.nanoTime();
                long fps = 1000_000_000 / (now - past);
                double delta = (now - past) / 1E9 * FPS_60;
                canvas.update(delta, fps);
                SwingUtilities.invokeLater(canvas::render);
                past = now;
            }
        };
    }

    void start() {
        startGameLoop();
        setVisible(true);
    }
}
