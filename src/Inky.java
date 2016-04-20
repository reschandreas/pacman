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
}
