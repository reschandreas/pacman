package io.resch.pacman.movable;

import io.resch.pacman.gui.*;

/**
 * Created by Andreas on 09.04.16.
 * Red Ghost
 */
public class Blinky extends Ghost {

    public Blinky() {
        super(Type.BLINKY);
        insideHouse = false;
        startpos[0] = 208;
        startpos[1] = 216;
        setLocation(startpos[0], startpos[1]);
        target[0] = currentTarget[0] = 400;
        target[1] = currentTarget[1] = 0;
    }


    @Override
    protected void chaseMode() {
        super.chaseMode();
        setCurrentTarget(new int[] {PacmanGUI.pacman.getRealX(), PacmanGUI.pacman.getRealY()});
    }

    @Override
    public void calculateTarget() {
        if (getCurrentMode() == Mode.CHASE) {
            setCurrentTarget(new int[]{PacmanGUI.pacman.getRealX(), PacmanGUI.pacman.getRealY()});
        } else {
            super.calculateTarget();
        }
    }
}
