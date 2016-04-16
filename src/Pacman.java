import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Andreas on 03.04.16.
 */
public class Pacman extends Wall {

    private int x_speed = 0;
    private int y_speed = 0;
    private long points = 0;

    private BufferedImage image_right;
    private BufferedImage image_left;
    private BufferedImage image_up;
    private BufferedImage image_down;
    private BufferedImage image_next;

    private int x_next = 0;
    private int y_next = 0;

    public Pacman(String path) {
        super(path);
        x_speed = -1;
        x_next = -1;
    }

    public Pacman(String up, String down, String left, String right) {
        this(left);
        try {
            image_left = ImageIO.read(new File(getClass().getResource(left).toURI()));
            image_next = image_left;
            image_right = ImageIO.read(new File(getClass().getResource(right).toURI()));
            image_up = ImageIO.read(new File(getClass().getResource(up).toURI()));
            image_down = ImageIO.read(new File(getClass().getResource(down).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public int getX_next() {
        return x_next;
    }

    public void setX_next(int x_next) {
        this.x_next = x_next;
        if (x_next == -1)
            image_next = image_left;
        else
            image_next = image_right;
        y_next = 0;
    }

    public int getX_speed() {
        return x_speed;
    }

    public void setX_speed(int x_speed) {
        this.x_speed = x_speed;
    }

    public int getY_next() {
        return y_next;
    }

    public void setY_next(int y_next) {
        this.y_next = y_next;
        if (y_next == -1)
            image_next = image_up;
        else
            image_next = image_down;
        x_next = 0;
    }

    public int getY_speed() {
        return y_speed;
    }

    public void setY_speed(int y_speed) {
        this.y_speed = y_speed;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    private void changeDirection() {
        image = image_next;
        x_speed = x_next;
        y_speed = y_next;
    }

    public int getRealX() {
        return getX() + getWidth() / 2;
    }

    public int getRealY() {
        return getY() + getHeight() / 2;
    }

    private void moveHorizontal() {
        if (x_speed == 1 || x_speed == -1) {
            if (x_speed == -1 && getX() < 0) {
                setLocation(getParent().getWidth(), getY());
            } else if (x_speed == 1 && getX() + getWidth() + x_speed > getParent().getWidth()) {
                setLocation(x_speed, getY());
            } else if (!(getObjektBei(getX() + x_speed, getY()) instanceof Wall)) {
                setLocation(getX() + x_speed, getY());
            }
        }
    }

    private void moveVertical() {
        if (y_speed == 1 || y_speed == -1) {
            if (!(getObjektBei(getX(), getY() + y_speed) instanceof Wall)) {
                setLocation(getX(), getY() + y_speed);
            }
        }
    }

    public void move() {
        Intersection intersection = PacmanGUI.intersectionCheck();
        if (intersection != null && (intersection.isLeft() && x_next == -1 || intersection.isRight() && x_next == 1
                || intersection.isUp() && y_next == -1 || intersection.isDown() && y_next == 1)) {
            changeDirection();
            moveHorizontal();
            moveVertical();
        } else {
            intersection = null;
        }
        if (intersection == null) {
            moveHorizontal();
            moveVertical();
        }
    }

    /**
     * Kontrolliert ob das Objekt - verschoben zur Ã¼bergebenen x- und y-Position - mit
     * einem anderen Objekt kollidiert. Ist das der Fall, so wird das andere kollidierende
     * Objekt zurÃ¼ck geliefert.<br>
     * Es wird ebenfalls der contentPane des Formulars zurÃ¼ck geliefert, falls das Objekt
     * auÃŸerhalb des Formulars positioniert werden sollte, d. h. Ã¼ber den Rand des
     * Formulars hinaus ragen wÃ¼rde
     *
     * @param x die zu kontrollierende x-Koordinate
     * @param y die zu kontrollierende y-Koordinate
     * @return das Objekt das mit dem Objekt kollidiert oder - falls das Objekt an der
     * Ã¼bergebenen Position auÃŸerhalb des Frames positioniert wird - wird der contentPane
     * zurÃ¼ck geliefert. Liefert null zurÃ¼ck, falls das Objekt ohne Ãœberdeckung an der
     * Ã¼bergebenen Position positioniert werden kann
     */
    private Component getObjektBei(int x, int y) {
        Component ret = null;
        if (this.getParent() != null) {
            // Kontrolliere ob neue Position auÃŸerhalb des Frames liegt
            if (x < 0 || y < 0 || x + this.getWidth() > this.getParent().getWidth() ||
                    y + this.getHeight() > this.getParent().getHeight())
                // In diesem Fall wird der contentPane des Formulars Ã¼bergeben
                ret = this.getParent();
            else {
                // Kontrolliere ob sich die neue Position mit anderen Objekten Ã¼berdeckt
                Rectangle neuePosition =
                        new Rectangle(x, y, this.getWidth(), this.getHeight());
                // Gehe alle Objekte des Formulars durch und vergleiche ihre Position mit der
                // neuen Position
                Component[] komponenten = this.getParent().getComponents();
                int i = 0;
                while (komponenten != null && i < komponenten.length && ret == null) {
                    // Wenn das Objekt nicht das zu kontrollierende Objekt ist und das Objekt
                    // mit dem zu Kontrollierendem zusammenfällt
                    if (komponenten[i] != this && neuePosition.intersects(komponenten[i].getBounds()) && !(komponenten[i] instanceof Pacman))
                        ret = komponenten[i];
                    i++;
                }
            }
        }
        return ret;
    }
}