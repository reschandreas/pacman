/**
 * Created by Andreas on 09.04.16.
 * Red Ghost
 */
public class Blinky extends Ghost {

    public Blinky(String path) {
        super(path);
        startpos[0] = 216;
        startpos[1] = 216;
        setLocation(startpos[0], startpos[1]);
        TARGET[0] = 400;
        TARGET[1] = 0;
    }
}
