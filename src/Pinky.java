/**
 * Created by Andreas on 09.04.16.
 * Pink Ghost
 */
public class Pinky extends Ghost {

    public Pinky(String path) {
        super(path);
        startpos[0] = 240;
        startpos[1] = 268;
        setLocation(startpos[0], startpos[1]);
        TARGET[0] = 32;
        TARGET[1] = 0;
    }
}
