package io.resch.pacman.board;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SpeedLimits {

    private List<Speed> speeds;

    public SpeedLimits(Speed... speeds) {
        this.speeds = Arrays.asList(speeds.clone());
    }

    public Speed getSpeedByType(Speed.Type type) {
        Optional<Speed> speed = this.speeds
                .stream()
                .filter(s -> s.getType().equals(type))
                .findFirst();
        return speed.get();
    }
}
