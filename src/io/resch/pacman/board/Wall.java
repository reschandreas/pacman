package io.resch.pacman.board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Andreas on 03.04.16.
 */
public class Wall extends BoardItem {

    public Wall(Type type) {
        super(type);
    }

    public static Wall create(String[] strings) {
        Wall wall;
        switch (strings[0]) {
            case "wall_right.png" -> wall = new Wall(Type.WALL_RIGHT);
            case "wall_up.png" -> wall = new Wall(Type.WALL_UP);
            default -> wall = new Wall(Type.WALL_NORMAL);
        }
        wall.setBounds(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]), wall.getWidth(), wall.getHeight());
        return wall;
    }
}
