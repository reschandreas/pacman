import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.MalformedURLException;
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


    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    private Random random = new Random();

    private ArrayList<Wall> walls = new ArrayList<>();
    private static ArrayList<Intersection> intersections = new ArrayList<>();
    private ArrayList<Dot> dots = new ArrayList<>();
    private ArrayList<Tile> tiles = new ArrayList<>();

    static final private Pacman pacman = new Pacman("images/pacman_right.png");
    private Blinky blinky = new Blinky("images/ghost_red.png");
    private Thread moveThread;
    private Thread blinkyThread;
    private Container container;

    private int i = 0;

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

        //Startpunkt
        pacman.setBounds(208, 408, pacman.getWidth(), pacman.getHeight());
        pacman.setX_speed(-1);
        container.setBackground(Color.black);
        //Loadingscreen
        JWindow window = new JWindow();
        window.setLocationRelativeTo(null);
        window.setBounds(getX(), getY(), WIDTH, HEIGHT);
        window.setVisible(true);

        drawMaze();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.dispose();

        container.add(blinky);
        container.add(pacman);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(getMousePosition().getX() + "\t" + getMousePosition().getY());
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

        addKeyListener(new KeyListener() {
                           @Override
                           public void keyTyped(KeyEvent e) {
                           }

                           @Override
                           public void keyPressed(final KeyEvent e) {
                               switch (e.getKeyCode()) {
                                   case KeyEvent.VK_P: {
                                       //System.out.println(pacman.getX() + "\t" + pacman.getY() + "\n");
                                       dots.add(new Dot(pacman.getX() + 12, pacman.getY() + 12, "images/dots.png"));
                                       System.out.println(dots.get(i).getX() + "" + dots.get(i).getY());
                                       container.add(dots.get(i++));
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
                while(true) {
                    pacman.move();
                    try {
                        Thread.sleep(FRAMERATE);
                    } catch (InterruptedException ignored) {}
                }
            }
        });
        moveThread.start();

        blinkyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(FRAMERATE);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
        blinkyThread.start();
        setVisible(true);

    }

    public static Intersection intersectionCheck() {
        for (Intersection i : intersections) {
            if (pacman.getX() == i.getX() && pacman.getY() == i.getY()) {
                return i;
            }
        }
        return null;
    }

    private void drawMaze() {
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
       int k = 0;
        for (int i = 56; i < HEIGHT; i += RESOLUTION) {
            for (int j = 8; j < WIDTH; j += RESOLUTION) {
                dots.add(new Dot(j + 12, i + 12, "images/dots.png"));
                container.add(dots.get(k));
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        Insets insets = getInsets();
        g.fillRect(0, 0, WIDTH, HEIGHT);
        //g.setColor(Color.green);
        /*
        for (int i = insets.top; i < HEIGHT; i += RESOLUTION) {
            g.drawLine(0, i, HEIGHT, i);
        }
        for (int i = 0; i < WIDTH; i += RESOLUTION) {
            g.drawLine(i, insets.top, i, HEIGHT);
        }*/
        for (Component component : container.getComponents()) {
            component.repaint();
        }
    }

    public static void main(String[] args) {
        new PacmanGUI();
    }
}