package ui;

import ui.model.Arrow;
import ui.model.Rect;

public class Shot {
    public Arrow arrow;
    public Rect target;

    public Shot(Arrow arrow, Rect target) {
        this.arrow = arrow;
        this.target = target;
    }
}
