package io.resch.pacman.gui;

import io.resch.pacman.board.*;
import io.resch.pacman.movable.*;
import io.resch.pacman.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Andreas on 03.04.16.
 */
public class PacmanGUI extends JFrame {

    public static String playername = null;
    public static long playerpoints;
    public static int playerlevel = -1;

    private final List<Wall> lifelist = new ArrayList<>();

    private final List<Wall> walls = new ArrayList<>();
    public static List<Intersection> intersections = null;
    private final List<Dot> dots = Collections.synchronizedList(new ArrayList<>());
    private final List<Tile> tiles = new ArrayList<>();
    public static ArrayList<Ghost> ghosts = null;
    public static Pacman pacman = null;

    private Thread moveThread = null;
    private Thread ghostThread = null;

    private final Container container;
    private boolean caught = false;
    private boolean paused = false;

    private final JLabel l_score;

    private final JLabel l_level;
    private int level = 0;

    private Thread wavethread = null;

    private long starttime;
    private long fright_time;
    private long pausedtime;
    private long temptime;
    private long delay = 0;

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

        intersections = new ArrayList<>();
        ghosts = new ArrayList<>();

        drawMaze();

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

        for (int i = 0; i < 3; i++) {
            Wall wall = new Wall(BoardItem.Type.PACMAN);
            wall.setLocation(i * 32, 544);
            lifelist.add(wall);
            container.add(lifelist.get(i));
        }

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

