package io.resch.pacman.gui;

import io.resch.pacman.board.*;
import io.resch.pacman.movable.*;
import io.resch.pacman.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Created by Andreas on 03.04.16.
 */
public class PacmanGUI extends JFrame {

    public static String playername = null;
    public static long playerpoints;
    public static int playerlevel = -1;

    private final List<Wall> lifelist = new ArrayList<>();

    private List<Wall> walls = new ArrayList<>();
    public static List<Intersection> intersections = new ArrayList<>();
    private List<Dot> dots = Collections.synchronizedList(new ArrayList<>());
    private final List<Tile> tiles = new ArrayList<>();
    public static List<Ghost> ghosts = Collections.synchronizedList(new ArrayList<>());
    public static Pacman pacman = null;

    private Thread moveThread;
    private Thread ghostThread;

    private final Container container;
    private boolean caught = false;
    private boolean paused = false;

    private JLabel l_score;
    private JLabel l_level;
    private int level = 0;

    private Thread waveThread;

    private long startTime;
    private long frightTime;
    private long pausedTime;
    private long tempTime;
    private long delay;

    private Timer timer = new Timer();

    private final JFrame jFrame;

    public PacmanGUI() {
        super("Pacman");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0, 0, Utils.WIDTH, Utils.HEIGHT);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusable(true);
        jFrame = this;

        container = getContentPane();
        container.setLayout(null);
        container.setBackground(Color.black);

        drawMaze();

        addUIElements();
        addLiveIndicators();
        addMovableItems();

        addKeyListener(getKeyListener());
        createMoveThread();
        createGhostThread();
        newGame();
        createWaveThread();

