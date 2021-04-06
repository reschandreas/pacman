package io.resch.pacman.utils;

import io.resch.pacman.board.way.Direction;

import java.awt.Point;

import static io.resch.pacman.board.way.Direction.*;

public class Utils {

    public static final int RESOLUTION = 16;
    public static final int WIDTH = 28 * RESOLUTION;
    public static final int HEIGHT = 36 * RESOLUTION;

    public static int pythagoras(Point target, int startX, int startY) {
        return (int) Math.sqrt((Math.pow(target.x - startX, 2) + Math.pow(target.y - startY, 2)));
    }
}
