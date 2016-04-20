/**
 * Created by Andreas on 09.04.16.
 * Orange Ghost
 */
public class Clyde extends Ghost {

    public Clyde(String path) {
        super(path);
/*        startpos[0] = 176;
        startpos[1] = 268;*/
        startpos[0] = 208;
        startpos[1] = 216;
        setLocation(startpos[0], startpos[1]);
        target[0] = current_target[0] = 0;
        target[1] = current_target[1] = 560;
    }
}
