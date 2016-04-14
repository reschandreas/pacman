import javax.swing.*;
import java.awt.*;

/**
 * Created by Andreas on 14.04.16.
 */
public class Tile extends JComponent {

    protected Dot dot;

    public Tile(int x, int y, Dot dot) {
        setBounds(x, y, PacmanGUI.RESOLUTION, PacmanGUI.RESOLUTION);
        this.dot = dot;
    }

    public void paint(Graphics g) {
        g.setColor(Color.green);
        g.drawRect(0, 0, this.getWidth(), this.getHeight());
        dot.repaint();
    }

}
