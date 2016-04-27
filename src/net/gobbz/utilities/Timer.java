package net.gobbz.utilities;

/**
 * Created by Andreas on 21.04.16.
 */
public class Timer {

    private long startmillis = 0;
    private long pausedmillis = 0;
    private boolean paused = false;

    public Timer() {
    }

    public void start() {
        startmillis = System.currentTimeMillis();
    }

    public long getTime() {
        return (System.currentTimeMillis() - startmillis);
    }

    public void pause() {
        paused = true;
        startmillis = System.currentTimeMillis() - startmillis;
        pausedmillis = startmillis;
    }

    public boolean isPaused() {
        return paused;
    }

    public void restart() {
        paused = false;
        startmillis = System.currentTimeMillis() - pausedmillis;
    }

    public void stop() {
        startmillis = 0;
        pausedmillis = 0;
    }

}
