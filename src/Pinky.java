/**
 * Created by Andreas on 09.04.16.
 * Pink Ghost
 */
public class Pinky extends Ghost {

    public Pinky(String path) {
        super(path);
/*        startpos[0] = 240;
        startpos[1] = 268;*/
        startpos[0] = 208;
        startpos[1] = 216;
        setLocation(startpos[0], startpos[1]);
        target[0] = current_target[0] = 32;
        target[1] = current_target[1] = 0;
    }
}
