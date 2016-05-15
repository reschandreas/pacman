package net.gobbz.grundobjekte;

import programm.*;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Andreas on 10.04.16.
 */
public class Intersection extends JComponent {

    private final boolean up;
    private final boolean down;
    private final boolean right;
    private final boolean left;

    public Intersection(int x, int y, boolean up, boolean down, boolean right, boolean left) {
        setBounds(x, y, 2 * PacmanGUI.RESOLUTION, 2 * PacmanGUI.RESOLUTION);
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        setVisible(false);
    }

    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public boolean isDown() {
        return down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isUp() {
        return up;
    }
}
