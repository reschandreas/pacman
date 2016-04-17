/**
 * Created by Andreas on 09.04.16.
 * Orange Ghost
 */
public class Clyde extends Ghost {

    public Clyde(String path) {
        super(path);
        startpos[0] = 176;
        startpos[1] = 268;
        setLocation(startpos[0], startpos[1]);
        TARGET[0] = 0;
        TARGET[1] = 560;
    }
}
