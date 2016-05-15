package programm;

import net.gobbz.grundobjekte.Intersection;
import net.gobbz.spielobjekte.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;

/**
 * Created by Andreas on 15.05.16.
 */
public class MenuGUI extends JFrame {

    final JFrame jFrame;
    final Container container;
    final Pacman pacman;
    final ArrayList<Ghost> ghosts = new ArrayList<>();

    public MenuGUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0, 0, 448, 576);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        jFrame = this;
        container = getContentPane();
        container.setBackground(Color.black);

        final JLabel title = new JLabel("Pacman");
        title.setFont(new Font("PacFont", Font.BOLD, 32));
        title.setBounds(130, 110, 300, 50);
        title.setForeground(Color.white);
        container.add(title);

        JButton b_newgame = new JButton("Neues Spiel");
        b_newgame.setFont(new Font("PacFont", Font.BOLD, 24));
        b_newgame.setBounds(75, 160, 300, 50);
        container.add(b_newgame);
        b_newgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JDialog jd_name = new JDialog();
                jd_name.setLayout(null);
                jd_name.setBounds(0, 0, 160, 30);
                jd_name.setUndecorated(true);
                jd_name.setLayout(null);
                jd_name.setAlwaysOnTop(true);
                final JTextField name = new JTextField("Dein Name");
                name.setBounds(0, 0, 100, 30);
                jd_name.add(name);
                JButton start = new JButton("Start!");
                start.setBounds(100, 0, 60, 30);
                start.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (name.getText() != null && !name.getText().isEmpty()) {
                            jd_name.dispose();
                            new PacmanGUI();
                        }
                    }
                });
                jd_name.setLocationRelativeTo(jFrame);
                jd_name.add(start);
                jd_name.setVisible(true);

            }
        });

        JButton highscores = new JButton("Highscores");
        highscores.setFont(new Font("PacFont", Font.BOLD, 24));
        highscores.setBounds(75, 210, 300, 50);
        container.add(highscores);

        pacman = new Pacman("pacman_up.png", "pacman_down.png", "pacman_left.png", "pacman_right.png");
        pacman.setLocation(0, 0);
        pacman.setImage(pacman.getImage_right());
        pacman.setX_speed(1);
        container.add(pacman);

        ghosts.add(new Blinky());
        container.add(ghosts.get(0));
        ghosts.get(0).setLocation(0, 0);
        ghosts.get(0).setX_speed(1);

        ghosts.add(new Inky());
        container.add(ghosts.get(1));
        ghosts.get(1).setLocation(0, 0);
        ghosts.get(1).setX_speed(1);

        ghosts.add(new Pinky());
        container.add(ghosts.get(2));
        ghosts.get(2).setLocation(0, 0);
        ghosts.get(2).setX_speed(1);

        ghosts.add(new Clyde());
        container.add(ghosts.get(3));
        ghosts.get(3).setLocation(0, 0);
        ghosts.get(3).setX_speed(1);

        final long starttime = System.currentTimeMillis();
        Thread animation = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (System.currentTimeMillis() - starttime >= 1000) {
                        ghosts.get(0).move();
                    }
                    if (System.currentTimeMillis() - starttime >= 1350) {
                        ghosts.get(1).move();
                    }
                    if (System.currentTimeMillis() - starttime >= 1700) {
                        ghosts.get(2).move();
                    }
                    if (System.currentTimeMillis() - starttime >= 2050) {
                        ghosts.get(3).move();
                    }
                    for (Ghost ghost : ghosts) {
                        if (ghost.getX() == 0 && ghost.getY() == 0) {
                            ghost.setX_speed(1);
                            ghost.setY_speed(0);
                        }
                        if (ghost.getX() == 416 && ghost.getY() == 0) {
                            ghost.setX_speed(0);
                            ghost.setY_speed(1);
                        }
                        if (ghost.getX() == 416 && ghost.getY() == 544) {
                            ghost.setX_speed(-1);
                            ghost.setY_speed(0);
                        }
                        if (ghost.getX() == 0 && ghost.getY() == 544) {
                            ghost.setX_speed(0);
                            ghost.setY_speed(-1);
                        }
                    }

                    pacman.move();
                    if (pacman.getX() == 0 && pacman.getY() == 0) {
                        pacman.setX_speed(1);
                        pacman.setY_speed(0);
                        pacman.setImage(pacman.getImage_right());
                    }
                    if (pacman.getX() == 416 && pacman.getY() == 0) {
                        pacman.setX_speed(0);
                        pacman.setY_speed(1);
                        pacman.setImage(pacman.getImage_down());
                    }
                    if (pacman.getX() == 416 && pacman.getY() == 544) {
                        pacman.setX_speed(-1);
                        pacman.setY_speed(0);
                        pacman.setImage(pacman.getImage_left());
                    }
                    if (pacman.getX() == 0 && pacman.getY() == 544) {
                        pacman.setX_speed(0);
                        pacman.setY_speed(-1);
                        pacman.setImage(pacman.getImage_up());
                    }
                    try {
                        Thread.sleep(pacman.getSpeed());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        animation.start();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE: {
                        jFrame.dispose();
                        System.exit(0);
                        break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        g.fillRect(0, 0, getWidth(), getHeight());
        for (Component component : container.getComponents()) {
            component.repaint();
        }
    }
}
