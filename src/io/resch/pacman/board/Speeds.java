package io.resch.pacman.board;

// https://www.gamasutra.com/view/feature/132330/the_pacman_dossier.php?page=3
public final class Speeds {

    private static final SpeedLimits one;
    private static final SpeedLimits two;
    private static final SpeedLimits three;
    private static final SpeedLimits four;

    static {
        one = new SpeedLimits(
                new Speed(Speed.Type.NORMAL, 1.2),
                new Speed(Speed.Type.DOT_NORMAL, 1.29),
                new Speed(Speed.Type.FRIGHT, 1.10),
                new Speed(Speed.Type.DOT_FRIGHT, 1.21),
                new Speed(Speed.Type.GHOST_NORMAL, 1.25),
                new Speed(Speed.Type.GHOST_FRIGHT, 1.50),
                new Speed(Speed.Type.GHOST_TUNNEL, 1.60)
        );

        two = new SpeedLimits(
                new Speed(Speed.Type.NORMAL, 1.1),
                new Speed(Speed.Type.DOT_NORMAL, 1.21),
                new Speed(Speed.Type.FRIGHT, 1.05),
                new Speed(Speed.Type.DOT_FRIGHT, 1.17),
                new Speed(Speed.Type.GHOST_NORMAL, 1.15),
                new Speed(Speed.Type.GHOST_FRIGHT, 1.45),
                new Speed(Speed.Type.GHOST_TUNNEL, 1.55)
        );

        three = new SpeedLimits(
                new Speed(Speed.Type.NORMAL, 1),
                new Speed(Speed.Type.DOT_NORMAL, 1.13),
                new Speed(Speed.Type.FRIGHT, 1),
                new Speed(Speed.Type.DOT_FRIGHT, 1.13),
                new Speed(Speed.Type.GHOST_NORMAL, 1.05),
                new Speed(Speed.Type.GHOST_FRIGHT, 1.40),
                new Speed(Speed.Type.GHOST_TUNNEL, 1.5)
        );

        four = new SpeedLimits(
                new Speed(Speed.Type.NORMAL, 1.1),
                new Speed(Speed.Type.DOT_NORMAL, 1.21),
                new Speed(Speed.Type.FRIGHT, 1),
                new Speed(Speed.Type.DOT_FRIGHT, 1.13),
                new Speed(Speed.Type.GHOST_NORMAL, 1.05),
                new Speed(Speed.Type.GHOST_FRIGHT, 1.40),
                new Speed(Speed.Type.GHOST_TUNNEL, 1.5)
        );
    }

    public static SpeedLimits getCurrentSpeedLimits(int level) {
        if (level == 1)
            return one;

        if (level < 5)
            return two;

        if (level < 20)
            return three;

        return four;
    }
}
