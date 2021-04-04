package io.resch.pacman.board;

import io.resch.pacman.movable.Pacman;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class BoardItem extends JComponent {

    public enum Type {
        WALL_NORMAL("images/wall.png"),
        WALL_RIGHT("images/wall_right.png"),
        WALL_UP("images/wall_up.png"),
        DOT("images/dot.png"),
        ENERGIZER("images/energizer.png"),
        PACMAN("images/pacman_right.png"),
        BLINKY("images/blinky.png"),
        INKY("images/inky.png"),
        PINKY("images/pinky.png"),
        CLYDE("images/clyde.png"),
        FRIGHTENED("images/ghost_frightened.png");

        public final String label;

        Type(String label) {
            this.label = label;
        }
    }

    protected Image image = null;

    public BoardItem(Type type) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(type.label);

        if (input == null) {
            System.out.println("file not found --- " + type.label);
        } else {
            try {
                this.image = ImageIO.read(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Stelle Größe des Objektes auf Größe des Bildes ein
            this.setSize(this.image.getWidth(this), this.image.getHeight(this));
        }
    }


    protected Image loadImage(String path) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(path);
        if (input == null) {
            return null;
        }
        return ImageIO.read(input);
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
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
