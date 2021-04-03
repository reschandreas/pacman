package io.resch.pacman.movable;

import io.resch.pacman.gui.*;
/**
 * Created by Andreas on 09.04.16.
 * Orange Ghost
 */
public class Clyde extends Ghost {

    public Clyde() {
        super("ghost_orange.png", "ghost_frightened.png");
        startpos[0] = 240;
        startpos[1] = 268;
        setLocation(startpos[0], startpos[1]);
        target[0] = 0;
        target[1] = 560;
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
            if (pythagoras(PacmanGUI.pacman.getRealX(), PacmanGUI.pacman.getRealY(), getRealX(), getRealY()) >= 8 * PacmanGUI.RESOLUTION) {
                setCurrent_target(new int[]{PacmanGUI.pacman.getRealX(), PacmanGUI.pacman.getRealY()});
            } else {
                setCurrent_target(target);
            }
        } else {
            super.calculateTarget();
        }
    }
}
