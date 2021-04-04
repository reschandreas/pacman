package io.resch.pacman.movable;

import io.resch.pacman.board.*;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Andreas on 03.04.16.
 */
public abstract class Ghost extends MovableBoardItem {

    public enum Mode {
        SCATTER,
        CHASE,
        FRIGHTENED
    }

    protected Image image_frightened = null;

    private Random random = new Random();

    protected Mode current_mode = Mode.SCATTER;
    protected Mode prev_mode = Mode.CHASE;
    public static int[] targetinhouse = {208, 240};
    public static int[] targetouthouse = {208, 216};

    public boolean insideHouse = true;

    protected int[] target = new int[2];

    protected int[] currentTarget = target.clone();

    public Ghost(Type type) {
        super(type);
        try {
            this.image_frightened = loadImage(Type.FRIGHTENED.label);
        } catch (IOException e) {
            e.printStackTrace();
        }
        speed = 7;
        setEatable(false);
        currentTarget[0] = target[0];
        currentTarget[1] = target[1];
        current_mode = Mode.SCATTER;
    }

    public boolean isInsideHouse() {
        return insideHouse;
    }

    public void start() {
        setLocation(startpos[0], startpos[1]);
        x_speed = x_next = -1;
        y_speed = y_next = 0;
        insideHouse = true;
    }

    public void resume() {
        setLocation(targetinhouse[0], targetinhouse[1]);
        x_speed = x_next = -1;
        y_speed = y_next = 0;
        insideHouse = true;
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
        if (insideHouse) {
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
                insideHouse = false;
            }
        } else {
            Intersection i = intersectionCheck();
            if (i != null) {
                do {
                    if (current_mode != Mode.FRIGHTENED) {
                        if (up != -1)
                            up = pythagoras(currentTarget[0], currentTarget[1], i.getX() + i.getWidth() / 2, i.getY());
                        if (down != -1)
                            down = pythagoras(currentTarget[0], currentTarget[1], i.getX() + i.getWidth() / 2, i.getY() + i.getHeight());
                        if (left != -1)
                            left = pythagoras(currentTarget[0], currentTarget[1], i.getX(), i.getY() + i.getHeight() / 2);
                        if (right != -1)
                            right = pythagoras(currentTarget[0], currentTarget[1], i.getX() + i.getWidth(), i.getY() + i.getHeight() / 2);
                    }
                    //Von rechts in die Kreuzung
                    //Von links in die Kreuzung
                    switch (getX_speed()) {
                        case -1 -> {
                            if (current_mode != Mode.FRIGHTENED) {
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
                        case 1 -> {
                            if (current_mode != Mode.FRIGHTENED) {
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
                        }
                    }
                    switch (getY_speed()) {
                        case -1 -> {
                            if (current_mode != Mode.FRIGHTENED) {
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
                        case 1 -> {
                            if (current_mode != Mode.FRIGHTENED) {
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
        setCurrentTarget(target);
    }

    public Mode getCurrentMode() {
        return current_mode;
    }

    public Mode getPrev_mode() {
        return prev_mode;
    }

    public void setTarget(int[] target) {
        this.target = target;
    }

    public int[] getTarget() {
        return target;
    }

    public void setCurrentTarget(int[] current_target) {
        this.currentTarget = current_target;
    }

    protected int pythagoras(int targetx, int targety, int startx, int starty) {
        return (int) Math.sqrt((Math.pow(targetx - startx, 2) + Math.pow(targety - starty, 2)));
    }

    public void changeModeTo(Mode mode) {
        if (insideHouse)
            return;

        setMode(mode);
        switch (mode) {
            case SCATTER -> scatterMode();
            case CHASE -> chaseMode();
            case FRIGHTENED -> frightenedMode();
        }
    }

    private void setMode(Mode mode) {
        prev_mode = current_mode;
        this.current_mode = mode;
    }

    protected void chaseMode() {
        setImage(image);
        if (prev_mode == Mode.SCATTER)
            reverse();
        setEatable(false);
    }

    protected void scatterMode() {
        setImage(image);
        if (prev_mode == Mode.CHASE)
            reverse();
        else {
            setEatable(false);
        }
        setCurrentTarget(target);
    }

    protected void frightenedMode() {
        setImage(image_frightened);
        reverse();
        setEatable(true);
    }

    private void reverse() {
        x_next = x_speed *= -1;
        y_next = y_speed *= -1;
    }
}