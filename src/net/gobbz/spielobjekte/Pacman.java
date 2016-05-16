package net.gobbz.spielobjekte;

import net.gobbz.grundobjekte.Wall;

import net.gobbz.grundobjekte.*;
import programm.*;
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
public class Pacman extends Wall {

    int x_speed = 0;
    int y_speed = 0;
    private long points = 0;

    private int lives = 0;

    int[] startpos = new int[2];

    private Image image_right = null;
    private Image image_left = null;
    private Image image_up = null;
    private Image image_down = null;
    private Image image_next = null;

    private boolean eatable = true;

    int x_next = 0;
    int y_next = 0;

    long speed = 5;

    protected Pacman(String path) {
        super(path);
        setEatable(true);
        startpos[0] = 208;
        startpos[1] = 408;
        setLocation(startpos[0], startpos[1]);
        lives = 3;
        x_speed = -1;
        x_next = -1;
    }

    public Pacman() {
        this("pacman_left.png");
        URL url = this.getClass().getResource("pacman_up.png");
        if (url == null)
            System.out.println("Datei nicht gefunden");
        else {
            this.image_up = getToolkit().getImage(url);
            prepareImage(image_up, this);
            Thread t = Thread.currentThread();
            // Warte bis die Eigenschaften des Bildes geladen sind
            while ((checkImage(image_up, this) & PROPERTIES) != PROPERTIES) {
                try {
                    // Pause, um dem Ladevorgang keine Ressourcen zu nehmen
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        url = this.getClass().getResource("pacman_down.png");
        if (url == null)
            System.out.println("Datei nicht gefunden");
        else {
            this.image_down = getToolkit().getImage(url);
            prepareImage(image_down, this);
            Thread t = Thread.currentThread();
            // Warte bis die Eigenschaften des Bildes geladen sind
            while ((checkImage(image_down, this) & PROPERTIES) != PROPERTIES) {
                try {
                    // Pause, um dem Ladevorgang keine Ressourcen zu nehmen
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        url = this.getClass().getResource("pacman_left.png");
        if (url == null)
            System.out.println("Datei nicht gefunden");
        else {
            this.image_left = getToolkit().getImage(url);
            prepareImage(image_left, this);
            Thread t = Thread.currentThread();
            // Warte bis die Eigenschaften des Bildes geladen sind
            while ((checkImage(image_left, this) & PROPERTIES) != PROPERTIES) {
                try {
                    // Pause, um dem Ladevorgang keine Ressourcen zu nehmen
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        url = this.getClass().getResource("pacman_right.png");
        if (url == null)
            System.out.println("Datei nicht gefunden");
        else {
            this.image_right = getToolkit().getImage(url);
            prepareImage(image_right, this);
            Thread t = Thread.currentThread();
            // Warte bis die Eigenschaften des Bildes geladen sind
            while ((checkImage(image_right, this) & PROPERTIES) != PROPERTIES) {
                try {
                    // Pause, um dem Ladevorgang keine Ressourcen zu nehmen
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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

    public Image getImage_down() {
        return image_down;
    }

    public void setImage_down(Image image_down) {
        this.image_down = image_down;
    }

    public Image getImage_left() {
        return image_left;
    }

    public void setImage_left(Image image_left) {
        this.image_left = image_left;
    }

    public Image getImage_next() {
        return image_next;
    }

    public void setImage_next(Image image_next) {
        this.image_next = image_next;
    }

    public Image getImage_right() {
        return image_right;
    }

    public void setImage_right(Image image_right) {
        this.image_right = image_right;
    }

    public Image getImage_up() {
        return image_up;
    }

    public void setImage_up(Image image_up) {
        this.image_up = image_up;
    }

    public int[] getStartpos() {
        return startpos;
    }

    public void setStartpos(int[] startpos) {
        this.startpos = startpos;
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
        for (int i = 0; i < PacmanGUI.intersections.size(); i++) {
            if (getX() == PacmanGUI.intersections.get(i).getX() && getY() == PacmanGUI.intersections.get(i).getY()) {
                return PacmanGUI.intersections.get(i);
            }
        }
        return null;
    }

    /**
     * Kontrolliert ob das Objekt - verschoben zur ÃƒÂ¼bergebenen x- und y-Position - mit
     * einem anderen Objekt kollidiert. Ist das der Fall, so wird das andere kollidierende
     * Objekt zurÃƒÂ¼ck geliefert.<br>
     * Es wird ebenfalls der contentPane des Formulars zurÃƒÂ¼ck geliefert, falls das Objekt
     * auÃƒÅ¸erhalb des Formulars positioniert werden sollte, d. h. ÃƒÂ¼ber den Rand des
     * Formulars hinaus ragen wÃƒÂ¼rde
     *
     * @param x die zu kontrollierende x-Koordinate
     * @param y die zu kontrollierende y-Koordinate
     * @return das Objekt das mit dem Objekt kollidiert oder - falls das Objekt an der
     * ÃƒÂ¼bergebenen Position auÃƒÅ¸erhalb des Frames positioniert wird - wird der contentPane
     * zurÃƒÂ¼ck geliefert. Liefert null zurÃƒÂ¼ck, falls das Objekt ohne ÃƒÅ“berdeckung an der
     * ÃƒÂ¼bergebenen Position positioniert werden kann
     */
    public Component getObjektBei(int x, int y) {
        Component ret = null;
        if (this.getParent() != null) {
            // Kontrolliere ob neue Position auÃƒÅ¸erhalb des Frames liegt
            if (x < 0 || y < 0 || x + this.getWidth() > this.getParent().getWidth() ||
                    y + this.getHeight() > this.getParent().getHeight())
                // In diesem Fall wird der contentPane des Formulars ÃƒÂ¼bergeben
                ret = this.getParent();
            else {
                // Kontrolliere ob sich die neue Position mit anderen Objekten ÃƒÂ¼berdeckt
                Rectangle neuePosition =
                        new Rectangle(x, y, this.getWidth(), this.getHeight());
                // Gehe alle Objekte des Formulars durch und vergleiche ihre Position mit der
                // neuen Position
                Component[] komponenten = this.getParent().getComponents();
                int i = 0;
                while (komponenten != null && i < komponenten.length && ret == null) {
                    // Wenn das Objekt nicht das zu kontrollierende Objekt ist und das Objekt
                    // mit dem zu Kontrollierendem zusammenfÃ¤llt
                    if (komponenten[i] != this && neuePosition.intersects(komponenten[i].getBounds()) && !(komponenten[i] instanceof Pacman))
                        ret = komponenten[i];
                    i++;
                }
            }
        }
        return ret;
    }
}