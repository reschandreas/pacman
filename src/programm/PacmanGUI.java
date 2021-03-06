package programm;

import net.gobbz.grundobjekte.*;
import net.gobbz.spielobjekte.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

/**
 * Created by Andreas on 03.04.16.
 */
public class PacmanGUI extends JFrame {

    public static final int RESOLUTION = 16;

    public static final int WIDTH = 28 * RESOLUTION;
    public static final int HEIGHT = 36 * RESOLUTION;
    public static String playername = null;
    public static long playerpoints;
    public static int playerlevel = -1;

    private int wavelist[][] = new int[5][8];
    private double speedlist[][] = new double[4][7];

    private ArrayList<Wall> lifelist = new ArrayList<>();

    private ArrayList<Wall> walls = new ArrayList<>();
    public static ArrayList<Intersection> intersections = null;
    private ArrayList<Dot> dots = new ArrayList<>();
    private ArrayList<Tile> tiles = new ArrayList<>();
    public static ArrayList<Ghost> ghosts = null;
    public static Pacman pacman = null;

    private Thread moveThread = null;
    private Thread ghostThread = null;

    private Container container;
    private boolean caught = false;
    private boolean paused = false;

    private JLabel l_scoretext;
    private JLabel l_score;

    private JLabel l_leveltext;
    private JLabel l_level;
    private int level = 0;

    private Thread wavethread = null;

    private long starttime;
    private long fright_time;
    private long pausedtime;
    private long temptime;
    private long delay = 0;

    private JFrame jFrame = null;

    public PacmanGUI() {
        super("Pacman");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0, 0, WIDTH, HEIGHT);
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

        readDatas();

        drawMaze();

        l_scoretext = new JLabel("Score: ");
        l_scoretext.setBounds(0, 30, 50, 20);
        l_scoretext.setForeground(Color.white);
        container.add(l_scoretext);

        l_score = new JLabel("0");
        l_score.setBounds(50, 30, 40, 20);
        l_score.setForeground(Color.white);
        container.add(l_score);

        l_leveltext = new JLabel("Level: ");
        l_leveltext.setBounds(0, 15, 50, 20);
        l_leveltext.setForeground(Color.white);
        container.add(l_leveltext);

        l_level = new JLabel(String.valueOf(level));
        l_level.setBounds(50, 15, 40, 20);
        l_level.setForeground(Color.white);
        container.add(l_level);

