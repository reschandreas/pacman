package io.resch.pacman.board;

import io.resch.pacman.utils.Utils;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Andreas on 03.04.16.
 */
public class Dot extends BoardItem {

    protected int points;
    protected boolean eaten = false;

    public Dot(Type type, Point location) {
        this(type);
        points = 10;
        setLocation(location.x, location.y);
    }

    private Dot(Type type) {
        super(type);
    }

    public static Dot create(String[] strings) {
        Point location = new Point(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
        if (strings[0].equals("E"))
            return Energizer.create(location);

        return new Dot(Type.DOT, location);
    }

    public int getPoints() {
        return points;
    }

    public void eaten() {
        if (eaten)
            return;
        this.eaten = true;
    }

    public static List<Dot> readDotsFile() {
        return Utils.readFile("data/dots.data")
                .stream()
                .map(line -> Dot.create(line.split(";")))
                .collect(Collectors.toList());
    }
}