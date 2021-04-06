package io.resch.pacman.board;

import io.resch.pacman.gui.*;
import io.resch.pacman.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Andreas on 14.04.16.
 */
public class Tile extends JComponent {

    public Tile(Point location) {
        setBounds(location.x, location.y, Utils.RESOLUTION, Utils.RESOLUTION);
        setVisible(true);
    }

    public void paint(Graphics g) {
        g.setColor(Color.green);
        g.drawRect(0, 0, this.getWidth(), this.getHeight());
    }

    public int getRealX() {
        return getX() + getWidth() / 2;
    }

    public int getRealY() {
        return getY() + getHeight() / 2;
    }
}
