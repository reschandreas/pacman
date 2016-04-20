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

    protected BufferedImage image = null;
    protected String image_path = null;

    protected boolean dead = false;

    public Wall(String path) {
        image_path = path;
        try {
            image = ImageIO.read(new File(getClass().getResource(path).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        // Stelle Größe des Objektes auf Größe des Bildes ein
        this.setSize(this.image.getWidth(this), this.image.getHeight(this));
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

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
