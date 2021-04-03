package io.resch.pacman.movable;

import io.resch.pacman.gui.*;

/**
 * Created by Andreas on 09.04.16.
 * Red Ghost
 */
public class Blinky extends Ghost {

    public Blinky() {
        super("images/ghost_red.png", "images/ghost_frightened.png");
        insidehouse = false;
        startpos[0] = 208;
        startpos[1] = 216;
        setLocation(startpos[0], startpos[1]);
        target[0] = current_target[0] = 400;
        target[1] = current_target[1] = 0;
    }


    @Override
    protected void chaseMode() {
        super.chaseMode();
        setCurrent_target(new int[] {PacmanGUI.pacman.getRealX(), PacmanGUI.pacman.getRealY()});
    }

    @Override
    public void calculateTarget() {
        if (getCurrentMode() == CHASEMODE) {
            setCurrent_target(new int[]{PacmanGUI.pacman.getRealX(), PacmanGUI.pacman.getRealY()});
        } else {
            super.calculateTarget();
        }
    }
}
