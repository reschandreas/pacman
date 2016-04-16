import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created by Andreas on 03.04.16.
 */
public class Dot extends JComponent {

    protected Image image = null;
    protected String image_path = null;
    protected int points;

    protected boolean dead = false;

    public Dot( String path, int x, int y) {
        this(path);
        points = 10;
        setLocation(x, y);
    }

    public Dot(String path) {
        image_path = path;
        URL url = this.getClass().getResource(path);
        if (url == null)
            System.out.println("File not Found");
        else {
            image = getToolkit().getImage(url);
            prepareImage(image, this);
            Thread t = Thread.currentThread();
            // Warte bis die Eigenschaften des Bildes geladen sind
            while ((checkImage(image, this) & PROPERTIES) != PROPERTIES) {
                try {
                    // Pause, um dem Ladevorgang keine Ressourcen zu nehmen
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Stelle Größe des Objektes auf Größe des Bildes ein
            this.setSize(this.image.getWidth(this), this.image.getHeight(this));
        }
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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

    public void setImage(Image image) {
        this.image = image;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}