import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by Andreas on 03.04.16.
 */
public class PacmanGUI extends JFrame {

    public static final int RESOLUTION = 16;

    public final int WIDTH = 28 * RESOLUTION;
    public final int HEIGHT = 36 * RESOLUTION;

    private Random random = new Random();

    List<List<Integer>> wavelist = new ArrayList<>();
    List<List<Double>> speedlist = new ArrayList<>();

    private ArrayList<Wall> walls = new ArrayList<>();
    public static ArrayList<Intersection> intersections = new ArrayList<>();
    private ArrayList<Dot> dots = new ArrayList<>();
    private ArrayList<Tile> tiles = new ArrayList<>();
    public static ArrayList<Ghost> ghosts = new ArrayList<>();
    static final public Pacman pacman = new Pacman("images/pacman_up.png", "images/pacman_down.png", "images/pacman_left.png", "images/pacman_right.png");

    private Thread moveThread;
    private Thread ghostThread;

    private Blinky blinky;
    private Inky inky;
    private Pinky pinky;
    private Clyde clyde;

    private Container container;

    private JLabel l_scoretext;
    private JLabel l_score;

    private JLabel l_leveltext;
    private JLabel l_level;
    private int level = 1;

    private Timer wavetimer = new Timer();
    private Timer frigthenedtimer = new Timer();

    private final int FRAMERATE = 3;

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
        container.add(l_leveltext);

        l_level = new JLabel(String.valueOf(level));
        l_level.setBounds(50, 15, 40, 20);
        container.add(l_level);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.dispose();

        ghosts.add(new Blinky("images/ghost_red.png", "images/ghost_frightened.png"));
        container.add(ghosts.get(0));

        ghosts.add(new Inky("images/ghost_blue.png", "images/ghost_frightened.png"));
        container.add(ghosts.get(1));

        ghosts.add(new Pinky("images/ghost_pink.png", "images/ghost_frightened.png"));
        container.add(ghosts.get(2));

        ghosts.add(new Clyde("images/ghost_orange.png", "images/ghost_frightened.png"));
        container.add(ghosts.get(3));

        container.add(pacman);

        addKeyListener(new KeyListener() {
                           @Override
                           public void keyTyped(KeyEvent e) {
                           }

                           @Override
                           public void keyPressed(final KeyEvent e) {
                               switch (e.getKeyCode()) {
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
                        if (!eaten && ghosts.get(0).current_mode == Ghost.FRIGHTENEDMODE)
                            row = 2;
                        if (eaten && ghosts.get(0).current_mode == Ghost.FRIGHTENEDMODE)
                            row = 3;
                        double tempspeed;
                        switch (level) {
                            case 1:
                                tempspeed = (pacman.getSpeed() * speedlist.get(0).get(row));
                                break;
                            case 2 - 4:
                                tempspeed = (pacman.getSpeed() * speedlist.get(1).get(row));
                                break;
                            case 5 - 20:
                                tempspeed = (pacman.getSpeed() * speedlist.get(2).get(row));
                                break;
                            default:
                                tempspeed = (pacman.getSpeed() * speedlist.get(3).get(row));
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
                    newGame();
                    drawMaze();
                } while (true);
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

        wavetimer.start();
        ghostThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!dots.isEmpty()) {
                    int row = 4;
                    double tempspeed = 0;
                    long speedms = 0;
                    double temp = 0;
                    int speedns = 0;
                    for (Ghost ghost : ghosts) {
                        ghost.move();
                    }

                    if (ghosts.get(0).current_mode == Ghost.FRIGHTENEDMODE)
                        row = 5;
                    else row = 4;
                    switch (level) {
                        case 1:
                            tempspeed = (ghosts.get(0).getSpeed() * speedlist.get(0).get(row));
                            break;
                        case 2 - 4:
                            tempspeed = (ghosts.get(0).getSpeed() * speedlist.get(1).get(row));
                            break;
                        case 5 - 20:
                            tempspeed = (ghosts.get(0).getSpeed() * speedlist.get(2).get(row));
                            break;
                        default:
                            tempspeed = (ghosts.get(0).getSpeed() * speedlist.get(3).get(row));
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

        setVisible(true);
    }



    private boolean caught() {
        for (Ghost ghost : ghosts) {
        }

        return false;
    }

    private void newGame() {
        wavetimer.start();
        pacman.reStart();
        for (Ghost ghost : ghosts) {
            ghost.reStart();
        }


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
                            wavetimer.pause();
                            for (Ghost ghost : ghosts) {
                                if (ghost.current_mode != Ghost.FRIGHTENEDMODE)
                                    ghost.modes(Ghost.FRIGHTENEDMODE);
                            }
                            frigthenedtimer.start();
                        }
                        dots.get(i).die();
                        pacman.setPoints(pacman.getPoints() + dots.get(i).points);
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
        try {
            BufferedReader reader = new BufferedReader(new FileReader("waves.txt"));
            while (true) {
                String line = reader.readLine();
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
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("waves.txt not found");
        } catch (IOException e) {
            System.out.println("Error happened");
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("speeds.txt"));
            while (true) {
                String line = reader.readLine();
                if (line == null || line.isEmpty())
                    // Dateiende erkannt
                    break;
                else {
                    String[] strings = line.split(";");
                    speedlist.add(new ArrayList<Double>());
                    speedlist.get(i).addAll(Arrays.asList(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]),
                            Double.parseDouble(strings[2]), Double.parseDouble(strings[3]), Double.parseDouble(strings[4]),
                            Double.parseDouble(strings[5]), Double.parseDouble(strings[6])));
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("speeds.txt not found");
        } catch (IOException e) {
            System.out.println("Error happened");
        }
    }

    private void drawMaze() {
        if (walls.isEmpty()) {
            //Zeichne Mauern aus der Datei maze.txt
            try {
                BufferedReader reader = new BufferedReader(new FileReader("maze.txt"));
                while (true) {
                    String line = reader.readLine();
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
            } catch (FileNotFoundException e) {
                System.out.println("maze.txt not found");
            } catch (IOException e) {
                System.out.println("Error happened");
            }
        }
        if (intersections.isEmpty()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("intersections.txt"));
                while (true) {
                    String line = reader.readLine();
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
            } catch (FileNotFoundException e) {
                System.out.println("intersections.txt not found");
            } catch (IOException e) {
                System.out.println("Error happened");
            }
        }

        if (dots.isEmpty()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("dots.txt"));
                while (true) {
                    String line = reader.readLine();
                    if (line == null || line.isEmpty())
                        // Dateiende erkannt
                        break;
                    else {
                        String[] strings = line.split(";");
                        Dot dot;
                        if (strings[0].equals("images/energizer.png")) {
                            dot = new Energizer(strings[0], Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
                        } else
                            dot = new Dot(strings[0], Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
                        container.add(dot);
                        dots.add(dot);
                    }
                }
                reader.close();
                System.out.println(dots.size());
            } catch (FileNotFoundException e) {
                System.out.println("dots.txt not found");
            } catch (IOException e) {
                System.out.println("Error happened");
            }
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