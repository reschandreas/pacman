package io.resch.pacman.board;

import io.resch.pacman.movable.Ghost;

public class Speed {

    public enum Type {
        NORMAL,
        DOT_NORMAL,
        FRIGHT,
        DOT_FRIGHT,
        GHOST_NORMAL,
        GHOST_FRIGHT,
        GHOST_TUNNEL
    }

    private Type type;
    private double value;

    public Speed(Type type, double value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public static Speed.Type getCurrentType(boolean eaten, boolean ghostsFrightened) {
        if (!eaten && ghostsFrightened)
            return Speed.Type.FRIGHT;
        if (eaten && ghostsFrightened)
            return Speed.Type.DOT_FRIGHT;
        if (!eaten) {
            return Speed.Type.NORMAL;
        } else return Speed.Type.DOT_NORMAL;
    }
}
