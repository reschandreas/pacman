/**
 * Created by Andreas on 03.04.16.
 */
public class Dot extends Wall {

    public Dot(int x, int y, String path) {
        super(path);
        setBounds(x, y, getWidth(), getHeight());
    }


}
