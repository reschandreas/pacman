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

    protected int x_speed = 0;
    protected int y_speed = 0;
    private long points = 0;

    private int lives = 0;

    protected int[] startpos = new int[2];

    private BufferedImage image_right = null;
    private BufferedImage image_left = null;
    private BufferedImage image_up = null;
    private BufferedImage image_down = null;
    private BufferedImage image_next = null;

    private boolean eatable = true;

    protected int x_next = 0;
    protected int y_next = 0;

    protected long speed = 5;

    public Pacman(String path) {
        super(path);
        startpos[0] = 208;
        startpos[1] = 408;
        setLocation(startpos[0], startpos[1]);
        lives = 3;
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

    public void reStart() {
        setLocation(startpos[0], startpos[1]);
        lives = 3;
        points = 0;
        x_speed = x_next = -1;
        y_speed = y_next = 0;
        image = image_next = image_left;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public boolean isEatable() {
        return eatable;
    }

    public void setEatable(boolean eatable) {
        this.eatable = eatable;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getX_next() {
        return x_next;
    }

    public void setX_next(int x_next) {
        this.x_next = x_next;
        if (x_next == -1) {
            if (image_left != null)
                image_next = image_left;
        } else if (image_right != null)
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
        if (y_next == -1) {
            if (image_up != null)
                image_next = image_up;
        } else if (image_down != null)
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
        if (image_next != null)
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

    protected void moveHorizontal() {
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

    protected void moveVertical() {
        if (y_speed == 1 || y_speed == -1) {
            if (!(getObjektBei(getX(), getY() + y_speed) instanceof Wall)) {
                setLocation(getX(), getY() + y_speed);
            }
        }
    }

    public void move() {
        Intersection intersection = intersectionCheck();
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

    protected Intersection intersectionCheck() {
        for (Intersection i : PacmanGUI.intersections) {
            if (getX() == i.getX() && getY() == i.getY()) {
                return i;
            }
        }
        return null;
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
    public Component getObjektBei(int x, int y) {
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