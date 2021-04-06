package io.resch.pacman.movable;

import io.resch.pacman.gui.*;
import io.resch.pacman.utils.Utils;

import java.awt.*;

/**
 * Created by Andreas on 09.04.16.
 * Pink Ghost
 */
public class Pinky extends Ghost {

    public Pinky() {
        super(Type.PINKY, new Point(240, 268), new Point(32, 0));
        currentTarget = new Point(targetInHouse.x, targetInHouse.y);
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
            int x = 0;
            int y = 0;
            switch (PacmanGUI.pacman.getX_speed()) {
                case -1 -> x = -4 * Utils.RESOLUTION;
                case 1 -> x = 4 * Utils.RESOLUTION;
            }
            switch (PacmanGUI.pacman.getY_speed()) {
                case -1 -> y = -4 * Utils.RESOLUTION;
                case 1 -> y = 4 * Utils.RESOLUTION;
            }
            setCurrentTarget(new Point(PacmanGUI.pacman.getX() + x, PacmanGUI.pacman.getY() + y));
        } else {
            super.calculateTarget();
        }
    }
}
