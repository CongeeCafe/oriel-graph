package graph;

import javax.swing.*;
import java.awt.*;

/**
 * GraphSelection class that draws the selection hint on the cursor location.
 *
 * @author Kevin Zhou
 * @since 12/30/13
 */

public class GraphSelection extends JPanel {

    private boolean showSelection; // Whether or not to show the tool
    private int selectionX; // X value of cursor

    private final float DEFAULT_SELECTION_X = 0.1f; // Where to display the tool at first at % of area

    /**
     * Main constructor.
     */
    public GraphSelection() {
        this.showSelection = false;
        this.selectionX = 0;
    }

    /**
     * Updates the cursor X value.
     *
     * @param x cursor x value
     */
    public void setSelection(int x) {
        this.selectionX = x;
        this.repaint();
    }

    /**
     * Sets whether or not to show the tool.
     *
     * @param visibility whether or not to show the tool
     */
    public void setSelectionVisibility(boolean visibility) {
        showSelection = visibility;
        this.selectionX = (int) (DEFAULT_SELECTION_X * this.getWidth());
        this.repaint();
    }

    /**
     * Draws the selection tool at cursor location on screen.
     *
     * @param g graphics object
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (showSelection) {
            g.setColor(Color.RED);
            g.drawLine(selectionX, 0, selectionX, this.getHeight());
        }
    }

}
