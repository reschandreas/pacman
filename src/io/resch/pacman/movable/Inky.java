package io.resch.pacman.movable;

import io.resch.pacman.gui.*;
import io.resch.pacman.utils.Utils;

/**
 * Created by Andreas on 09.04.16.
 * Blue Ghost
 */
public class Inky extends Ghost {

    public Inky() {
        super(Type.INKY);
        startpos[0] = 176;
        startpos[1] = 268;
        setLocation(startpos[0], startpos[1]);
        target[0] = 432;
        target[1] = 560;
        currentTarget[0] = targetinhouse[0];
        currentTarget[1] = targetinhouse[1];
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
        if (getCurrentMode() == Mode.CHASE) {
            int targetx = 0;
            int targety = 0;
            switch (PacmanGUI.pacman.getX_speed()) {
                case -1 -> targetx = -2 * Utils.RESOLUTION;
                case 1 -> targetx = 2 * Utils.RESOLUTION;
            }
            switch (PacmanGUI.pacman.getY_speed()) {
                case -1 -> targety = -2 * Utils.RESOLUTION;
                case 1 -> targety = 2 * Utils.RESOLUTION;
            }
            targetx += PacmanGUI.pacman.getRealX();
            targety += PacmanGUI.pacman.getRealY();
            int x_blinky = PacmanGUI.ghosts.get(0).getRealX();
            int y_blinky = PacmanGUI.ghosts.get(0).getRealY();
            setCurrentTarget(new int[]{x_blinky + (2 * (targetx - x_blinky)), y_blinky + (2 * (targety - y_blinky))});
        } else {
            super.calculateTarget();
        }
    }
}
