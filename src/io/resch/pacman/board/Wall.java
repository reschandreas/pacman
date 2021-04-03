package io.resch.pacman.board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Andreas on 03.04.16.
 */
public class Wall extends JComponent {

    protected Image image = null;
    private String image_path = null;

    public Wall(String path) {
        image_path = path;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(path);

        if (input == null) {
            System.out.println("file not found --- " + path);
        } else {
            try {
                this.image = image = ImageIO.read(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Stelle Größe des Objektes auf Größe des Bildes ein
            this.setSize(this.image.getWidth(this), this.image.getHeight(this));
        }
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

    public void setImage(Image image) {
        this.image = image;
    }
}
