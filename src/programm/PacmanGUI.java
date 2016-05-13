package programm;

import net.gobbz.grundobjekte.*;
import net.gobbz.spielobjekte.*;
import sun.plugin.javascript.navig.Array;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Arc2D;
import java.sql.Time;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * Created by Andreas on 03.04.16.
 */
public class PacmanGUI extends JFrame {

    public static final int RESOLUTION = 16;

    public final int WIDTH = 28 * RESOLUTION;
    public final int HEIGHT = 36 * RESOLUTION;

    List<List<Integer>> wavelist = new ArrayList<>();
    double speedlist[][] = new double[4][7];

    private ArrayList<Wall> walls = new ArrayList<>();
    public static ArrayList<Intersection> intersections = new ArrayList<>();
    private ArrayList<Dot> dots = new ArrayList<>();
    private ArrayList<Tile> tiles = new ArrayList<>();
    public static ArrayList<Ghost> ghosts = new ArrayList<>();
    static final public Pacman pacman = new Pacman("pacman_up.png", "pacman_down.png", "pacman_left.png", "pacman_right.png");

    private Thread moveThread;
    private Thread ghostThread;

    private Container container;

    private JLabel l_scoretext;
    private JLabel l_score;

    private JLabel l_leveltext;
    private JLabel l_level;
    private int level = 0;

    private Timer wavetimer = null;
    private Timer modestimer = null;