        setVisible(true);
    }

    private void newGame() {
        level = 0;
        pacman.setPoints(0);
        pacman.setLives(3);
        nextLevel();
    }

    private KeyListener getKeyListener() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_P -> pause();
                    case KeyEvent.VK_ESCAPE -> exit();
                    case KeyEvent.VK_M -> System.out.println(getMousePosition().toString());
                    case KeyEvent.VK_I -> toggleIntersections();
                    case KeyEvent.VK_T -> toggleTiles();
                    case KeyEvent.VK_R -> forceModeChangeTo(Ghost.Mode.SCATTER);
                    case KeyEvent.VK_F -> forceModeChangeTo(Ghost.Mode.FRIGHTENED);
                    case KeyEvent.VK_C -> forceModeChangeTo(Ghost.Mode.CHASE);
                }
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> pacman.goUp();    // UP
                    case KeyEvent.VK_S -> pacman.goDown();  // DOWN
                    case KeyEvent.VK_A -> pacman.goLeft();  // LEFT
                    case KeyEvent.VK_D -> pacman.goRight(); //RIGHT
                }
            }
        };
    }

    private void createGhostThread() {
        ghostThread = new Thread(() -> {
            do {
                while (!Thread.interrupted() && !dots.isEmpty() && !caught()) {
                    double tempspeed, temp;
                    long speedms;
                    int speedns;
                    for (Ghost ghost : ghosts) {
                        if (ghost instanceof Blinky) {
                            ghost.move();
                        }
                        if (ghost instanceof Pinky) {
                            ghost.move();
                        }
                        if (ghost instanceof Inky) {
                            if (dots.size() <= 242 - 30) {
                                ghost.move();
                            }
                        }
                        if (ghost instanceof Clyde) {
                            if (dots.size() <= 242 - 80) {
                                ghost.move();
                            }
                        }
                    }

                    tempspeed = (ghosts.get(0).getSpeed() * Speeds.getCurrentSpeedLimits(level)
                            .getSpeedByType(ghosts.get(0).getCurrentMode().equals(Ghost.Mode.FRIGHTENED) ?
                                    Speed.Type.GHOST_FRIGHT :
                                    Speed.Type.GHOST_NORMAL)
                            .getValue());
                    speedms = (long) tempspeed;
                    temp = (tempspeed - speedms) * 1000;
                    speedns = (int) temp;

                    try {
                        Thread.sleep(speedms, speedns);
                    } catch (InterruptedException ignored) {
                    }
                }
                if (!dots.isEmpty()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (!Thread.interrupted() && pacman.getLives() > 0);
        }
        );
        ghostThread.start();
    }

    private void createMoveThread() {
        moveThread = new Thread(() -> {
            do {
                while (!Thread.interrupted() && !dots.isEmpty() && !caught()) {
                    pacman.move();
                    double tempspeed = (pacman.getSpeed() * Speeds.getCurrentSpeedLimits(level)
                            .getSpeedByType(Speed.getCurrentType(eatenDots(), ghosts.get(0).getCurrentMode().equals(Ghost.Mode.FRIGHTENED)))
                            .getValue());
                    long speedms = (long) tempspeed;
                    double temp = (tempspeed - speedms) * 1000;
                    int speedns = (int) temp;
                    try {
                        Thread.sleep(speedms, speedns);
                    } catch (InterruptedException ignored) {
                    }
                }
                if (dots.isEmpty())
                    nextLevel();
                else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continueLevel();
                }
                drawMaze();
            } while (!Thread.interrupted() && pacman.getLives() > 0);
            gameOver();
        });
        moveThread.start();
    }

    private void createWaveThread() {
        waveThread = new Thread(() -> {
            while (!Thread.interrupted() && pacman.getLives() > 0) {
                try {
                    Difficulties
                            .getCurrentDifficulty(level)
                            .getCurrentWave(System.currentTimeMillis() - startTime - pausedTime - delay)
                            .setModes(ghosts);
                } catch (IndexOutOfBoundsException ignored) {}
            }
        });
        waveThread.start();
    }

    private void gameOver() {
        MenuGUI.highscores.add(new Score(playername, playerlevel, playerpoints));
        new MenuGUI();
        jFrame.dispose();
    }

    private void continueLevel() {
        for (Ghost ghost : ghosts) {
            ghost.start();
        }
        pacman.start();
        caught = false;
    }

    private void forceModeChangeTo(Ghost.Mode mode) {
        for (Ghost ghost : ghosts) {
            ghost.changeModeTo(mode);
        }
    }

    private void pause() {
        if (paused) {
            paused = false;
            try {
                moveThread.wait();
                ghostThread.wait();
                waveThread.wait();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            pausedTime += System.currentTimeMillis() - tempTime;
        } else {
            paused = true;
            tempTime = System.currentTimeMillis();
            moveThread.notify();
            ghostThread.notify();
            waveThread.notify();
        }
    }

    private void exit() {
        ghostThread.interrupt();
        moveThread.interrupt();
        waveThread.interrupt();
        new MenuGUI();
        jFrame.dispose();
    }

    private void toggleIntersections() {
        intersections.forEach(i -> i.setVisible(!i.isVisible()));
    }

    private void toggleTiles() {
        if (tiles.isEmpty()) {
            for (int i = 0; i < Utils.WIDTH; i += Utils.RESOLUTION) {
                for (int j = 0; j < Utils.HEIGHT; j += Utils.RESOLUTION) {
                    Tile tile = new Tile(new Point(i, j));
                    tiles.add(tile);
                    container.add(tile);
                }
            }
        } else {
            for (Tile tile : tiles) {
                container.remove(tile);
            }
            tiles.clear();
        }
    }

    private void addUIElements() {
        JLabel l_scoretext = new JLabel("Score: ");
        l_scoretext.setBounds(0, 30, 50, 20);
        l_scoretext.setForeground(Color.white);
        container.add(l_scoretext);

        l_score = new JLabel("0");
        l_score.setBounds(50, 30, 40, 20);
        l_score.setForeground(Color.white);
        container.add(l_score);

        JLabel l_leveltext = new JLabel("Level: ");
        l_leveltext.setBounds(0, 15, 50, 20);
        l_leveltext.setForeground(Color.white);
        container.add(l_leveltext);

        l_level = new JLabel(String.valueOf(level));
        l_level.setBounds(50, 15, 40, 20);
        l_level.setForeground(Color.white);
        container.add(l_level);
    }

    private void addLiveIndicators() {
        for (int i = 0; i < 3; i++) {
            Wall wall = new Wall(BoardItem.Type.PACMAN);
            wall.setLocation(i * 32, 544);
            lifelist.add(wall);
            container.add(lifelist.get(i));
        }
    }

    private void addMovableItems() {
        ghosts.add(new Blinky());
        container.add(ghosts.get(0));

        ghosts.add(new Inky());
        container.add(ghosts.get(1));

        ghosts.add(new Pinky());
        container.add(ghosts.get(2));

        ghosts.add(new Clyde());
        container.add(ghosts.get(3));

        pacman = new Pacman();
        container.add(pacman);
    }

    private boolean caught() {
        for (Ghost ghost : ghosts) {
            if (pacman.getX() == ghost.getX() && pacman.getY() == ghost.getY()) {
                System.out.println(ghost.getCurrentMode().toString());
                if (ghost.isEatable()) {
                    ghost.getsEaten();
                } else {
                    if (!caught) {
                        pacman.deductLife();
                        caught = true;
                        repaint();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void endFrightenedMode() {
        for (Ghost ghost : ghosts) {
            ghost.changeModeTo(ghost.getPreviousMode());
            ghost.repaint();
            container.repaint();
        }
    }

    private void nextLevel() {
        startTime = System.currentTimeMillis();
        level++;
        pausedTime = 0;
        l_level.setText(String.valueOf(level));
        playerlevel = level;
        for (Ghost ghost : ghosts) {
            ghost.start();
            ghost.changeModeTo(Ghost.Mode.SCATTER);
        }
        pacman.start();
    }

    private boolean eatenDots() {
        int temp = dots.size();
        EventQueue.invokeLater(() -> {
            List<Dot> eaten = new CopyOnWriteArrayList<>();
            AtomicInteger points = new AtomicInteger();
            dots.stream()
                    .parallel()
                    .filter(dot -> pacman.isEating(dot))
                    .forEach(dot -> {
                        if (dot instanceof Energizer) {
                            for (Ghost ghost : ghosts) {
                                ghost.changeModeTo(Ghost.Mode.FRIGHTENED);
                            }
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    endFrightenedMode();
                                }
                            }, 7000);
                            delay += 7000;
                            frightTime = System.currentTimeMillis();
                        }

                        eaten.add(dot);
                    });

            eaten.stream().parallel().forEach(dot -> {
                dot.eaten();
                container.remove(dot);
                points.addAndGet(dot.getPoints());
            });

            pacman.setPoints(pacman.getPoints() + points.get());
            l_score.setText(String.valueOf(pacman.getPoints()));
            playerpoints = pacman.getPoints();
            dots.removeAll(eaten);

            container.repaint();
        });
        return temp != dots.size();
    }

    private void drawMaze() {
        if (walls.isEmpty()) {
            walls = Wall.readWallsFile();
            walls.forEach(container::add);
        }
        if (intersections.isEmpty()) {
            intersections = Intersection.readIntersectionsFile();
            intersections.forEach(container::add);
        }

        if (dots.isEmpty()) {
            dots = Dot.readDotsFile();
            dots.forEach(container::add);
        }
    }

    private void resetGameAfterBeingEaten() {
        IntStream.range(0, lifelist.size()).forEach(i -> lifelist.get(i).setVisible(i < pacman.getLives()));
    }

    @Override
    public void paint(Graphics g) {
        resetGameAfterBeingEaten();
    }

    public static void main(String[] args) {
        new MenuGUI();
    }
}