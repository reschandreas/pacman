package io.resch.pacman.gui;

import io.resch.pacman.movable.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by Andreas on 15.05.16.
 */
public class MenuGUI extends JFrame {

    private final JFrame jFrame;
    private final Container container;
    static ArrayList<Score> highscores = new ArrayList<>();

    private final JTextField tf_name;

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
        b_newgame.addActionListener(e -> startGame());

        JButton highscores = new JButton("Highscores");
        highscores.setFont(new Font("PacFont", Font.BOLD, 24));
        highscores.setBounds(75, 210, 300, 50);
        container.add(highscores);
        highscores.addActionListener(e -> showScores());

        tf_name = new JTextField("Dein Name", SwingConstants.CENTER);
        tf_name.setFont(new Font("Serif", Font.BOLD, 24));
        tf_name.setBounds(75, 260, 300, 50);
        container.add(tf_name);
        readScores();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    writeScores();
                    jFrame.dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        addChasingPacmanAnimation();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    jFrame.dispose();
                    System.exit(0);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        setVisible(true);
    }

    private void addChasingPacmanAnimation() {
        new Thread(new ChasingAnimation(container)).start();
    }

    private void readScores() {
        if (highscores.isEmpty()) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream input = classLoader.getResourceAsStream("data/scores.data");
                if (input == null) {
                    return;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
                while (true) {
                    String line = reader.readLine();
                    if (line == null || line.isEmpty())
                        // Dateiende erkannt
                        break;
                    else {
                        String[] strings = line.split(";");
                        highscores.add(new Score(strings[0], Integer.parseInt(strings[1]), Long.parseLong(strings[2])));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startGame() {
        if (tf_name.getText() != null && !tf_name.getText().isEmpty() && !tf_name.getText().equals("Dein Name")) {
            PacmanGUI.playerpoints = 0;
            PacmanGUI.playername = tf_name.getText();
            new PacmanGUI();
            jFrame.dispose();
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
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dialog.dispose();
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
        sortScores();
        writeScores();
        for (int i = 0; i < highscores.size(); i++) {
            JLabel name = new JLabel(i + 1 + "." + highscores.get(i).getName());
            name.setBounds(45, 40 + i * 30, 200, 30);
            name.setForeground(Color.WHITE);
            JLabel score = new JLabel(String.valueOf(highscores.get(i).getScore()));
            score.setBounds(245, 40 + i * 30, 100, 30);
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
            BufferedWriter writer = new BufferedWriter(new FileWriter("./scores.data"));
            for (Score highscore : highscores) {
                if (highscore != null) {
                    writer.write(highscore.toString());
                    writer.write("\n");
                    writer.flush();
                }
            }
            writer.close();
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
