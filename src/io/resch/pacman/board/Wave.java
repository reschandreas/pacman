package io.resch.pacman.board;


import io.resch.pacman.movable.Ghost;

import java.util.List;
import java.util.Objects;

public record Wave(long offset, Ghost.Mode mode) {

    public long getOffset() {
        return offset;
    }

    public void setModes(List<Ghost> ghosts) {
        ghosts.stream()
                .filter(Objects::nonNull)
                .filter(ghost -> !ghost.getCurrentMode().equals(Ghost.Mode.FRIGHTENED))
                .forEach(ghost -> ghost.changeModeTo(mode));
    }
}

