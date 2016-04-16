/**
 * Created by Andreas on 09.04.16.
 * Pink Ghost
 */
public class Pinky extends Ghost {

    private final int[] STARTPOS = {240, 268};

    public Pinky(String path) {
        super(path);
        setLocation(STARTPOS[0], STARTPOS[1]);
    }
}
