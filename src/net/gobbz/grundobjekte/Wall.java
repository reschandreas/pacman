package net.gobbz.grundobjekte;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Andreas on 03.04.16.
 */
public class Wall extends JComponent {

    protected Image image = null;
    private String image_path = null;

    public Wall(String path) {
        image_path = path;
/*
        try {
            image = ImageIO.read(new File(getClass().getResource(path).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        // Stelle Größe des Objektes auf Größe des Bildes ein
        this.setSize(this.image.getWidth(this), this.image.getHeight(this));
*/

        URL url = this.getClass().getResource(path);
        if (url == null)
            System.out.println("Datei nicht gefunden");
        else {
            this.image = getToolkit().getImage(url);
            prepareImage(image, this);
            Thread t = Thread.currentThread();
            // Warte bis die Eigenschaften des Bildes geladen sind
            while ((checkImage(image, this) & PROPERTIES) != PROPERTIES) {
                try {
                    // Pause, um dem Ladevorgang keine Ressourcen zu nehmen
                    Thread.sleep(50);
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Stelle Größe des Objektes auf Größe des Bildes ein
            this.setSize(this.image.getWidth(this),this.image.getHeight(this));
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
