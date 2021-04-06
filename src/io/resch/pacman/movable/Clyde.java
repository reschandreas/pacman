package io.resch.pacman.movable;

import io.resch.pacman.gui.*;
import io.resch.pacman.utils.Utils;

import java.awt.*;

/**
 * Created by Andreas on 09.04.16.
 * Orange Ghost
 */
public class Clyde extends Ghost {

    public Clyde() {
        super(Type.CLYDE, new Point(240, 268), new Point(0, 560));
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
        if (getCurrentMode().equals(Mode.CHASE)) {
            if (Utils.pythagoras(new Point(PacmanGUI.pacman.getRealX(), PacmanGUI.pacman.getRealY()), getRealX(), getRealY()) >= 8 * Utils.RESOLUTION) {
                setCurrentTarget(new Point(PacmanGUI.pacman.getRealX(), PacmanGUI.pacman.getRealY()));
            } else {
                setCurrentTarget(target);
            }
        } else {
            super.calculateTarget();
        }
    }
}
