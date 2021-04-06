package io.resch.pacman.board;

public class BoardControl {

    private static final BoardControl instance;

    private BoardControl() {
    }

    static {
        instance = new BoardControl();
    }
}
