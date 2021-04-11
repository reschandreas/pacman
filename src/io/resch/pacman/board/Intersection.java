package io.resch.pacman.board;

import io.resch.pacman.board.way.Direction;
import io.resch.pacman.board.way.Distance;
import io.resch.pacman.gui.*;
import io.resch.pacman.movable.Ghost;
import io.resch.pacman.movable.MovableBoardItem;
import io.resch.pacman.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.resch.pacman.board.way.Direction.*;
import static io.resch.pacman.board.way.Direction.RIGHT;

/**
 * Created by Andreas on 10.04.16.
 */
public class Intersection extends JComponent {

    private final List<Direction> directions = new ArrayList<>(4);
    private final boolean upForbiddenForGhosts;

    public Intersection(Point location, boolean[] allowedDirections, boolean upforbiddenForGhosts) {
        setBounds(location.x, location.y, 2 * Utils.RESOLUTION, 2 * Utils.RESOLUTION);
        if (allowedDirections[0]) {
            directions.add(Direction.UP);
        }
        if (allowedDirections[1]) {
            directions.add(Direction.DOWN);
        }
        if (allowedDirections[2]) {
            directions.add(Direction.RIGHT);
        }
        if (allowedDirections[3]) {
            directions.add(Direction.LEFT);
        }
        this.upForbiddenForGhosts = upforbiddenForGhosts;
        setVisible(false);
    }

    public static Intersection create(String[] strings) {
        boolean[] allowedDirections = new boolean[4];
        boolean upforbiddenForGhosts = false;
        for (int i = 2; i < allowedDirections.length + 2; i++) {
            allowedDirections[i - 2] = Boolean.parseBoolean(strings[i]);
        }
        if (strings.length > 6) {
            upforbiddenForGhosts = "U".equals(strings[6]);
        }
        return new Intersection(new Point(Integer.parseInt(strings[0]), Integer.parseInt(strings[1])), allowedDirections, upforbiddenForGhosts);
    }

    public void paint(Graphics g) {
        if (upForbiddenForGhosts)
            g.setColor(Color.blue);
        else
            g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public boolean isDown() {
        return directions.contains(Direction.DOWN);
    }

    public boolean isLeft() {
        return directions.contains(Direction.LEFT);
    }

    public boolean isRight() {
        return directions.contains(Direction.RIGHT);
    }

    public boolean isUp(MovableBoardItem requester) {
        if (upForbiddenForGhosts && requester instanceof Ghost) {
            return false;
        }
        return directions.contains(Direction.UP);
    }

    public List<Distance> getAllowedDirections(MovableBoardItem requester, Point target) {
        List<Distance> distances = new ArrayList<>(4);
        for (Direction direction : directions) {
            switch (direction) {
                case UP -> {
                    if (isUp(requester)) {
                        distances.add(new Distance(UP, Utils.pythagoras(target, getX() + getWidth() / 2, getY())));
                    }
                }
                case DOWN -> distances.add(new Distance(DOWN, Utils.pythagoras(target, getX() + getWidth() / 2, getY() + getHeight())));
                case LEFT -> distances.add(new Distance(LEFT, Utils.pythagoras(target, getX(), getY() + getHeight() / 2)));
                case RIGHT -> distances.add(new Distance(RIGHT, Utils.pythagoras(target, getX() + getWidth(), getY() + getHeight() / 2)));
            }
        }
        return distances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intersection that = (Intersection) o;
        return upForbiddenForGhosts == that.upForbiddenForGhosts && Objects.equals(directions, that.directions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(directions, upForbiddenForGhosts);
    }

    public static List<Intersection> readIntersectionsFile() {
        return Utils.readFile("data/intersections.data")
                .stream()
                .map(line -> Intersection.create(line.split(";")))
                .collect(Collectors.toList());
    }
}
