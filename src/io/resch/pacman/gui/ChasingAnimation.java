package io.resch.pacman.gui;

import io.resch.pacman.movable.*;

import java.awt.*;
import java.util.ArrayList;

public class ChasingAnimation implements Runnable {

    private final ArrayList<MovableBoardItem> items;
    private final Point upperLeft = new Point(0, 0);
    private final Point upperRight = new Point(416, 0);
    private final Point lowerLeft = new Point(0, 544);
    private final Point lowerRight = new Point(416, 544);

    public ChasingAnimation(Container container) {
        items = new ArrayList<>();
        Pacman pacman = new Pacman();
        pacman.setImage(pacman.getImage_right());

        items.add(pacman);
        items.add(new Blinky());
        items.add(new Inky());
        items.add(new Pinky());
        items.add(new Clyde());

        for (MovableBoardItem item : items) {
            container.add(item);
            item.setLocation(0, 0);
            item.setX_speed(1);
            item.setX_next(1);
        }
    }

    @Override
    public synchronized void run() {
        final long startTime = System.currentTimeMillis();
        long speed = items.get(0).getSpeed();
        while (!Thread.interrupted()) {
            for (int i = 1; i < items.size(); i++) {
                if (System.currentTimeMillis() - startTime >= 750 + i * 350L) {
                    items.get(i).move();
                }
            }
            items.get(0).move();
            for (MovableBoardItem item : items) {
                handleCornersOfChasingAnimation(item);
            }

            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleCornersOfChasingAnimation(MovableBoardItem item) {
        Point location = item.getLocation();

        if (upperLeft.equals(location)) {
            handleUpperLeftCorner(item);
            return;
        }
        if (upperRight.equals(location)) {
            handleUpperRightCorner(item);
            return;
        }
        if (lowerRight.equals(location)) {
            handleLowerRightCorner(item);
            return;
        }
        if (lowerLeft.equals(location)) {
            handleLowerLeftCorner(item);
        }
    }

    private void handleUpperLeftCorner(MovableBoardItem item) {
        item.setX_speed(1);
        item.setX_next(1);
        item.setY_speed(0);
        if (item instanceof Pacman)
            item.setImage(((Pacman) item).getImage_right());
    }

    private void handleUpperRightCorner(MovableBoardItem item) {
        item.setX_speed(0);
        item.setY_speed(1);
        item.setY_next(1);
        if (item instanceof Pacman)
            item.setImage(((Pacman) item).getImage_down());
    }

    private void handleLowerLeftCorner(MovableBoardItem item) {
        item.setX_speed(0);
        item.setY_speed(-1);
        item.setY_next(-1);
        if (item instanceof Pacman)
            item.setImage(((Pacman) item).getImage_up());
    }

    private void handleLowerRightCorner(MovableBoardItem item) {
        item.setX_speed(-1);
        item.setY_speed(0);
        item.setX_next(-1);
        if (item instanceof Pacman)
            item.setImage(((Pacman) item).getImage_left());
    }
}
