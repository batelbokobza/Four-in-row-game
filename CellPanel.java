package question1;

import java.awt.*;
import javax.swing.JPanel;

/*The Class produces a panel that has a disc inside.
 * This class can be used to change the color of the disc with function getColor.*/
public class CellPanel extends JPanel {

    private Color discColor = new Color(0xACACA5);
    private final int PANEL_DIMENSION = 75;

    /*Class constructor.
     * Initializes the panel size, and inserts a disk into the panel.*/
    public CellPanel() {
        this.setPreferredSize(new Dimension(PANEL_DIMENSION, PANEL_DIMENSION));
        this.setColor(discColor);
    }

    public void setColor(Color color) {
        this.discColor = color;
        this.setForeground(color);
    }

    /*The function creates the circle found in each slot.*/
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(25, 63, 206));

        int baseLine = this.getBaseline(this.getWidth(), this.getHeight());
        g.fillOval(baseLine , baseLine, this.getWidth(), this.getHeight());
    }
}