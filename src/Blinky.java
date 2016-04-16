/**
 * Created by Andreas on 09.04.16.
 * Red Ghost
 */
public class Blinky extends Ghost {

    private final int[] STARTPOS = {216, 216};

    public Blinky(String path) {
        super(path);
        setLocation(STARTPOS[0], STARTPOS[1]);
    }
}
