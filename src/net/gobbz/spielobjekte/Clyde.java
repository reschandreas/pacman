package net.gobbz.spielobjekte;

import programm.*;
/**
 * Created by Andreas on 09.04.16.
 * Orange Ghost
 */
public class Clyde extends Ghost {

    public Clyde(String path, String frightened) {
        super(path, frightened);
/*        startpos[0] = 176;
        startpos[1] = 268;    */
/*        startpos[0] = 208;
        startpos[1] = 268;*/
        startpos[0] = 208;
        startpos[1] = 216;
        setLocation(startpos[0], startpos[1]);
        target[0] = 0;
        target[1] = 560;
        current_target[0] = targetinhouse[0];
        current_target[1] = targetinhouse[1];
    }

    @Override
    protected void calculateTarget() {
        if (getCurrent_mode() == CHASEMODE) {
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
