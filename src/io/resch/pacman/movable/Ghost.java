package io.resch.pacman.movable;

import io.resch.pacman.board.*;
import io.resch.pacman.board.way.Direction;
import io.resch.pacman.board.way.Distance;

import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static io.resch.pacman.board.way.Direction.*;
import static io.resch.pacman.board.way.Navigator.*;

/**
 * Created by Andreas on 03.04.16.
 */
public abstract class Ghost extends MovableBoardItem {

    public enum Mode {
        SCATTER,
        CHASE,
        FRIGHTENED
    }

    protected Image imageFrightened = null;
    protected Image imageNormal = null;

    protected Mode currentMode = Mode.SCATTER;
    protected Mode previousMode = Mode.CHASE;
    public static final Point targetInHouse = new Point(208, 240);
    public static final Point targetOutHouse = new Point(208, 216);

    public boolean insideHouse = true;

    protected Point target;

    protected Point currentTarget;

    public Ghost(Type type, Point startPosition, Point target) {
        super(type, startPosition);
        try {
            this.imageNormal = loadImage(type.label);
            this.imageFrightened = loadImage(Type.FRIGHTENED.label);
        } catch (IOException e) {
            e.printStackTrace();
        }
        speed = 7;
        setEatable(false);
        this.target = new Point(target.x, target.y);
        currentTarget = new Point(target.x, target.y);
        currentMode = Mode.SCATTER;
    }

    public boolean isInsideHouse() {
        return insideHouse;
    }

    public void start() {
        setLocation(startPosition);
        x_speed = x_next = -1;
        y_speed = y_next = 0;
        insideHouse = true;
    }

    public void resume() {
        setLocation(targetInHouse);
        x_speed = x_next = -1;
        y_speed = y_next = 0;
        insideHouse = true;
    }

    public void getsEaten() {
        setImage(imageNormal);
        changeModeTo(getPreviousMode());
        startPosition = new Point(240, 268);
        start();
    }

    private void wayChooser() {
        // in the house we have to move away from the walls
        if (getX() == 240 && getY() == 268 || getX() == 176 && getY() == 268) {
            x_speed *= -1;
        }

        if (getX() == targetOutHouse.x && getY() == targetOutHouse.y) {
            x_speed = -1;
            y_speed = 0;
            x_next = 0;
            y_next = 1;
            setTarget(target);
            insideHouse = false;
        }

        Intersection i = intersectionCheck();
        Direction currentDirection = getCurrentDirection();
        Distance way;
        List<Distance> distances;
        if (i != null) {
            if (i.getX() == 88 && i.getY() == 120) {
                way = new Distance(RIGHT, 0);
            } else {
                distances = i.getAllowedDirections(this, currentTarget)
                        .stream()
                        .filter(d -> !d.direction().equals(getOpposite(currentDirection)))
                        .collect(Collectors.toList());
                if (!currentMode.equals(Mode.FRIGHTENED)) {
                    way = getBestDirection(distances);
                } else {
                    way = getRandomDirection(distances);
                }
                if (way == null) {
                    return;
                }
            }
            switch (way.direction()) {
                case UP -> goUp();
                case DOWN -> goDown();
                case LEFT -> goLeft();
                case RIGHT -> goRight();
            }
            changeDirection();
        }
    }

    @Override
    public void move() {
        calculateTarget();
        wayChooser();
        super.move();
    }

    public void calculateTarget() {
        setCurrentTarget(target);
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    public Mode getPreviousMode() {
        return previousMode;
    }

    public void setTarget(Point target) {
        this.target = target;
    }

    public Point getTarget() {
        return target;
    }

    public void setCurrentTarget(Point currentTarget) {
        this.currentTarget = currentTarget;
    }

    public void changeModeTo(Mode mode) {
        if (mode.equals(currentMode)) {
            return;
        }
        if (insideHouse)
            return;

        setMode(mode);
        switch (mode) {
            case SCATTER -> scatterMode();
            case CHASE -> chaseMode();
            case FRIGHTENED -> frightenedMode();
        }
        this.repaint();
    }

    private void setMode(Mode mode) {
        if (!currentMode.equals(Mode.FRIGHTENED))
            previousMode = currentMode;
        this.currentMode = mode;
    }

    protected void chaseMode() {
        setImage(imageNormal);
        repaint();
        if (previousMode.equals(Mode.SCATTER))
            reverse();
        setEatable(false);
    }

    protected void scatterMode() {
        setImage(imageNormal);
        repaint();
        if (previousMode.equals(Mode.CHASE))
            reverse();
        else {
            setEatable(false);
        }
        setCurrentTarget(target);
    }

    protected void frightenedMode() {
        setImage(imageFrightened);
        repaint();
        reverse();
        setEatable(true);
    }

    private void reverse() {
        x_next = x_speed *= -1;
        y_next = y_speed *= -1;
    }

    @Override
    public boolean isEatable() {
        return currentMode.equals(Mode.FRIGHTENED);
    }

    public Ghost reincarnate() {
        return null;
    }
}