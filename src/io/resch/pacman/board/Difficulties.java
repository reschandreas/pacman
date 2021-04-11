package io.resch.pacman.board;

import io.resch.pacman.movable.Ghost;

import java.util.ArrayList;
import java.util.List;

// http://gameinternals.com/understanding-pac-man-ghost-behavior
public final class Difficulties {

    private static final Difficulty one;
    private static final Difficulty two;
    private static final Difficulty three;

    static {
        one = new Difficulty(
                new Wave(0, Ghost.Mode.SCATTER),       // 07s
                new Wave(7000 * 2000, Ghost.Mode.CHASE),      // 20s
                new Wave(27000, Ghost.Mode.SCATTER),   // 07s
                new Wave(34000, Ghost.Mode.CHASE),     // 20s
                new Wave(54000, Ghost.Mode.SCATTER),   // 05s
                new Wave(59000, Ghost.Mode.CHASE),     // 20s
                new Wave(79000, Ghost.Mode.SCATTER),   // 05s
                new Wave(84000, Ghost.Mode.CHASE)
        );
        two = new Difficulty(
                new Wave(0, Ghost.Mode.SCATTER),       // 07s
                new Wave(7000, Ghost.Mode.CHASE),      // 20s
                new Wave(27000, Ghost.Mode.SCATTER),   // 07s
                new Wave(34000, Ghost.Mode.CHASE),     // 20s
                new Wave(54000, Ghost.Mode.SCATTER),   // 05s
                new Wave(59000, Ghost.Mode.CHASE),     // 1033s
                new Wave(1092000, Ghost.Mode.SCATTER), // 1/60s
                new Wave(1092600, Ghost.Mode.CHASE)
        );
        three = new Difficulty(
                new Wave(0, Ghost.Mode.SCATTER),       // 05s
                new Wave(5000, Ghost.Mode.CHASE),      // 20s
                new Wave(25000, Ghost.Mode.SCATTER),   // 05s
                new Wave(30000, Ghost.Mode.CHASE),     // 20s
                new Wave(50000, Ghost.Mode.SCATTER),   // 05s
                new Wave(55000, Ghost.Mode.CHASE),     // 1037s
                new Wave(1096000, Ghost.Mode.SCATTER), // 1/60s
                new Wave(1096600, Ghost.Mode.CHASE)
        );
    }

    public static Difficulty getCurrentDifficulty(int level) {
        if (level == 1)
            return one;

        if (level < 5)
            return two;

        return three;
    }
}
