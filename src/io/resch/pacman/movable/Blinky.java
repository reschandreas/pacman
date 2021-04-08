package io.resch.pacman.movable;

import io.resch.pacman.gui.*;

import java.awt.*;

/**
 * Created by Andreas on 09.04.16.
 * Red Ghost
 */
public class Blinky extends Ghost {

    public Blinky() {
        super(Type.BLINKY, new Point(208, 216), new Point(400, 0));
        insideHouse = false;
    }

    @Override
    protected void chaseMode() {
        super.chaseMode();
        setCurrentTarget(new Point(PacmanGUI.pacman.getRealX(), PacmanGUI.pacman.getRealY()));
    }

    @Override
    public void calculateTarget() {
        if (getCurrentMode().equals(Mode.CHASE)) {
            setCurrentTarget(new Point(PacmanGUI.pacman.getRealX(), PacmanGUI.pacman.getRealY()));
        } else {
            super.calculateTarget();
        }
    }

    @Override
    public Ghost reincarnate() {
        return new Blinky();
    }
}
