package net.gobbz.spielobjekte;

import programm.*;

/**
 * Created by Andreas on 09.04.16.
 * Blue Ghost
 */
public class Inky extends Ghost {

    public Inky() {
        super("ghost_blue.png", "ghost_frightened.png");
        startpos[0] = 176;
        startpos[1] = 268;
        setLocation(startpos[0], startpos[1]);
        target[0] = 432;
        target[1] = 560;
        current_target[0] = targetinhouse[0];
        current_target[1] = targetinhouse[1];
    }

    @Override
    public void start() {
        super.start();
        x_speed = 1;
        x_next = 0;
        y_speed = 0;
        y_next = -1;
    }

    @Override
    public void calculateTarget() {
        if (getCurrentMode() == CHASEMODE) {
            int targetx = 0;
            int targety = 0;
            switch (PacmanGUI.pacman.getX_speed()) {
                case -1:
                    targetx = -2 * PacmanGUI.RESOLUTION;
                    break;
                case 1:
                    targetx = 2 * PacmanGUI.RESOLUTION;
                    break;
            }
            switch (PacmanGUI.pacman.getY_speed()) {
                case -1:
                    targety = -2 * PacmanGUI.RESOLUTION;
                    break;
                case 1:
                    targety = 2 * PacmanGUI.RESOLUTION;
                    break;
            }
            targetx += PacmanGUI.pacman.getRealX();
            targety += PacmanGUI.pacman.getRealY();
            int x_blinky = PacmanGUI.ghosts.get(0).getRealX();
            int y_blinky = PacmanGUI.ghosts.get(0).getRealY();
            setCurrent_target(new int[]{x_blinky + (2 * (targetx - x_blinky)), y_blinky + (2 * (targety - y_blinky))});
        } else {
            super.calculateTarget();
        }
    }
}
