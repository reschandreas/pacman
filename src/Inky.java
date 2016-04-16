/**
 * Created by Andreas on 09.04.16.
 * Blue Ghost
 */
public class Inky extends Ghost {

    private final int[] STARTPOS = {208, 268};

    public Inky(String path) {
        super(path);
        setLocation(STARTPOS[0], STARTPOS[1]);
    }
}
