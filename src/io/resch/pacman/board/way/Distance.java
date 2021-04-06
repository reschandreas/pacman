package io.resch.pacman.board.way;

public class Distance {

    private Direction direction;
    private double distance;

    public Distance(Direction direction, double distance) {
        this.direction = direction;
        this.distance = distance;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
