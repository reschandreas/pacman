package io.resch.pacman.board;

public record SleepTime(long milliSeconds, int nanoSeconds) {

    public static SleepTime create(double speed) {
        long milliSeconds = (long) speed;
        int nanoSeconds = (int) ((speed - milliSeconds) * 1000);
        return new SleepTime(milliSeconds, nanoSeconds);
    }
}
