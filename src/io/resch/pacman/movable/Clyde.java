package io.resch.pacman.movable;

import io.resch.pacman.gui.*;
import io.resch.pacman.utils.Utils;

/**
 * Created by Andreas on 09.04.16.
 * Orange Ghost
 */
public class Clyde extends Ghost {

    public Clyde() {
        super(Type.CLYDE);
        startpos[0] = 240;
        startpos[1] = 268;
        setLocation(startpos[0], startpos[1]);
        target[0] = 0;
        target[1] = 560;
        currentTarget[0] = targetinhouse[0];
        currentTarget[1] = targetinhouse[1];
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
        if (getCurrentMode() == Mode.CHASE) {
            if (pythagoras(PacmanGUI.pacman.getRealX(), PacmanGUI.pacman.getRealY(), getRealX(), getRealY()) >= 8 * Utils.RESOLUTION) {
                setCurrentTarget(new int[]{PacmanGUI.pacman.getRealX(), PacmanGUI.pacman.getRealY()});
            } else {
                setCurrentTarget(target);
            }
        } else {
            super.calculateTarget();
        }
    }
}
