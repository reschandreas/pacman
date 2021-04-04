package io.resch.pacman.movable;

import io.resch.pacman.gui.*;

/**
 * Created by Andreas on 09.04.16.
 * Pink Ghost
 */
public class Pinky extends Ghost {

    public Pinky() {
        super(Type.PINKY);
        startpos[0] = 240;
        startpos[1] = 268;
        setLocation(startpos[0], startpos[1]);
        target[0] = 32;
        target[1] = 0;
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
            int x = 0;
            int y = 0;
            switch (PacmanGUI.pacman.getX_speed()) {
                case -1 -> x = -4 * PacmanGUI.RESOLUTION;
                case 1 -> x = 4 * PacmanGUI.RESOLUTION;
            }
            switch (PacmanGUI.pacman.getY_speed()) {
                case -1 -> y = -4 * PacmanGUI.RESOLUTION;
                case 1 -> y = 4 * PacmanGUI.RESOLUTION;
            }
            setCurrentTarget(new int[]{PacmanGUI.pacman.getX() + x, PacmanGUI.pacman.getY() + y});
        } else {
            super.calculateTarget();
        }
    }
}
