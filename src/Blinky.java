/**
 * Created by Andreas on 09.04.16.
 * Red Ghost
 */
public class Blinky extends Pacman {

    private final int[] STARTPOS = {208, 216};

    public Blinky(String path) {
        super(path);
        setBounds(STARTPOS[0], STARTPOS[1], getWidth(), getHeight());
    }
}
