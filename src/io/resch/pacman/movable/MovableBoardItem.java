package io.resch.pacman.movable;

import io.resch.pacman.board.BoardItem;
import io.resch.pacman.board.Intersection;
import io.resch.pacman.board.Wall;
import io.resch.pacman.gui.PacmanGUI;
import io.resch.pacman.utils.Utils;

import java.awt.*;

public class MovableBoardItem extends BoardItem {

    long speed = 5;
    protected Image image_next = null;

    int x_speed = 0;
    int y_speed = 0;
    private boolean eatable = true;

    int[] startpos = new int[2];

    int x_next = 0;
    int y_next = 0;

    public MovableBoardItem(Type type) {
        super(type);
    }

    protected Intersection intersectionCheck() {
        if (PacmanGUI.intersections != null) {
            for (int i = 0; i < PacmanGUI.intersections.size(); i++) {
                if (getX() == PacmanGUI.intersections.get(i).getX() && getY() == PacmanGUI.intersections.get(i).getY()) {
                    return PacmanGUI.intersections.get(i);
                }
            }
        }
        return null;
    }

    public void move() {
        Intersection intersection = intersectionCheck();
        if (intersection != null && (intersection.isLeft() && x_next == -1 || intersection.isRight() && x_next == 1
                || intersection.isUp() && y_next == -1 || intersection.isDown() && y_next == 1)) {
            changeDirection();
            moveHorizontal();
            moveVertical();
        } else {
            intersection = null;
        }
        if (intersection != null)
            return;

        if (y_next == y_speed * -1 || x_next == x_speed * -1) {
            changeDirection();
        }
        if (getX() == 240 && getY() == 268) {
            x_speed = -1;
        }
        moveHorizontal();
        moveVertical();
    }

    private void changeDirection() {
        if (image_next != null)
            image = image_next;
        x_speed = x_next;
        y_speed = y_next;
    }

    protected void moveHorizontal() {
        if (x_speed == 1 || x_speed == -1) {
            if (x_speed == -1 && getX() < 0) {
                setLocation(getParent().getWidth(), getY());
            } else if (x_speed == 1 && getX() + getWidth() + x_speed > Utils.WIDTH) {
                setLocation(x_speed, getY());
            } else if (!(getObjektBei(getX() + x_speed, getY()) instanceof Wall)) {
                setLocation(getX() + x_speed, getY());
            }
        }
    }

    protected void moveVertical() {
        if (y_speed == 1 || y_speed == -1) {
            if (!(getObjektBei(getX(), getY() + y_speed) instanceof Wall)) {
                setLocation(getX(), getY() + y_speed);
            }
        }
    }

    public long getSpeed() {
        return speed;
    }

    public boolean isEatable() {
        return eatable;
    }

    public void setEatable(boolean eatable) {
        this.eatable = eatable;
    }

    public int getX_speed() {
        return x_speed;
    }

    public void setX_speed(int x_speed) {
        this.x_speed = x_speed;
    }

    public int getY_speed() {
        return y_speed;
    }

    public void setY_speed(int y_speed) {
        this.y_speed = y_speed;
    }

    public int getX_next() {
        return x_next;
    }

    public void setX_next(int x_next) {
        this.x_next = x_next;
    }

    public int getY_next() {
        return y_next;
    }

    public void setY_next(int y_next) {
        this.y_next = y_next;
    }

    public int getRealX() {
        return getX() + getWidth() / 2;
    }

    public int getRealY() {
        return getY() + getHeight() / 2;
    }

    public Point getLocation() {
        return new Point(getX(), getY());
    }
}
