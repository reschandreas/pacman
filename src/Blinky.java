/**
 * Created by Andreas on 09.04.16.
 * Red Ghost
 */
public class Blinky extends Ghost {

    public Blinky(String path) {
        super(path);
        startpos[0] = 208;
        startpos[1] = 216;
        setLocation(startpos[0], startpos[1]);
        target[0] = current_target[0] = 400;
        target[1] = current_target[1] = 0;
    }
}
