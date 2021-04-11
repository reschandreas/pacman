package io.resch.pacman.movable;

import io.resch.pacman.gui.*;
import io.resch.pacman.utils.Utils;

import java.awt.*;

/**
 * Created by Andreas on 09.04.16.
 * Blue Ghost
 */
public class Inky extends Ghost {

    public Inky() {
        super(Type.INKY, new Point(176, 268), new Point(432, 560));
        currentTarget = new Point(targetInHouse.x, targetInHouse.y);
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
    public void move() {
        if (isInsideHouse()) {
            setCurrentTarget(Ghost.targetInHouse);
        } else if (getTarget().equals(Ghost.targetOutHouse)) {
            calculateTarget();
        }
        super.move();
    }

    @Override
    public void calculateTarget() {
        if (getCurrentMode().equals(Mode.CHASE)) {
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
            setCurrentTarget(new Point(x_blinky + (2 * (targetx - x_blinky)), y_blinky + (2 * (targety - y_blinky))));
        } else {
            super.calculateTarget();
        }
    }

    @Override
    public Ghost reincarnate() {
        return new Inky();
    }
}
