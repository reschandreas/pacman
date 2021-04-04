package io.resch.pacman.board;

import io.resch.pacman.movable.Ghost;

import java.util.ArrayList;
import java.util.List;

public final class Difficulties {

    private static final Difficulty one;
    private static final Difficulty two;
    private static final Difficulty three;
    private static final Difficulty five;

    static {
        one = new Difficulty(
                new Wave(0, Ghost.Mode.SCATTER),
                new Wave(7000, Ghost.Mode.CHASE),
                new Wave(27000, Ghost.Mode.SCATTER),
                new Wave(34000, Ghost.Mode.CHASE),
                new Wave(54000, Ghost.Mode.SCATTER),
                new Wave(59000, Ghost.Mode.CHASE),
                new Wave(79000, Ghost.Mode.SCATTER),
                new Wave(84000, Ghost.Mode.CHASE)
        );
        two = new Difficulty(
                new Wave(0, Ghost.Mode.SCATTER),
                new Wave(7000, Ghost.Mode.CHASE),
                new Wave(27000, Ghost.Mode.SCATTER),
                new Wave(34000, Ghost.Mode.CHASE),
                new Wave(54000, Ghost.Mode.SCATTER),
                new Wave(59000, Ghost.Mode.CHASE),
                new Wave(1092000, Ghost.Mode.SCATTER),
                new Wave(1092600, Ghost.Mode.CHASE)
        );
        three = new Difficulty(
                new Wave(0, Ghost.Mode.SCATTER),
                new Wave(7000, Ghost.Mode.CHASE),
                new Wave(27000, Ghost.Mode.SCATTER),
                new Wave(34000, Ghost.Mode.CHASE),
                new Wave(54000, Ghost.Mode.SCATTER),
                new Wave(59000, Ghost.Mode.CHASE),
                new Wave(1092000, Ghost.Mode.SCATTER),
                new Wave(1092600, Ghost.Mode.CHASE)
        );
        five = new Difficulty(
                new Wave(0, Ghost.Mode.SCATTER),
                new Wave(5000, Ghost.Mode.CHASE),
                new Wave(25000, Ghost.Mode.SCATTER),
                new Wave(32000, Ghost.Mode.CHASE),
                new Wave(52000, Ghost.Mode.SCATTER),
                new Wave(57000, Ghost.Mode.CHASE),
                new Wave(1090000, Ghost.Mode.SCATTER),
                new Wave(1090600, Ghost.Mode.CHASE)
        );
    }

    public static Difficulty getCurrentDifficulty(int level) {
        if (level == 1)
            return one;

        if (level == 2)
            return two;

        if (level <= 20)
            return three;

        return five;
    }
}
