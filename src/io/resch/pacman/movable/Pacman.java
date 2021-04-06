package io.resch.pacman.movable;

import io.resch.pacman.board.Intersection;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Andreas on 03.04.16.
 */
public class Pacman extends MovableBoardItem {

    private long points = 0;

    private int lives = 3;

    private Image image_right = null;
    private Image image_left = null;
    private Image image_up = null;
    private Image image_down = null;

    private boolean eatable = true;

    public Pacman() {
        this(Type.PACMAN);
        speed = 5;
    }

    protected Pacman(Type type) {
        super(type, new Point(208, 408));
        setImages();
        setEatable(true);
        speed = 5;
        x_speed = -1;
        x_next = -1;
    }

    private void setImages() {
        try {
            this.image_up = loadImage("images/pacman_up.png");
            this.image_down = loadImage("images/pacman_down.png");
            this.image_left = loadImage("images/pacman_left.png");
            this.image_right = loadImage("images/pacman_right.png");
            this.image = this.image_left;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        setLocation(startPosition);
        x_speed = x_next = -1;
        y_speed = y_next = 0;
        image = this.image_next = image_left;
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

    public int getLives() {
        return lives;
    }

    public void deductLife() {
        lives--;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getX_next() {
        return x_next;
    }

    @Override
    public void setX_next(int x_next) {
        this.x_next = x_next;
        if (x_next == -1) {
            if (image_left != null)
                image_next = image_left;
        } else if (image_right != null)
            image_next = image_right;
        y_next = 0;
    }

    public Image getImage_down() {
        return image_down;
    }

    public Image getImage_left() {
        return image_left;
    }

    public Image getImage_right() {
        return image_right;
    }

    public Image getImage_up() {
        return image_up;
    }

    public int getX_speed() {
        return x_speed;
    }

    public void setX_speed(int x_speed) {
        this.x_speed = x_speed;
    }

    public int getY_next() {
        return y_next;
    }

    @Override
    public void setY_next(int y_next) {
        this.y_next = y_next;
        if (y_next == -1) {
            if (image_up != null)
                image_next = image_up;
        } else if (image_down != null)
            image_next = image_down;
        x_next = 0;
    }

    public int getY_speed() {
        return y_speed;
    }

    public void setY_speed(int y_speed) {
        this.y_speed = y_speed;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    @Override
    public void move() {
        Intersection intersection = intersectionCheck();
        if (intersection != null && (intersection.isLeft() && x_next == -1 || intersection.isRight() && x_next == 1
                || intersection.isUp(this) && y_next == -1 || intersection.isDown() && y_next == 1)) {
            changeDirection();
            moveHorizontal();
            moveVertical();
        } else {
            intersection = null;
        }
        if (intersection != null)
            return;
        super.move();
    }
}