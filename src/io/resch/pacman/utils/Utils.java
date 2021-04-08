package io.resch.pacman.utils;

import io.resch.pacman.board.Wall;
import io.resch.pacman.board.way.Direction;

import java.awt.Point;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static io.resch.pacman.board.way.Direction.*;

public class Utils {

    public static final int RESOLUTION = 16;
    public static final int WIDTH = 28 * RESOLUTION;
    public static final int HEIGHT = 36 * RESOLUTION;

    public static int pythagoras(Point target, int startX, int startY) {
        return (int) Math.sqrt((Math.pow(target.x - startX, 2) + Math.pow(target.y - startY, 2)));
    }

    public static List<String> readFile(String filename) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(filename);
        if (input == null) {
            return Collections.emptyList();
        }
        Scanner reader = new Scanner(input).useDelimiter("\\n");
        String line;
        List<String> lines = new ArrayList<>();
        while (!(line = reader.hasNext() ? reader.next() : "").isEmpty()) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }
}
