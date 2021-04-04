package io.resch.pacman.board;


import io.resch.pacman.movable.Ghost;

import java.util.List;

public class Wave {

    private final long offset;
    private final Ghost.Mode mode;

    public Wave(long offset, Ghost.Mode mode) {
        this.offset = offset;
        this.mode = mode;
    }

    public long getOffset() {
        return offset;
    }

    public void setModes(List<Ghost> ghosts) {
        ghosts.forEach(ghost -> ghost.changeModeTo(mode));
    }
}

