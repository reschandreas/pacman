package net.gobbz.spielobjekte;

import programm.*;

/**
 * Created by Andreas on 09.04.16.
 * Red Ghost
 */
public class Blinky extends Ghost {

    public Blinky(String path, String frightened) {
        super(path, frightened);
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
    protected void calculateTarget() {
        if (getCurrent_mode() == CHASEMODE) {
            setCurrent_target(new int[]{PacmanGUI.pacman.getRealX(), PacmanGUI.pacman.getRealY()});
        } else {
            super.calculateTarget();
        }
    }
}
