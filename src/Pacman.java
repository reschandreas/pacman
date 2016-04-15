import java.awt.*;

/**
 * Created by Andreas on 03.04.16.
 */
public class Pacman extends Ghost {

    private int x_speed = 0;
    private int y_speed = 0;

    private int x_next = 0;
    private int y_next = 0;

    public Pacman(String path) {
        super(path);
        x_speed = -1;
        x_next = -1;
    }

    public int getX_next() {
        return x_next;
    }

    public void setX_next(int x_next) {
        this.x_next = x_next;
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
        x_next = 0;
    }

    public int getY_speed() {
        return y_speed;
    }

    public void setY_speed(int y_speed) {
        this.y_speed = y_speed;
    }

    private void reset() {
        x_speed = x_next;
        y_speed = y_next;
    }

    private void moveHorizontal() {
        if (x_speed == -1 && getX() < 0) {
            setBounds(getParent().getWidth(), getY(), getWidth(), getHeight());
        } else if (x_speed == 1 && getX() + getWidth() + x_speed > getParent().getWidth()) {
            setBounds(x_speed, getY(), getWidth(), getHeight());
        } else if (!(getObjektBei(getX() + x_speed, getY()) instanceof Wall)) {
            setBounds(this.getX() + x_speed, this.getY(), this.getBounds().width, this.getBounds().height);
        }
    }

    private void moveVertical() {
        if (!(getObjektBei(getX(), getY() + y_speed) instanceof Wall)) {
            setBounds(this.getX(), this.getY() + y_speed, this.getBounds().width, this.getBounds().height);
        }
    }

    public void move() {
        Intersection intersection = PacmanGUI.intersectionCheck();
        if (intersection == null) {
            if (x_speed == 1 || x_speed == -1) {
                moveHorizontal();
            } else if (y_speed == 1 || y_speed == -1) {
                moveVertical();
            }
        } else {
            reset();
            if (x_speed == 1 || x_speed == -1) {
                moveHorizontal();
            } else if (y_speed == 1 || y_speed == -1) {
                moveVertical();
            }
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
                    // mit dem zu Kontrollierendem zusammenfÃ¤llt
                    if (komponenten[i] != this &&
                            neuePosition.intersects(komponenten[i].getBounds()) && !(komponenten[i] instanceof Ghost))
                        ret = komponenten[i];
                    i++;
                }
            }
        }
        return ret;
    }
}