        for (int i = 0; i < 3; i++) {
            Wall wall = new Wall("pacman_right.png");
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
                                           moveThread.resume();
                                           ghostThread.resume();
                                           wavethread.resume();
                                           pausedtime += System.currentTimeMillis() - temptime;
                                       } else {
                                           paused = true;
                                           temptime = System.currentTimeMillis();
                                           moveThread.suspend();
                                           ghostThread.suspend();
                                           wavethread.suspend();
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
                                       for (Tile tile : tiles) {
                                           tile.setVisible(!tile.isVisible());
                                       }
                                       break;
                                   }
                                   case KeyEvent.VK_R: {
                                       for (Ghost ghost : ghosts) {
                                           ghost.modes(Ghost.SCATTERMODE);
                                       }
                                       break;
                                   }
                                   case KeyEvent.VK_F: {
                                       for (Ghost ghost : ghosts) {
                                           ghost.modes(Ghost.FRIGHTENEDMODE);
                                       }
                                       break;
                                   }
                                   case KeyEvent.VK_C: {
                                       for (Ghost ghost : ghosts) {
                                           ghost.modes(Ghost.CHASEMODE);
                                       }
                                       break;
                                   }
                               }
                           }

                           @Override
                           public void keyReleased(final KeyEvent e) {
                               switch (e.getKeyCode()) {
                                   //UP
                                   case KeyEvent.VK_W:
                                       pacman.setY_next(-1);
                                       break;
                                   //DOWN
                                   case KeyEvent.VK_S:
                                       pacman.setY_next(1);
                                       break;
                                   //LEFT
                                   case KeyEvent.VK_A:
                                       pacman.setX_next(-1);
                                       break;
                                   //RIGHT
                                   case KeyEvent.VK_D:
                                       pacman.setX_next(1);
                                       break;
                               }
                           }
                       }

        );
        moveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    while (!Thread.interrupted() && !dots.isEmpty() && !caught()) {
                        pacman.move();
                        int column;
                        boolean eaten = eatenDots();
                        if (!eaten) {
                            column = 0;
                        } else column = 1;
                        if (!eaten && ghosts.get(0).getCurrentMode() == Ghost.FRIGHTENEDMODE)
                            column = 2;
                        if (eaten && ghosts.get(0).getCurrentMode() == Ghost.FRIGHTENEDMODE)
                            column = 3;
                        double tempspeed;
                        switch (level) {
                            case 1:
                                tempspeed = (pacman.getSpeed() * speedlist[0][column]);
                                break;
                            case 2:
                            case 3:
                            case 4:
                                tempspeed = (pacman.getSpeed() * speedlist[1][column]);
                                break;
                            default:
                                if (level <= 20)
                                    tempspeed = (pacman.getSpeed() * speedlist[2][column]);
                                else
                                    tempspeed = (pacman.getSpeed() * speedlist[3][column]);
                                break;
                        }
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
            }
        });
        moveThread.start();

        ghostThread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    while (!Thread.interrupted() && !dots.isEmpty() && !caught()) {
                        int row = 4;
                        double tempspeed, temp;
                        long speedms;
                        int speedns;
                        for (int i = 0; i < ghosts.size(); i++) {
                            if (ghosts.get(i) instanceof Blinky) {
                                ghosts.get(i).move();
                            }
                            if (ghosts.get(i) instanceof Pinky) {
                                ghosts.get(i).move();
                            }
                            if (ghosts.get(i) instanceof Inky) {
                                if (dots.size() <= 242 - 30) {
                                    if (ghosts.get(i).isInsidehouse()) {
                                        ghosts.get(i).setTarget(Ghost.targetinhouse);
                                    } else if (ghosts.get(i).getTarget()[0] == Ghost.targetouthouse[0] &&
                                            ghosts.get(i).getTarget()[1] == Ghost.targetouthouse[1]) {
                                        ghosts.get(i).calculateTarget();
                                    }
                                    ghosts.get(i).move();
                                }
                            }
                            if (ghosts.get(i) instanceof Clyde)
                                if (dots.size() <= 242 - 80) {
                                    if (ghosts.get(i).isInsidehouse()) {
                                        ghosts.get(i).setTarget(Ghost.targetouthouse);
                                    } else if (ghosts.get(i).getTarget()[0] == Ghost.targetouthouse[0] &&
                                            ghosts.get(i).getTarget()[1] == Ghost.targetouthouse[1]) {
                                        ghosts.get(i).calculateTarget();
                                    }
                                    ghosts.get(i).move();
                                }
                            for (Ghost ghost : ghosts) {
                                if (ghost.getCurrentMode() == Ghost.FRIGHTENEDMODE)
                                    row = 5;
                            }
                        }
                        switch (level) {
                            case 1:
                                tempspeed = (ghosts.get(0).getSpeed() * speedlist[0][row]);
                                break;
                            case 2:
                            case 3:
                            case 4:
                                tempspeed = (ghosts.get(0).getSpeed() * speedlist[1][row]);
                                break;
                            default:
                                if (level <= 20)
                                    tempspeed = (ghosts.get(0).getSpeed() * speedlist[2][row]);
                                else
                                    tempspeed = (ghosts.get(0).getSpeed() * speedlist[3][row]);
                                break;
                        }
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
        }

        );
        ghostThread.start();
        newGame();
        wavethread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted() && pacman.getLives() > 0) {
                    if (ghosts.get(0).getCurrentMode() == Ghost.FRIGHTENEDMODE) {
                        if (fright_time != 0) {
                            if (System.currentTimeMillis() - fright_time - pausedtime > 7000) {
                                for (Ghost ghost : ghosts) {
                                    ghost.modes(ghost.getPrev_mode());
                                }
                                fright_time = 0;
                            }
                        }

                    } else if (fright_time == 0) {
                        long gametime = System.currentTimeMillis() - starttime - pausedtime;
                        int row = 0;
                        switch (level) {
                            case 1:
                                row = 0;
                                break;
                            case 2:
                                row = 1;
                                break;
                            case 3:
                            case 4:
                            case 5:
                                row = 2;
                                break;
                            default:
                                if (level <= 20) {
                                    row = 3;
                                } else {
                                    row = 4;
                                }
                        }
                        int mode = 0;
                        int prev = ghosts.get(0).getCurrentMode();
                        long sum = (wavelist[row][0] == -1 ? Long.MAX_VALUE : wavelist[row][0]) - delay;
                        if (gametime > sum) {
                            mode = Ghost.CHASEMODE;
                        }
                        sum += (wavelist[row][1] == -1 ? Long.MAX_VALUE : wavelist[row][1]);
                        if (gametime >= sum) {
                            mode = Ghost.SCATTERMODE;
                        }
                        sum += (wavelist[row][2] == -1 ? Long.MAX_VALUE : wavelist[row][2]);
                        if (gametime >= sum) {
                            mode = Ghost.CHASEMODE;
                        }
                        sum += (wavelist[row][3] == -1 ? Long.MAX_VALUE : wavelist[row][3]);
                        if (gametime >= sum) {
                            mode = Ghost.SCATTERMODE;
                        }
                        sum += (wavelist[row][4] == -1 ? Long.MAX_VALUE : wavelist[row][4]);
                        if (gametime >= sum) {
                            mode = Ghost.CHASEMODE;
                        }
                        sum += (wavelist[row][5] == -1 ? Long.MAX_VALUE : wavelist[row][5]);
                        if (gametime >= sum) {
                            mode = Ghost.SCATTERMODE;
                        }
                        sum += (wavelist[row][6] == -1 ? Long.MAX_VALUE : wavelist[row][6]);
                        if (gametime >= sum) {
                            mode = Ghost.CHASEMODE;
                        }
                        for (Ghost ghost : ghosts) {
                            ghost.modes(mode);
                        }
                    }
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
            if (pacman.getX() == ghost.getX() && pacman.getY() == ghost.getY())
                if (ghost.isEatable()) {
                    ghost.modes(ghost.getPrev_mode());
                    ghost.resume();
                } else {
                    if (!caught) {
                        pacman.deductLife();
                        caught = true;
                        repaint();
                    }
                    return true;
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
            ghost.modes(Ghost.SCATTERMODE);
        }
        pacman.start();
    }

    private boolean eatenDots() {
        int temp = dots.size();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < dots.size(); i++) {
                    if (pacman.getRealX() - 2 == dots.get(i).getX() && pacman.getRealY() - 2 == dots.get(i).getY()
                            || pacman.getRealX() - 8 == dots.get(i).getX() && pacman.getRealY() - 8 == dots.get(i).getY()) {
                        if (dots.get(i) instanceof Energizer) {
                            delay += 7000;
                            fright_time = System.currentTimeMillis();
                            for (Ghost ghost : ghosts) {
                                if (ghost.getCurrentMode() != Ghost.FRIGHTENEDMODE) {
                                    ghost.modes(Ghost.FRIGHTENEDMODE);
                                    ghost.repaint();
                                }
                            }
                        }
                        dots.get(i).die();
                        pacman.setPoints(pacman.getPoints() + dots.get(i).getPoints());
                        l_score.setText(String.valueOf(pacman.getPoints()));
                        playerpoints = pacman.getPoints();
                        dots.remove(i);
                        container.repaint();
                        break;
                    }
                }
            }
        });
        return temp != dots.size();
    }

    private void readDatas() {
        int i = 0;
        Scanner reader = new Scanner(PacmanGUI.class.getResourceAsStream("waves.data")).useDelimiter("\\n");
        while (true) {
            String line = reader.hasNext() ? reader.next() : "";
            if (line == null || line.isEmpty())
                // Dateiende erkannt
                break;
            else {
                String[] strings = line.split(";");
                int j = 0;
                for (String s : strings) {
                    wavelist[i][j] = Integer.parseInt(s);
                    j++;
                }
                i++;
            }
        }

        reader = new Scanner(PacmanGUI.class.getResourceAsStream("speeds.data")).useDelimiter("\\n");
        i = 0;
        while (true) {
            String line = reader.hasNext() ? reader.next() : "";
            if (line == null || line.isEmpty())
                // Dateiende erkannt
                break;
            else {
                String[] strings = line.split(";");
                int j = 0;
                for (String s : strings) {
                    speedlist[i][j] = Double.parseDouble(s);
                    j++;
                }
                i++;
            }
        }
    }

    private void drawMaze() {
        if (walls.isEmpty()) {
            //Zeichne Mauern aus der Datei maze.data*/
            Scanner reader = new Scanner(PacmanGUI.class.getResourceAsStream("maze.data")).useDelimiter("\\n");
            while (true) {
                String line = reader.hasNext() ? reader.next() : "";
                if (line == null || line.isEmpty())
                    // Dateiende erkannt
                    break;
                else {
                    String[] strings = line.split(";");
                    Wall wall = new Wall(strings[0]);
                    wall.setBounds(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]), wall.getWidth(), wall.getHeight());
                    container.add(wall);
                    walls.add(wall);
                }
            }
            reader.close();
        }
        if (intersections.isEmpty()) {
            Scanner reader = new Scanner(PacmanGUI.class.getResourceAsStream("intersections.data")).useDelimiter("\\n");
            while (true) {
                String line = reader.hasNext() ? reader.next() : "";
                if (line == null || line.isEmpty())
                    break;
                else {
                    String[] strings = line.split(";");
                    Intersection intersection = new Intersection(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Boolean.parseBoolean(strings[2]),
                            Boolean.parseBoolean(strings[3]), Boolean.parseBoolean(strings[4]), Boolean.parseBoolean(strings[5]));
                    container.add(intersection);
                    intersections.add(intersection);
                }
            }
            reader.close();
        }

        if (dots.isEmpty()) {
            Scanner reader = new Scanner(PacmanGUI.class.getResourceAsStream("dots.data")).useDelimiter("\\n");
            while (true) {
                String line = reader.hasNext() ? reader.next() : "";
                if (line == null || line.isEmpty())
                    // Dateiende erkannt
                    break;
                else {
                    String[] strings = line.split(";");
                    Dot dot;
                    if (strings[0].equals("energizer.png")) {
                        dot = new Energizer(strings[0], Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
                    } else
                        dot = new Dot(strings[0], Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
                    container.add(dot);
                    dots.add(dot);
                }
            }
            reader.close();
        }

        if (tiles.isEmpty()) {
            for (int i = 0; i < WIDTH; i += RESOLUTION) {
                for (int j = 0; j < HEIGHT; j += RESOLUTION) {
                    Tile tile = new Tile(i, j);
                    tiles.add(tile);
                    container.add(tile);
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.fillRect(0, 0, WIDTH, HEIGHT);
        for (Component component : container.getComponents()) {
            component.repaint();
        }
        for (Ghost ghost : ghosts) {
            ghost.repaint();
        }
        pacman.repaint();
        for (Wall wall : lifelist) {
            wall.setVisible(false);
        }
        for (int i = 0; i < pacman.getLives(); i++) {
            lifelist.get(i).setVisible(true);
        }

    }

    public static void main(String[] args) {
        new MenuGUI();
    }
}