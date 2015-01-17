package graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * GraphInfo class that draws the graph info overlays on the main graph view.
 *
 * @author Kevin Zhou
 * @since 12/30/13
 */

public class GraphInfo extends JPanel {

    public static final int STATE_AVAILABLE = 0; // Graph state available
    public static final int STATE_BUSY = 1; // Graph state busy
    public static final int STATE_DISPLAY_EXTREMUM = 2; // Graph state show extremum calculation result
    public static final int STATE_DISPLAY_ZERO = 3; // Graph state show zero calculation result
    public static final int STATE_EXTREMUM_FAIL = 4; // Graph state show zero extremum calculation fail note
    public static final int STATE_ZERO_FAIL = 5; // Graph state show zero calculate fail note

    private GraphModel model; // Model linked to the view

    /**
     * Main constructor.
     *
     * @param model model linked to the view
     */
    public GraphInfo(GraphModel model) {
        this.model = model;
    }

    /**
     * Updates the view.
     */
    public void update() {
        this.repaint();
    }

    /**
     * Draws the position indicator on the view.
     *
     * @param g2 graphics object
     */
    private void drawIndicator(Graphics2D g2) {
        // Enable sub-pixel rendering
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        // Draw indicator
        g2.setColor(Color.RED);
        g2.fill(new Ellipse2D.Double(model.getRelativeX(model.getGraphHighlightX()) - 4,
                model.getRelativeY(model.getGraphHighlightY()) - 4, 8,
                8));

        // Draw indicator outline
        g2.setColor(new Color(100, 0, 0)); // Dark red
        g2.draw(new Ellipse2D.Double(model.getRelativeX(model.getGraphHighlightX()) - 4,
                model.getRelativeY(model.getGraphHighlightY()) - 4, 8,
                8));
    }

    /**
     * Draws the info overlays.
     *
     * @param g graph info
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g; // 2D graphics object
        super.paintComponent(g);

        // Enable anti-aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (model.getState() != STATE_AVAILABLE) {
            Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 14); // Font for overlay text
            g2.setFont(font);

            FontMetrics fm = getFontMetrics(font);
            int height = fm.getHeight(); // Height of component

            int messageX = 15; // Message position x
            int messageY = height + 11; // Message position y
            String text = ""; // Message text

            switch (model.getState()) {
                case STATE_BUSY:
                    text = "Working...";
                    break;
                case STATE_DISPLAY_EXTREMUM:
                    text = "Extremum: (" + model.getGraphHighlightX() + ", " + model.getGraphHighlightY() + ")";
                    drawIndicator(g2);
                    break;
                case STATE_DISPLAY_ZERO:
                    text = "Zero at X = " + model.getGraphHighlightX();
                    drawIndicator(g2);
                    break;
                case STATE_EXTREMUM_FAIL:
                    text = "Unable to find a local extremum";
                    break;
                case STATE_ZERO_FAIL:
                    text = "Unable to find a zero";
                    break;
            }

            g2.setColor(new Color(255, 233, 152));
            g2.drawRect(9, 9, fm.stringWidth(text) + 11, height + 11);

            g2.setColor(new Color (255, 255, 186));
            g2.fillRect(10, 10, fm.stringWidth(text) + 10, height + 10);

            g2.setColor(Color.BLACK);
            g2.drawString(text, messageX, messageY);
        }
    }

}
