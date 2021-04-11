package io.resch.pacman.board.way;

import java.util.*;

import static io.resch.pacman.board.way.Direction.*;
import static io.resch.pacman.board.way.Direction.LEFT;

public class Navigator {

    public static Distance getBestDirection(List<Distance> distances) {
        if (distances.isEmpty()) {
            return null;
        }
        Optional<Distance> minimal = distances.stream().min(Comparator.comparingDouble(Distance::distance));
        return minimal.get();
    }

    public static Distance getRandomDirection(List<Distance> distances) {
        if (distances.isEmpty()) {
            return null;
        }
        Collections.shuffle(distances);
        return distances.get(0);
    }

    public static Direction getOpposite(Direction direction) {
        return switch (direction) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
