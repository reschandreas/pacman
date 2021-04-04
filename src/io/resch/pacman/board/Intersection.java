package io.resch.pacman.board;

import io.resch.pacman.gui.*;
import io.resch.pacman.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Created by Andreas on 10.04.16.
 */
public class Intersection extends JComponent {

    private final boolean up;
    private final boolean down;
    private final boolean right;
    private final boolean left;

    public Intersection(Point location, boolean[] allowedDirections) {
        setBounds(location.x, location.y, 2 * Utils.RESOLUTION, 2 * Utils.RESOLUTION);
        this.up = allowedDirections[0];
        this.down = allowedDirections[1];
        this.right = allowedDirections[2];
        this.left = allowedDirections[3];
        setVisible(false);
    }

    public static Intersection create(String[] strings) {
        boolean[] allowedDirections = new boolean[4];
        for (int i = 2; i < allowedDirections.length + 2; i++) {
            allowedDirections[i - 2] = Boolean.parseBoolean(strings[i]);
        }
        return new Intersection(new Point(Integer.parseInt(strings[0]), Integer.parseInt(strings[1])), allowedDirections);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intersection that = (Intersection) o;
        return up == that.up && down == that.down && right == that.right && left == that.left;
    }

    @Override
    public int hashCode() {
        return Objects.hash(up, down, right, left);
    }
}
