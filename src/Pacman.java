import java.awt.*;

/**
 * Created by Andreas on 03.04.16.
 */
public class Pacman extends Ghost {

    private int step = 1;


    public Pacman(String path) {
        super(path);
    }

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