    public PacmanGUI() {
        super("Pacman");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0, 0, WIDTH, HEIGHT);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);

        container.setBackground(Color.black);
        //Loadingscreen
        JWindow window = new JWindow();
        window.setLocationRelativeTo(null);
        window.setBounds(getX(), getY(), WIDTH, HEIGHT);
        window.setVisible(true);

        readDatas();

        drawMaze();

        wavetimer = new Timer();

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

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.dispose();

        ghosts.add(new Blinky("ghost_red.png", "ghost_frightened.png"));
        container.add(ghosts.get(0));

        ghosts.add(new Inky("ghost_blue.png", "ghost_frightened.png"));
        container.add(ghosts.get(1));

        ghosts.add(new Pinky("ghost_pink.png", "ghost_frightened.png"));
        container.add(ghosts.get(2));

        ghosts.add(new Clyde("ghost_orange.png", "ghost_frightened.png"));
        container.add(ghosts.get(3));

        container.add(pacman);

        addKeyListener(new KeyListener() {
                           @Override
                           public void keyTyped(KeyEvent e) {
                           }

                           @Override
                           public void keyPressed(final KeyEvent e) {
                               switch (e.getKeyCode()) {
                                   case KeyEvent.VK_M: {
                                       System.out.println(getMousePosition().getX() + " " + getMousePosition().getY());
                                       break;
                                   }
                                   case KeyEvent.VK_P: {
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
                    while (!dots.isEmpty() && !caught()) {
                        pacman.move();
                        int row;
                        boolean eaten = eatenDots();
                        if (!eaten) {
                            row = 0;
                        } else row = 1;
                        if (!eaten && ghosts.get(0).getCurrent_mode() == Ghost.FRIGHTENEDMODE)
                            row = 2;
                        if (eaten && ghosts.get(0).getCurrent_mode() == Ghost.FRIGHTENEDMODE)
                            row = 3;
                        double tempspeed;
                        switch (level) {
                            case 1:
                                tempspeed = (pacman.getSpeed() * speedlist[0][row]);
                                break;
                            case 2 - 4:
                                tempspeed = (pacman.getSpeed() * speedlist[1][row]);
                                break;
                            case 5 - 20:
                                tempspeed = (pacman.getSpeed() * speedlist[2][row]);
                                break;
                            default:
                                tempspeed = (pacman.getSpeed() * speedlist[3][row]);
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
                } while (pacman.getLives() != 0);
            }
        });
        moveThread.start();

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println((int) getMousePosition().getX() + ";" + (int) getMousePosition().getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        ghostThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!dots.isEmpty() && !caught()) {
                    int row = 4;
                    double tempspeed = 0;
                    long speedms = 0;
                    double temp = 0;
                    int speedns = 0;
                    for (Ghost ghost : ghosts) {
                        ghost.move();
                        if (ghost.getCurrent_mode() == Ghost.FRIGHTENEDMODE)
                            row = 5;
                    }
                    switch (level) {
                        case 1:
                            tempspeed = (ghosts.get(0).getSpeed() * speedlist[0][row]);
                            break;
                        case 2 - 4:
                            tempspeed = (ghosts.get(0).getSpeed() * speedlist[1][row]);
                            break;
                        case 5 - 20:
                            tempspeed = (ghosts.get(0).getSpeed() * speedlist[2][row]);
                            break;
                        default:
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
            }
        }

        );
        ghostThread.start();
        newGame();
        setVisible(true);
    }

    private void continueLevel() {
        for (Ghost ghost : ghosts) {
            ghost.reStart();
        }
        pacman.reStart();
    }

    private boolean caught() {
        for (Ghost ghost : ghosts) {
            if (pacman.getX() == ghost.getX() && pacman.getY() == ghost.getY())
                if (ghost.isEatable())
                    ghost.reStart();
                else
                    return true;
        }
        return false;
    }

    private void nextLevel() {
        level++;
        for (Ghost ghost : ghosts) {
            ghost.setCurrent_mode(Ghost.SCATTERMODE);
        }
        switch (level) {
            case 1: {
                for (final Ghost ghost : ghosts) {
                    wavetimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ghost.setCurrent_mode(Ghost.CHASEMODE);
                            wavetimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    ghost.setCurrent_mode(Ghost.SCATTERMODE);
                                    wavetimer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            ghost.setCurrent_mode(Ghost.CHASEMODE);
                                            wavetimer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    ghost.setCurrent_mode(Ghost.SCATTERMODE);
                                                    wavetimer.schedule(new TimerTask() {
                                                        @Override
                                                        public void run() {
                                                            ghost.setCurrent_mode(Ghost.CHASEMODE);
                                                            wavetimer.schedule(new TimerTask() {
                                                                @Override
                                                                public void run() {
                                                                    ghost.setCurrent_mode(Ghost.SCATTERMODE);
                                                                    wavetimer.schedule(new TimerTask() {
                                                                        @Override
                                                                        public void run() {
                                                                            ghost.setCurrent_mode(Ghost.CHASEMODE);
                                                                        }
                                                                    }, (long) wavelist.get(0).get(6) == -1 ? Integer.MAX_VALUE : wavelist.get(0).get(6));
                                                                }
                                                            }, (long) wavelist.get(0).get(5) == -1 ? Integer.MAX_VALUE : wavelist.get(0).get(5));
                                                        }
                                                    }, (long) wavelist.get(0).get(4) == -1 ? Integer.MAX_VALUE : wavelist.get(0).get(4));
                                                }
                                            }, (long) wavelist.get(0).get(3) == -1 ? Integer.MAX_VALUE : wavelist.get(0).get(3));
                                        }
                                    }, (long) wavelist.get(0).get(2) == -1 ? Integer.MAX_VALUE : wavelist.get(0).get(2));
                                }
                            }, (long) wavelist.get(0).get(1) == -1 ? Integer.MAX_VALUE : wavelist.get(0).get(1));
                        }
                    }, (long) wavelist.get(0).get(0) == -1 ? Integer.MAX_VALUE : wavelist.get(0).get(0));
                }
            }
        }
        pacman.reStart();
        for (Ghost ghost : ghosts) {
            ghost.reStart();
        }
    }

    private void newGame() {
        level = 0;
        pacman.setPoints(0);
        pacman.setLives(3);
        nextLevel();
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
                            for (Ghost ghost : ghosts) {
                                if (ghost.getCurrent_mode() != Ghost.FRIGHTENEDMODE)
                                    ghost.modes(Ghost.FRIGHTENEDMODE);
                            }
                        }
                        dots.get(i).die();
                        pacman.setPoints(pacman.getPoints() + dots.get(i).getPoints());
                        l_score.setText(String.valueOf(pacman.getPoints()));
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
                wavelist.add(new ArrayList<Integer>());
                wavelist.get(i).addAll(Arrays.asList(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]),
                        Integer.parseInt(strings[2]), Integer.parseInt(strings[3]), Integer.parseInt(strings[4]),
                        Integer.parseInt(strings[5]), Integer.parseInt(strings[6]), Integer.parseInt(strings[7])));
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
                    System.out.println("i: " + i + " j: " + j);
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
        g.setColor(Color.green);
        for (Component component : container.getComponents()) {
            component.repaint();
        }
    }

    public static void main(String[] args) {
        new PacmanGUI();
    }
}