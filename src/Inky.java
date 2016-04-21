/**
 * Created by Andreas on 09.04.16.
 * Blue Ghost
 */
public class Inky extends Ghost {

    public Inky(String path) {
        super(path);
/*        startpos[0] = 208;
        startpos[1] = 268;*/
        startpos[0] = 208;
        startpos[1] = 216;
        setLocation(startpos[0], startpos[1]);
        target[0] = current_target[0] = 432;
        target[1] = current_target[1] = 560;
    }

    @Override
    protected void chaseMode() {
        int targetx = 0;
        int targety = 0;
        switch (PacmanGUI.pacman.getX_speed()) {
            case -1:
                targetx = - 2 * PacmanGUI.RESOLUTION;
                break;
            case 1:
                targetx = 2 * PacmanGUI.RESOLUTION;
                break;
        }
        switch (PacmanGUI.pacman.getY_speed()) {
            case -1:
                targety = - 2 * PacmanGUI.RESOLUTION;
                break;
            case 1:
                targety = 2 * PacmanGUI.RESOLUTION;
                break;
        }
        targetx += PacmanGUI.pacman.getRealX();
        targety += PacmanGUI.pacman.getRealY();
        int x_blinky = PacmanGUI.ghosts.get(0).getX();
        int y_blinky = PacmanGUI.ghosts.get(0).getY();
        int newtargetx = x_blinky + 2 * (targetx - x_blinky);
        int newtargety = y_blinky + 2 * (targety - y_blinky);
        setCurrent_target(new int[] {newtargetx, newtargety});

    }

    @Override
    protected void frightenedMode() {
        super.frightenedMode();
    }

    @Override
    protected void scatterMode() {
        super.scatterMode();
    }
}
