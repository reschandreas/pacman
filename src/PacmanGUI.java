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
    private ArrayList<Intersection> intersections = new ArrayList<>();

    final private Pacman pacman = new Pacman("images/pacman_right.png");
    private Ghost ghost_blue = new Ghost("images/ghost_pink.png");
    private Thread moveThread;
    private Container container;

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

        ghost_blue.setBounds(random.nextInt(WIDTH / 30) * 30, random.nextInt(HEIGHT / 30) * 30, ghost_blue.getWidth(), ghost_blue.getHeight());
        container.add(ghost_blue);
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
                        System.out.println(pacman.getX() + "\t" + pacman.getY() + "\n");
                    }
                }
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                moveThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        switch (e.getKeyCode()) {
                            //UP
                            case KeyEvent.VK_W:
                                if (!up) {
                                    up = true;
                                    down = false;
                                    right = false;
                                    left = false;
                                    while (!down && !pacman.moveUp() && (!intersectionCheck() || up)) {
                                        try {
                                            Thread.sleep(FRAMERATE);
                                        } catch (InterruptedException ignored) {
                                        }
                                    }
                                }
                                break;
                            //DOWN
                            case KeyEvent.VK_S:
                                if (!down) {
                                    up = false;
                                    down = true;
                                    right = false;
                                    left = false;
                                    while (!up && !pacman.moveDown() && (!intersectionCheck() || down)) {
                                        try {
                                            Thread.sleep(FRAMERATE);
                                        } catch (InterruptedException ignored) {
                                        }
                                    }
                                }
                                break;
                            //LEFT
                            case KeyEvent.VK_A:
                                if (!left) {
                                    up = false;
                                    down = false;
                                    right = false;
                                    left = true;
                                    while (!right && !pacman.moveLeft() && (!intersectionCheck() || left)) {
                                        try {
                                            Thread.sleep(FRAMERATE);
                                        } catch (InterruptedException ignored) {
                                        }
                                    }
                                }
                                break;
                            //RIGHT
                            case KeyEvent.VK_D:
                                if (!right) {
                                    up = false;
                                    down = false;
                                    right = true;
                                    left = false;
                                    while (!left && !pacman.moveRight() && (!intersectionCheck() || right)) {
                                        try {
                                            Thread.sleep(FRAMERATE);
                                        } catch (InterruptedException ignored) {
                                        }
                                    }
                                }
                                break;
                        }
                    }
                });

                moveThread.start();
            }
        });
        setVisible(true);

    }

    private boolean intersectionCheck() {
        for (Intersection i : intersections) {
            if (pacman.getX() == i.getX() && pacman.getY() == i.getY()) {
                return true;
            }
        }
        return false;
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
    }

    @Override
    public void paint(Graphics g) {
        Insets insets = getInsets();
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.green);
        for (int i = insets.top; i < HEIGHT; i += RESOLUTION) {
            g.drawLine(0, i, HEIGHT, i);
        }
        for (int i = 0; i < WIDTH; i += RESOLUTION) {
            g.drawLine(i, insets.top, i, HEIGHT);
        }
        for (Component component : container.getComponents()) {
            component.repaint();
        }
    }

    public static void main(String[] args) {
        new PacmanGUI();
    }
}