import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Andreas on 03.04.16.
 */
public abstract class Ghost extends Pacman {

    protected final int SCATTERMODE = 0;
    protected final int CHASEMODE = 1;
    protected final int FRIGHTENEDMODE = 2;

    protected int current_mode = SCATTERMODE;

    protected final int[] TARGET = new int[2];

    protected int[] current_target = TARGET.clone();

    public Ghost(String path) {
        super(path);
    }

    protected void wayChooser() {
        Intersection i = PacmanGUI.intersectionCheck();
        if (i != null) {
            //Oberhalb der Intersection
            int up = pythagoras(i.getX() + i.getX() / 2, i.getY());
            int down = pythagoras(i.getX() + i.getX() / 2, i.getY() + i.getHeight());
            int left = pythagoras(i.getX(), i.getY() + i.getHeight()/2);
            int right = pythagoras(i.getX() + i.getWidth(), i.getY() + i.getHeight()/2);
            if ((up < down && up < left && up < right) && i.isUp()) {
                setY_next(-1);
            }
        }
    }

    protected int pythagoras(int x, int y) {
        return (int) Math.sqrt((double) (Math.pow(current_target[0] - x, 2) + Math.pow(getRealY() - current_target[1], 2)));
    }

    protected void changeMode(int mode) {
        switch (mode) {
            case SCATTERMODE:
                current_target = TARGET.clone();
                break;
            case CHASEMODE:

                break;
            case FRIGHTENEDMODE:

                break;
        }
    }

    private void reverse() {
        x_next = x_speed *= -1;
        y_next = y_speed *= -1;
    }
}
