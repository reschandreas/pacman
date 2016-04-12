import java.awt.*;

/**
 * Created by Andreas on 03.04.16.
 */
public class Pacman extends Ghost {

    private int step = 1;

    /*
    0 = right
    1 = up
    2 = left
    3 = down
     */
    private int tack = 2;
    private int nexttack = tack;

    public Pacman(String path) {
        super(path);
    }

    public boolean move() {
        switch (tack) {
            //RIGHT
            case 0:
                if (!(getObjektBei(getX() + step, getY()) instanceof Wall)) {
                    if (this.getX() + this.getSize().getWidth() < this.getParent().getWidth()) {
                        setBounds(this.getX() + step, this.getY(), this.getBounds().width, this.getBounds().height);
                        return false;
                    } else
                        setBounds(-step, this.getY(), this.getWidth(), this.getWidth());
                    return false;
                }
                return true;
            //UP
            case 1:
                if (!(getObjektBei(getX(), getY() - step) instanceof Wall)) {
                    if (0 < this.getY()) {
                        setBounds(this.getX(), this.getY() - step, this.getBounds().width, this.getBounds().height);
                        return false;
                    }
                }
                return true;
            //LEFT
            case 2:
                if (!(getObjektBei(getX() - step, getY()) instanceof Wall)) {
                    if (0 < this.getX()) {
                        setBounds(this.getX() - step, this.getY(), this.getBounds().width, this.getBounds().height);
                        return false;
                    } else
                        setBounds(this.getParent().getWidth() - step, this.getY(), this.getWidth(), this.getWidth());
                    return false;
                }
                return true;
            //DOWN
            case 3:
                if (!(getObjektBei(getX(), getY() + step) instanceof Wall)) {
                    if (this.getY() + this.getSize().getHeight() < this.getParent().getHeight()) {
                        setBounds(this.getX(), this.getY() + step, this.getBounds().width, this.getBounds().height);
                        return false;
                    }
                }
                return true;
        }
        return false;
    }

    public int getNexttack() {
        return nexttack;
    }

    public void setNexttack(int nexttack) {
        this.nexttack = nexttack;
    }

    public int getTack() {
        return tack;
    }

    public void setTack(int tack) {
        this.tack = tack;
    }

    /*
    public boolean moveRight() {
        if (!(getObjektBei(getX() + step, getY()) instanceof Wall)) {
            if (this.getX() + this.getSize().getWidth() < this.getParent().getWidth()) {
                setBounds(this.getX() + step, this.getY(), this.getBounds().width, this.getBounds().height);
                return false;
            } else
                setBounds(-step, this.getY(), this.getWidth(), this.getWidth());
            return false;
        }
        return true;
        if (!(getObjektBei(getX() + step, getY()) instanceof Wall)) {
            if (this.getX() + this.getSize().getWidth() < this.getParent().getWidth()) {
                setBounds(this.getX() + step, this.getY(), this.getBounds().width, this.getBounds().height);
                return false;
            } else
                setBounds(-step, this.getY(), this.getWidth(), this.getWidth());
            return false;
        }
        return true;
    }

    public boolean moveLeft() {
        if (!(getObjektBei(getX() - step, getY()) instanceof Wall)) {
            if (0 < this.getX()) {
                setBounds(this.getX() - step, this.getY(), this.getBounds().width, this.getBounds().height);
                return false;
            } else
                setBounds(this.getParent().getWidth() - step, this.getY(), this.getWidth(), this.getWidth());
            return false;
        }
        return true;
    }

    public boolean moveUp() {
        if (!(getObjektBei(getX(), getY() - step) instanceof Wall)) {
            if (0 < this.getY()) {
                setBounds(this.getX(), this.getY() - step, this.getBounds().width, this.getBounds().height);
                return false;
            }
        }
        return true;
    }

    public boolean moveDown() {
        if (!(getObjektBei(getX(), getY() + step) instanceof Wall)) {
            if (this.getY() + this.getSize().getHeight() < this.getParent().getHeight()) {
                setBounds(this.getX(), this.getY() + step, this.getBounds().width, this.getBounds().height);
                return false;
            }
        }
        return true;
    }
*/

    /**
     * Kontrolliert ob das Objekt - verschoben zur übergebenen x- und y-Position - mit
     * einem anderen Objekt kollidiert. Ist das der Fall, so wird das andere kollidierende
     * Objekt zurück geliefert.<br>
     * Es wird ebenfalls der contentPane des Formulars zurück geliefert, falls das Objekt
     * außerhalb des Formulars positioniert werden sollte, d. h. über den Rand des
     * Formulars hinaus ragen würde
     *
     * @param x die zu kontrollierende x-Koordinate
     * @param y die zu kontrollierende y-Koordinate
     * @return das Objekt das mit dem Objekt kollidiert oder - falls das Objekt an der
     * übergebenen Position außerhalb des Frames positioniert wird - wird der contentPane
     * zurück geliefert. Liefert null zurück, falls das Objekt ohne Überdeckung an der
     * übergebenen Position positioniert werden kann
     */
    public Component getObjektBei(int x, int y) {
        Component ret = null;
        if (this.getParent() != null) {
            // Kontrolliere ob neue Position außerhalb des Frames liegt
            if (x < 0 || y < 0 || x + this.getWidth() > this.getParent().getWidth() ||
                    y + this.getHeight() > this.getParent().getHeight())
                // In diesem Fall wird der contentPane des Formulars übergeben
                ret = this.getParent();
            else {
                // Kontrolliere ob sich die neue Position mit anderen Objekten überdeckt
                Rectangle neuePosition =
                        new Rectangle(x, y, this.getWidth(), this.getHeight());
                // Gehe alle Objekte des Formulars durch und vergleiche ihre Position mit der
                // neuen Position
                Component[] komponenten = this.getParent().getComponents();
                int i = 0;
                while (komponenten != null && i < komponenten.length && ret == null) {
                    // Wenn das Objekt nicht das zu kontrollierende Objekt ist und das Objekt
                    // mit dem zu Kontrollierendem zusammenfällt
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
