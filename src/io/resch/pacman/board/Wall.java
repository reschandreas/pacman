package io.resch.pacman.board;

import io.resch.pacman.utils.Utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
            case "R" -> wall = new Wall(Type.WALL_RIGHT);
            case "U" -> wall = new Wall(Type.WALL_UP);
            default -> wall = new Wall(Type.WALL_NORMAL);
        }
        wall.setBounds(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]), wall.getWidth(), wall.getHeight());
        return wall;
    }

    public static List<Wall> readWallsFile() {
        return Utils.readFile("data/maze.data")
                .stream()
                .map(line -> Wall.create(line.split(";")))
                .collect(Collectors.toList());
    }
}
