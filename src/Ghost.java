import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Andreas on 03.04.16.
 */
public abstract class Ghost extends Pacman {

    protected final int SCATTERMODE = 0;
    protected final int CHASEMODE = 1;
    protected final int FRIGHTENEDMODE = 2;

    protected int current_mode = SCATTERMODE;

    protected int[] target = new int[2];

    protected int[] current_target = target.clone();

    public Ghost(String path) {
        super(path, path, path, path);
        current_target[0] = target[0];
        current_target[1] = target[1];
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

    protected void wayChooser() {
        Intersection i = intersectionCheck();
        if (i != null) {
            boolean correct = false;
            int up = 0;
            int down = 0;
            int left = 0;
            int right = 0;
            do {
                if (up != -1)
                    up = pythagoras(i.getX() + i.getWidth() / 2, i.getY());
                if (down != -1)
                    down = pythagoras(i.getX() + i.getWidth() / 2, i.getY() + i.getHeight());
                if (left != -1)
                    left = pythagoras(i.getX(), i.getY() + i.getHeight() / 2);
                if (right != -1)
                    right = pythagoras(i.getX() + i.getWidth(), i.getY() + i.getHeight() / 2);
                switch (getX_speed()) {
                    //Von rechts in die Kreuzung
                    case -1: {
                        int way = compareWays(up, down, left);
                        if (way == up)
                            way = 1;
                        else if (way == down)
                            way = 2;
                        else if (way == left)
                            way = 3;
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
                        int way = compareWays(up, down, right);
                        if (way == up)
                            way = 1;
                        else if (way == down)
                            way = 2;
                        else if (way == right)
                            way = 3;
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
                        int way = compareWays(up, left, right);
                        if (way == up)
                            way = 1;
                        else if (way == left)
                            way = 2;
                        else if (way == right)
                            way = 3;
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
                        int way = compareWays(down, left, right);
                        if (way == down)
                            way = 1;
                        else if (way == left)
                            way = 2;
                        else if (way == right)
                            way = 3;
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

    @Override
    public void move() {
        wayChooser();
        super.move();
    }

    public void setCurrent_target(int[] current_target) {
        this.current_target = current_target;
    }

    protected int pythagoras(int x, int y) {
        return (int) Math.sqrt((Math.pow(current_target[0] - x, 2) + Math.pow(current_target[1] - y, 2)));
    }

    protected void changeMode(int mode) {
        switch (mode) {
            case SCATTERMODE:
                current_target = target.clone();
                break;
            case CHASEMODE:

                break;
            case FRIGHTENEDMODE:

                break;
        }
    }

    private void reverse() {
        x_next = x_speed *= -1;
        y_next = y_speed *= -1;
    }
}
