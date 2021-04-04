package io.resch.pacman.board;

import java.awt.*;

/**
 * Created by Andreas on 16.04.16.
 */
public class Energizer extends Dot {

    private Energizer(Type type, Point location) {
        super(type, new Point(location.x - 6 , location.y - 6));
        points = 50;
    }

    public static Energizer create(Point location) {
        return new Energizer(Type.ENERGIZER, location);
    }
}
