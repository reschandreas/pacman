package io.resch.pacman.board;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Difficulty {

    private final List<Wave> waves;

    public Difficulty(Wave... waves) {
        this.waves = Arrays.asList(waves.clone());
    }

    public Wave getCurrentWave(long time) {
        List<Wave> waves = this.waves
                        .stream()
                        .takeWhile(w -> w.getOffset() <= time)
                        .collect(Collectors.toList());
        return waves.get(waves.size() - 1);
    }
}
