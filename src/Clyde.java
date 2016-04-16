/**
 * Created by Andreas on 09.04.16.
 * Orange Ghost
 */
public class Clyde extends Ghost {

    private final int[] STARTPOS = {176, 268};

    public Clyde(String path) {
        super(path);
        setLocation(STARTPOS[0], STARTPOS[1]);
    }
}
