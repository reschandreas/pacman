package io.resch.pacman.movable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Andreas on 03.04.16.
 */
public class Dot extends JComponent {

    protected Image image = null;
    protected String image_path = null;
    protected int points;

    protected boolean dead = false;

    public Dot(String path, int x, int y) {
        this(path);
        points = 10;
        setLocation(x, y);
    }

    public Dot(String path) {
        image_path = path;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(path);

        if (input == null)
            System.out.println("Dot file not Found");
        else {
            try {
                image = ImageIO.read(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Stelle Größe des Objektes auf Größe des Bildes ein
            this.setSize(this.image.getWidth(this), this.image.getHeight(this));
        }
    }

    public int getPoints() {
        return points;
    }

    /**
     * Methode, welche automatisch aufgerufen wird und das Bild in der Größe des Objektes
     * darstellt
     */
    public void paint(Graphics g) {
        if (image != null) {
            g.drawImage(this.image, 0, 0, this);
        }
    }

    /**
     * Objekt stirbt, indem es sich selbständig vom contentPane des Formulars entfernt.
     * Dadurch wird das Objekt auch nicht mehr angezeigt
     */
    public void die() {
        if (!this.dead && this.getParent() != null) {
            this.getParent().remove(this);
            this.dead = true;
        }
    }
}