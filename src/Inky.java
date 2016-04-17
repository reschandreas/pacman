/**
 * Created by Andreas on 09.04.16.
 * Blue Ghost
 */
public class Inky extends Ghost {

    public Inky(String path) {
        super(path);
        startpos[0] = 208;
        startpos[1] = 268;
        setLocation(startpos[0], startpos[1]);
        TARGET[0] = 432;
        TARGET[1] = 560;
    }
}
