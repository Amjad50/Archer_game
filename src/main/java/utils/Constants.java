package utils;

public class Constants {

    // FPS is the number of times, the canvas will be updated in a second (more FPS, smooth animation).
    public static final int FPS = 250;
    // because we don't want our objects to be updated so fast, we have a separate update per second
    // variable, which means how many updates should be in a second.
    //
    // BUT, instead of only calling update N times every second with full update in each one
    // we can update the objects by some part of the whole update.
    // Example: if the ball moves 100M per update frame, we are running the app in
    // 60 updates per second and 120 frames per second, we can move the ball 50M each frame update
    // which will result in a better animation.
    public static final int UPDATE_PS = 60;
}
