/**
 * Created by Andreas on 21.04.16.
 */
public class Timer {

    private static long startmillis = 0;

    public static void start() {
        startmillis = System.currentTimeMillis();
    }

    public static long getTime() {
        return (System.currentTimeMillis() - startmillis);
    }

}
