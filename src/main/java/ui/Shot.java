package ui;

import ui.model.Arrow;
import ui.model.Rect;

public class Shot {
    public Arrow arrow;
    public Rect target;
    public boolean isTargetPlayer1;
    public boolean bleed = true;

    public Shot(Arrow arrow, Rect target, boolean isTargetPlayer1) {
        this.arrow = arrow;
        this.target = target;
        this.isTargetPlayer1 = isTargetPlayer1;
    }
}
