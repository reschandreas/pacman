package io.resch.pacman.board;

public class Player {

    private final String name;
    private int points;
    private int level;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getLevel() {
        return level;
    }

    public void levelUp() {
        this.level++;
    }
}
