package net.gobbz.spielobjekte;

import programm.*;

/**
 * Created by Andreas on 09.04.16.
 * Blue Ghost
 */
public class Inky extends Ghost {

    public Inky(String path, String frightened) {
        super(path, frightened);
/*        startpos[0] = 208;
        startpos[1] = 268;*/
        startpos[0] = 208;
        startpos[1] = 216;
        setLocation(startpos[0], startpos[1]);
        target[0] = current_target[0] = 432;
        target[1] = current_target[1] = 560;
    }

    @Override
    protected void calculateTarget() {
        if (getCurrent_mode() == CHASEMODE) {
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
