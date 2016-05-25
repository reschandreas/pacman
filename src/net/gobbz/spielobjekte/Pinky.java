package net.gobbz.spielobjekte;

import programm.*;

/**
 * Created by Andreas on 09.04.16.
 * Pink Ghost
 */
public class Pinky extends Ghost {

    public Pinky() {
        super("ghost_pink.png", "ghost_frightened.png");
        startpos[0] = 240;
        startpos[1] = 268;
        setLocation(startpos[0], startpos[1]);
        target[0] = 32;
        target[1] = 0;
        current_target[0] = targetinhouse[0];
        current_target[1] = targetinhouse[1];
    }

    @Override
    public void start() {
        super.start();
        x_speed = -1;
        x_next = 0;
        y_speed = 0;
        y_next = -1;
    }

    @Override
    public void calculateTarget() {
        if (getCurrentMode() == CHASEMODE) {
            int x = 0;
            int y = 0;
            switch (PacmanGUI.pacman.getX_speed()) {
                case -1:
                    x = -4 * PacmanGUI.RESOLUTION;
                    break;
                case 1:
                    x = 4 * PacmanGUI.RESOLUTION;
                    break;
            }
            switch (PacmanGUI.pacman.getY_speed()) {
                case -1:
                    y = -4 * PacmanGUI.RESOLUTION;
                    break;
                case 1:
                    y = 4 * PacmanGUI.RESOLUTION;
                    break;
            }
            setCurrent_target(new int[]{PacmanGUI.pacman.getX() + x, PacmanGUI.pacman.getY() + y});
        } else {
            super.calculateTarget();
        }
    }
}
