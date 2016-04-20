import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Andreas on 03.04.16.
 */
public class PacmanGUI extends JFrame {

    public static final int RESOLUTION = 16;

    public final int WIDTH = 28 * RESOLUTION;
    public final int HEIGHT = 36 * RESOLUTION;

    private Random random = new Random();

    private ArrayList<Wall> walls = new ArrayList<>();
    public static ArrayList<Intersection> intersections = new ArrayList<>();
    private ArrayList<Dot> dots = new ArrayList<>();
    private ArrayList<Tile> tiles = new ArrayList<>();

    static final private Pacman pacman = new Pacman("images/pacman_up.png", "images/pacman_down.png", "images/pacman_left.png", "images/pacman_right.png");

    private Blinky blinky = new Blinky("images/ghost_red.png");
    private Inky inky = new Inky("images/ghost_blue.png");
    private Pinky pinky = new Pinky("images/ghost_pink.png");
    private Clyde clyde = new Clyde("images/ghost_orange.png");

    private Thread moveThread;
    private Thread blinkyThread;
    private Container container;

    private JLabel l_scoretext;
    private JLabel l_score;

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

        drawMaze();

        l_scoretext = new JLabel("Score: ");
        l_scoretext.setBounds(0, 30, 50, 20);
        l_scoretext.setForeground(Color.white);
        container.add(l_scoretext);

        l_score = new JLabel("0");
        l_score.setBounds(50, 30, 40, 20);
        l_score.setForeground(Color.white);
        container.add(l_score);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.dispose();

        container.add(blinky);
        container.add(inky);
        container.add(pinky);
        container.add(clyde);

        container.add(pacman);

        addKeyListener(new KeyListener() {
                           @Override
                           public void keyTyped(KeyEvent e) {
                           }

                           @Override
                           public void keyPressed(final KeyEvent e) {
                               switch (e.getKeyCode()) {
                                   case KeyEvent.VK_P: {
                                       System.out.println(pacman.getX() + "\t" + pacman.getY());
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
                    while (!dots.isEmpty()) {
                        pacman.move();
                        eatenDots();
                        try {
                            Thread.sleep(FRAMERATE);
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
        blinkyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!dots.isEmpty()) {
                    blinky.move();
                    blinky.setCurrent_target(new int[] {pacman.getRealX(), pacman.getRealY()});
                    inky.move();
                    pinky.move();
                    clyde.move();
                    try {
                        Thread.sleep(5, 5);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
        blinkyThread.start();
        setVisible(true);

    }

    private boolean catched() {

        return false;
    }

    private void newGame() {
        pacman.reStart();
    }

    private void eatenDots() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < dots.size(); i++) {
                    if (pacman.getRealX() - 2 == dots.get(i).getX() && pacman.getRealY() - 2 == dots.get(i).getY()
                            || pacman.getRealX() - 8 == dots.get(i).getX() && pacman.getRealY() - 8 == dots.get(i).getY()) {
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