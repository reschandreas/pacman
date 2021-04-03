package io.resch.pacman.movable;

import io.resch.pacman.board.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

/**
 * Created by Andreas on 03.04.16.
 */
public abstract class Ghost extends Pacman {

    public static final int SCATTERMODE = 0;
    public static final int CHASEMODE = 1;
    public static final int FRIGHTENEDMODE = 2;
    protected Image image_frightened = null;
    protected Image image_normal = null;

    private Random random = new Random();

    protected int current_mode = SCATTERMODE;
    protected int prev_mode = SCATTERMODE;
    public static int[] targetinhouse = {208, 240};
    public static int[] targetouthouse = {208, 216};

    public boolean insidehouse;

    protected int[] target = new int[2];

    protected int[] current_target = target.clone();

    public Ghost(String path) {
        super(path);
        insidehouse = true;
        speed = 7;
        setEatable(false);
        current_target[0] = target[0];
        current_target[1] = target[1];
        current_mode = SCATTERMODE;
    }

    public Ghost(String path, String frightened) {
        this(path);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(path);

        if (input == null)
            System.out.println("Ghost file not found --- " + path);
        else {
            try {
                this.image_normal = ImageIO.read(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        input = classLoader.getResourceAsStream(frightened);
        if (input == null)
            System.out.println("Ghost file not found --- " + path);
        else {
            try {
                this.image_frightened = ImageIO.read(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isInsidehouse() {
        return insidehouse;
    }

    @Override
    public void start() {
        setLocation(startpos[0], startpos[1]);
        x_speed = x_next = -1;
        y_speed = y_next = 0;
        insidehouse = true;
    }

    public void resume() {
        setLocation(targetinhouse[0], targetinhouse[1]);
        x_speed = x_next = -1;
        y_speed = y_next = 0;
        insidehouse = true;
    }

    private int compareWays(int a, int b) {
        if (a == -1 && b > 0)
            return b;
        if (b == -1 && a > 0)
            return a;
        if (a == -1 && b == -1)
            return -1;
        if (a <= b && a > 0)
            return a;
        else
            return b;
    }

    private int compareWays(int a, int b, int c) {
        int shorter = compareWays(a, b);
        if (shorter == -1 && c > 0)
            return c;
        if (c == -1)
            return shorter;
        if (shorter <= c && shorter > 0)
            return shorter;
        else
            return c;
    }

    private void wayChooser() {
        boolean correct = false;
        int up = 0;
        int down = 0;
        int left = 0;
        int right = 0;
        int way = 0;
        if (getX() == 240 && getY() == 268 || getX() == 176 && getY() == 268) {
            x_speed *= -1;
        }
        if (insidehouse) {
            if (getX() == targetinhouse[0] && getY() == targetinhouse[1]) {
                x_speed = 0;
                y_speed = -1;
                x_next = -1;
                y_next = 0;
            }
            if (getX() == targetouthouse[0] && getY() == targetouthouse[1]) {
                x_speed = -1;
                y_speed = 0;
                x_next = 0;
                y_next = 1;
                setTarget(target);
                insidehouse = false;
            }
        } else {
            Intersection i = intersectionCheck();
            if (i != null) {
                do {
                    if (current_mode != FRIGHTENEDMODE) {
                        if (up != -1)
                            up = pythagoras(current_target[0], current_target[1], i.getX() + i.getWidth() / 2, i.getY());
                        if (down != -1)
                            down = pythagoras(current_target[0], current_target[1], i.getX() + i.getWidth() / 2, i.getY() + i.getHeight());
                        if (left != -1)
                            left = pythagoras(current_target[0], current_target[1], i.getX(), i.getY() + i.getHeight() / 2);
                        if (right != -1)
                            right = pythagoras(current_target[0], current_target[1], i.getX() + i.getWidth(), i.getY() + i.getHeight() / 2);
                    }
                    switch (getX_speed()) {
                        //Von rechts in die Kreuzung
                        case -1: {
                            if (current_mode != FRIGHTENEDMODE) {
                                way = compareWays(up, down, left);
                                if (way == up)
                                    way = 1;
                                else if (way == down)
                                    way = 2;
                                else if (way == left)
                                    way = 3;
                            } else way = random.nextInt(3) + 1;
                            switch (way) {
                                case 1:
                                    if (i.isUp()) {
                                        setY_next(-1);
                                        correct = true;
                                    } else {
                                        up = -1;
                                    }
                                    break;
                                case 2:
                                    if (i.isDown()) {
                                        setY_next(1);
                                        correct = true;
                                    } else {
                                        down = -1;
                                    }
                                    break;
                                case 3:
                                    if (i.isLeft()) {
                                        setX_next(-1);
                                        correct = true;
                                    } else {
                                        left = -1;
                                    }
                                    break;
                            }
                            break;
                        }
                        //Von links in die Kreuzung
                        case 1: {
                            if (current_mode != FRIGHTENEDMODE) {
                                way = compareWays(up, down, right);
                                if (way == up)
                                    way = 1;
                                else if (way == down)
                                    way = 2;
                                else if (way == right)
                                    way = 3;
                            } else way = random.nextInt(3) + 1;
                            switch (way) {
                                case 1:
                                    if (i.isUp()) {
                                        setY_next(-1);
                                        correct = true;
                                    } else {
                                        up = -1;
                                    }
                                    break;
                                case 2:
                                    if (i.isDown()) {
                                        setY_next(1);
                                        correct = true;
                                    } else {
                                        down = -1;
                                    }
                                    break;
                                case 3:
                                    if (i.isRight()) {
                                        setX_next(1);
                                        correct = true;
                                    } else {
                                        right = -1;
                                    }
                                    break;
                            }
                            break;
                        }
                    }
                    switch (getY_speed()) {
                        case -1: {
                            if (current_mode != FRIGHTENEDMODE) {
                                way = compareWays(up, left, right);
                                if (way == up)
                                    way = 1;
                                else if (way == left)
                                    way = 2;
                                else if (way == right)
                                    way = 3;
                            } else way = random.nextInt(3) + 1;
                            switch (way) {
                                case 1:
                                    if (i.isUp()) {
                                        setY_next(-1);
                                        correct = true;
                                    } else
                                        up = -1;
                                    break;
                                case 2:
                                    if (i.isLeft()) {
                                        setX_next(-1);
                                        correct = true;
                                    } else
                                        left = -1;
                                    break;
                                case 3:
                                    if (i.isRight()) {
                                        setX_next(1);
                                        correct = true;
                                    } else
                                        right = -1;
                                    break;
                            }
                            break;
                        }
                        case 1: {
                            if (current_mode != FRIGHTENEDMODE) {
                                way = compareWays(down, left, right);
                                if (way == down)
                                    way = 1;
                                else if (way == left)
                                    way = 2;
                                else if (way == right)
                                    way = 3;
                            } else way = random.nextInt(3) + 1;
                            switch (way) {
                                case 1:
                                    if (i.isDown()) {
                                        setY_next(1);
                                        correct = true;
                                    } else
                                        down = -1;
                                    break;
                                case 2:
                                    if (i.isLeft()) {
                                        setX_next(-1);
                                        correct = true;
                                    } else
                                        left = -1;
                                    break;
                                case 3:
                                    if (i.isRight()) {
                                        setX_next(1);
                                        correct = true;
                                    } else
                                        right = -1;
                                    break;
                            }
                            break;
                        }
                    }
                } while (!correct);
            }
        }
    }

    @Override
    public void move() {
        calculateTarget();
        wayChooser();
        super.move();
    }

    public void calculateTarget() {
        setCurrent_target(target);
    }

    public int getCurrentMode() {
        return current_mode;
    }

    public int getPrev_mode() {
        return prev_mode;
    }

    public void setTarget(int[] target) {
        this.target = target;
    }

    public int[] getTarget() {
        return target;
    }

    public void setCurrent_target(int[] current_target) {
        this.current_target = current_target;
    }

    protected int pythagoras(int targetx, int targety, int startx, int starty) {
        return (int) Math.sqrt((Math.pow(targetx - startx, 2) + Math.pow(targety - starty, 2)));
    }

    public void modes(int mode) {
        if (!insidehouse) {
            prev_mode = current_mode;
            switch (mode) {
                case SCATTERMODE:
                    current_mode = mode;
                    scatterMode();
                    break;
                case CHASEMODE:
                    current_mode = mode;
                    chaseMode();
                    break;
                case FRIGHTENEDMODE:
                    current_mode = mode;
                    frightenedMode();
                    break;
            }
        }
    }

    protected void chaseMode() {
        if (prev_mode == SCATTERMODE)
            reverse();
        setImage(image_normal);
        setEatable(false);
    }

    protected void scatterMode() {
        if (prev_mode == CHASEMODE)
            reverse();
        else {
            setImage(image_normal);
            setEatable(false);
        }
        setCurrent_target(target);

    }

    protected void frightenedMode() {
        reverse();
        setEatable(true);
        setImage(image_frightened);
    }

    private void reverse() {
        x_next = x_speed *= -1;
        y_next = y_speed *= -1;
    }
}