        addKeyListener(new KeyListener() {
                           @Override
                           public void keyTyped(KeyEvent e) {
                           }

                           @Override
                           public void keyPressed(final KeyEvent e) {
                               switch (e.getKeyCode()) {
                                   case KeyEvent.VK_P:
                                       if (paused) {
                                           paused = false;
                                           try {
                                               moveThread.wait();
                                               ghostThread.wait();
                                               wavethread.wait();
                                           } catch (InterruptedException interruptedException) {
                                               interruptedException.printStackTrace();
                                           }
                                           pausedtime += System.currentTimeMillis() - temptime;
                                       } else {
                                           paused = true;
                                           temptime = System.currentTimeMillis();
                                           moveThread.notify();
                                           ghostThread.notify();
                                           wavethread.notify();
                                       }
                                       break;
                                   case KeyEvent.VK_ESCAPE:
                                       ghostThread.interrupt();
                                       moveThread.interrupt();
                                       wavethread.interrupt();
                                       new MenuGUI();
                                       jFrame.dispose();
                                       break;
                                   case KeyEvent.VK_M: {
                                       System.out.println(getMousePosition().getX() + " " + getMousePosition().getY());
                                       break;
                                   }
                                   case KeyEvent.VK_I:
                                       for (Intersection intersection : intersections) {
                                           intersection.setVisible(!intersection.isVisible());
                                       }
                                       break;
                                   case KeyEvent.VK_T: {
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
                                       break;
                                   }
                                   case KeyEvent.VK_R: {
                                       for (Ghost ghost : ghosts) {
                                           ghost.changeModeTo(Ghost.Mode.SCATTER);
                                       }
                                       break;
                                   }
                                   case KeyEvent.VK_F: {
                                       for (Ghost ghost : ghosts) {
                                           ghost.changeModeTo(Ghost.Mode.FRIGHTENED);
                                       }
                                       break;
                                   }
                                   case KeyEvent.VK_C: {
                                       for (Ghost ghost : ghosts) {
                                           ghost.changeModeTo(Ghost.Mode.CHASE);
                                       }
                                       break;
                                   }
                               }
                           }

                           @Override
                           public void keyReleased(final KeyEvent e) {
                               switch (e.getKeyCode()) {
                                   //UP
                                   case KeyEvent.VK_W -> pacman.setY_next(-1);
                                   //DOWN
                                   case KeyEvent.VK_S -> pacman.setY_next(1);
                                   //LEFT
                                   case KeyEvent.VK_A -> pacman.setX_next(-1);
                                   //RIGHT
                                   case KeyEvent.VK_D -> pacman.setX_next(1);
                               }
                           }
                       }

        );
        moveThread = new Thread(() -> {
            do {
                while (!Thread.interrupted() && !dots.isEmpty() && !caught()) {
                    pacman.move();
                    boolean ghostsFrightened = ghosts.get(0).getCurrentMode().equals(Ghost.Mode.FRIGHTENED);
                    Speed.Type column;
                    boolean eaten = eatenDots();
                    if (!eaten) {
                        column = Speed.Type.NORMAL;
                    } else column = Speed.Type.DOT_NORMAL;
                    if (!eaten && ghostsFrightened)
                        column = Speed.Type.FRIGHT;
                    if (eaten && ghostsFrightened)
                        column = Speed.Type.DOT_FRIGHT;

                    double tempspeed = (pacman.getSpeed() * Speeds.getCurrentSpeedLimits(level)
                            .getSpeedByType(column)
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
                                if (ghost.isInsideHouse()) {
                                    ghost.setTarget(Ghost.targetInHouse);
                                } else if (ghost.getTarget().equals(Ghost.targetOutHouse)) {
                                    ghost.calculateTarget();
                                }
                                ghost.move();
                            }
                        }
                        if (ghost instanceof Clyde) {
                            if (dots.size() <= 242 - 80) {
                                if (ghost.isInsideHouse()) {
                                    ghost.setTarget(Ghost.targetOutHouse);
                                } else if (ghost.getTarget().equals(Ghost.targetOutHouse)) {
                                    ghost.calculateTarget();
                                }
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
        newGame();
        wavethread = new Thread(() -> {
            while (!Thread.interrupted() && pacman.getLives() > 0) {
                if (ghosts.get(0).getCurrentMode() == Ghost.Mode.FRIGHTENED) {
                    if (fright_time != 0) {
                        if (System.currentTimeMillis() - fright_time - pausedtime > 7000) {
                            for (Ghost ghost : ghosts) {
                                ghost.changeModeTo(ghost.getPreviousMode());
                                ghost.repaint();
                            }
                            fright_time = 0;
                        }
                    }

                } else if (fright_time == 0) {
                    Difficulties
                            .getCurrentDifficulty(level)
                            .getCurrentWave(System.currentTimeMillis() - starttime - pausedtime - delay)
                            .setModes(ghosts);
                }
            }
        }

        );
        wavethread.start();

        setVisible(true);
    }

    private void newGame() {
        level = 0;
        pacman.setPoints(0);
        pacman.setLives(3);
        nextLevel();
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

    private void nextLevel() {
        starttime = System.currentTimeMillis();
        level++;
        pausedtime = 0;
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
            for (int i = 0; i < dots.size(); i++) {
                if (pacman.getRealX() - 2 == dots.get(i).getX() && pacman.getRealY() - 2 == dots.get(i).getY()
                        || pacman.getRealX() - 8 == dots.get(i).getX() && pacman.getRealY() - 8 == dots.get(i).getY()) {
                    if (dots.get(i) instanceof Energizer) {
                        delay += 7000;
                        fright_time = System.currentTimeMillis();
                        for (Ghost ghost : ghosts) {
                            ghost.changeModeTo(Ghost.Mode.FRIGHTENED);
                        }
                    }
                    dots.get(i).eaten();
                    container.remove(dots.get(i));
                    pacman.setPoints(pacman.getPoints() + dots.get(i).getPoints());
                    l_score.setText(String.valueOf(pacman.getPoints()));
                    playerpoints = pacman.getPoints();
                    dots.remove(i);
                    container.repaint();
                    break;
                }
            }
        });
        return temp != dots.size();
    }

    private void drawMaze() {
        if (walls.isEmpty()) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = classLoader.getResourceAsStream("data/maze.data");
            if (input == null) {
                return;
            }
            Scanner reader = new Scanner(input).useDelimiter("\\n");
            String line;
            while (!(line = reader.hasNext() ? reader.next() : "").isEmpty()) {
                Wall wall = Wall.create(line.split(";"));
                container.add(wall);
                walls.add(wall);
            }
            reader.close();
        }
        if (intersections.isEmpty()) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = classLoader.getResourceAsStream("data/intersections.data");
            if (input == null)
                return;
            Scanner reader = new Scanner(input).useDelimiter("\\n");
            String line;
            while (!(line = reader.hasNext() ? reader.next() : "").isEmpty()) {
                Intersection intersection = Intersection.create(line.split(";"));
                container.add(intersection);
                intersections.add(intersection);
            }
            reader.close();
        }

        if (dots.isEmpty()) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = classLoader.getResourceAsStream("data/dots.data");
            if (input == null) {
                return;
            }
            Scanner reader = new Scanner(input).useDelimiter("\\n");
            String line;
            while (!(line = reader.hasNext() ? reader.next() : "").isEmpty()) {
                Dot dot = Dot.create(line.split(";"));
                container.add(dot);
                dots.add(dot);
            }
            reader.close();
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