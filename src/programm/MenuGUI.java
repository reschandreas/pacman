package programm;

import net.gobbz.spielobjekte.*;
import sun.plugin.javascript.navig.Array;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Andreas on 15.05.16.
 */
public class MenuGUI extends JFrame {

    private final JFrame jFrame;
    private final Container container;
    private final Pacman pacman;
    private final ArrayList<Ghost> ghosts = new ArrayList<>();
    private ArrayList<Score> highscores = new ArrayList<>();
    private String name;
    private long score;

    private JTextField tf_name = null;

    public MenuGUI() {
        setBounds(0, 0, 448, 576);
        setUndecorated(true);
        setFocusable(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        jFrame = this;
        setFocusable(true);
        container = getContentPane();
        container.setBackground(Color.black);

        final JLabel title = new JLabel("Pacman");
        title.setFont(new Font("PacFont", Font.BOLD, 32));
        title.setBounds(130, 110, 300, 50);
        title.setForeground(Color.yellow);
        container.add(title);

        JButton b_newgame = new JButton("Neues Spiel");
        b_newgame.setFont(new Font("PacFont", Font.BOLD, 24));
        b_newgame.setBounds(75, 160, 300, 50);
        container.add(b_newgame);
        b_newgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        JButton highscores = new JButton("Highscores");
        highscores.setFont(new Font("PacFont", Font.BOLD, 24));
        highscores.setBounds(75, 210, 300, 50);
        container.add(highscores);
        highscores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showScores();
            }
        });

        tf_name = new JTextField("Dein Name", SwingConstants.CENTER);
        tf_name.setFont(new Font("Arial", Font.BOLD, 24));
        tf_name.setBounds(75, 260, 300, 50);
        container.add(tf_name);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        jFrame.dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        pacman = new Pacman();
        pacman.setLocation(0, 0);
        pacman.setImage(pacman.getImage_right());
        pacman.setX_speed(1);
        pacman.setX_next(1);
        container.add(pacman);

        ghosts.add(new Blinky());
        container.add(ghosts.get(0));
        ghosts.get(0).setLocation(0, 0);

        ghosts.add(new Inky());
        container.add(ghosts.get(1));
        ghosts.get(1).setLocation(0, 0);
        ghosts.get(1).setX_speed(1);
        ghosts.get(1).setX_next(1);

        ghosts.add(new Pinky());
        container.add(ghosts.get(2));
        ghosts.get(2).setLocation(0, 0);
        ghosts.get(2).setX_speed(1);
        ghosts.get(2).setX_next(1);

        ghosts.add(new Clyde());
        container.add(ghosts.get(3));
        ghosts.get(3).setLocation(0, 0);
        ghosts.get(3).setX_speed(1);
        ghosts.get(3).setX_next(1);

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
                            ghost.setX_next(1);
                            ghost.setY_speed(0);
                        }
                        if (ghost.getX() == 416 && ghost.getY() == 0) {
                            ghost.setX_speed(0);
                            ghost.setY_speed(1);
                            ghost.setY_next(1);
                        }
                        if (ghost.getX() == 416 && ghost.getY() == 544) {
                            ghost.setX_speed(-1);
                            ghost.setY_speed(0);
                            ghost.setX_next(-1);
                        }
                        if (ghost.getX() == 0 && ghost.getY() == 544) {
                            ghost.setX_speed(0);
                            ghost.setY_speed(-1);
                            ghost.setY_next(-1);
                        }
                    }

                    pacman.move();
                    if (pacman.getX() == 0 && pacman.getY() == 0) {
                        pacman.setX_speed(1);
                        pacman.setX_next(1);
                        pacman.setY_speed(0);
                        pacman.setImage(pacman.getImage_right());
                    }
                    if (pacman.getX() == 416 && pacman.getY() == 0) {
                        pacman.setX_speed(0);
                        pacman.setY_speed(1);
                        pacman.setY_next(1);
                        pacman.setImage(pacman.getImage_down());
                    }
                    if (pacman.getX() == 416 && pacman.getY() == 544) {
                        pacman.setX_speed(-1);
                        pacman.setY_speed(0);
                        pacman.setX_next(-1);
                        pacman.setImage(pacman.getImage_left());
                    }
                    if (pacman.getX() == 0 && pacman.getY() == 544) {
                        pacman.setX_speed(0);
                        pacman.setY_speed(-1);
                        pacman.setY_next(-1);
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

    private void readScores() {
        Scanner reader = new Scanner(PacmanGUI.class.getResourceAsStream("scores.data")).useDelimiter("\\n");
        while (true) {
            String line = reader.hasNext() ? reader.next() : "";
            if (line == null || line.isEmpty())
                // Dateiende erkannt
                break;
            else {
                String[] strings = line.split(";");
                System.out.println(Arrays.toString(strings));
                highscores.add(new Score(strings[0], Long.parseLong(strings[1])));
            }
        }

    }

    private void startGame() {
        if (tf_name.getText() != null && !tf_name.getText().isEmpty() && !tf_name.getText().equals("Dein Name")) {
            name = tf_name.getText();
            PacmanGUI pacmanGUI = new PacmanGUI();
            score = pacmanGUI.checkGameOver();
            highscores.add(new Score(name, score));
        } else {
            final JDialog dialog = new JDialog();
            dialog.setBounds(0, 0, 200, 100);
            dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            dialog.setLayout(null);
            dialog.setTitle("Falsche Eingabe");
            dialog.setResizable(false);

            JLabel message = new JLabel();
            message.setBounds(0, 0, 200, 50);
            message.setText("Gib einen gültigen Namen ein!");
            dialog.add(message);

            JButton ok_button = new JButton("OK");
            ok_button.setBounds(75, 50, 50, 25);
            dialog.add(ok_button);
            ok_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });
            dialog.setLocationRelativeTo(jFrame);
            dialog.setVisible(true);
        }
    }

    private void showScores() {
        final JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE: {
                        dialog.dispose();
                        writeScores();
                        break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        dialog.setBounds(0, 0, 300, 400);
        dialog.setUndecorated(true);
        dialog.setLayout(null);
        dialog.setAlwaysOnTop(true);
        JLabel title = new JLabel("Highscores");
        title.setFont(new Font("PacFont", Font.BOLD, 24));
        title.setForeground(Color.white);
        title.setBounds(45, 0, 300, 30);
        dialog.setLocationRelativeTo(jFrame);
        dialog.add(title);
        dialog.getContentPane().setBackground(Color.black);
        readScores();
        sortScores();
        for (int i = 0; i < highscores.size(); i++) {
            JLabel name = new JLabel(i + 1 + "." + highscores.get(i).getName());
            name.setBounds(45, 40 + i * 30, 100, 30);
            name.setForeground(Color.WHITE);
            JLabel score = new JLabel(String.valueOf(highscores.get(i).getScore()));
            score.setBounds(145, 40 + i * 30, 100, 30);
            score.setForeground(Color.white);
            dialog.add(name);
            dialog.add(score);
        }
        dialog.setVisible(true);
    }

    private void sortScores() {
        for (int i = 0; i < highscores.size(); i++) {
            for (int j = i; j < highscores.size(); j++) {
                Score temp;
                temp = highscores.get(j).clone();
                if (temp.compareTo(highscores.get(i)) > 0) {
                    highscores.get(j).reSet(highscores.get(i).clone());
                    highscores.get(i).reSet(temp.clone());
                }
            }
        }
    }

    private void writeScores() {
        sortScores();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("scores.data"));
            for (Score x : highscores) {
                if (x != null) {
                    System.out.println(x.toString());
                    writer.write(x.toString());
                    writer.write("\n");
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.fillRect(0, 0, getWidth(), getHeight());
        for (Component component : container.getComponents()) {
            component.repaint();
        }
    }
}
