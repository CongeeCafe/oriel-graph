package graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

/**
 * GraphArea class that draws the graph on the screen.
 *
 * @author Kevin Zhou
 * @since 12/13/13
 */

public class GraphArea extends JPanel {

    private GraphModel model; // Model linked to the view

    int width; // Width of component
    int height; // Height of component

    BufferedImage graph; // Cache of graph image

    /**
     * Main Constructor.
     *
     * @param model model linked to the view
     */
    public GraphArea(GraphModel model) {
        super();
        this.model = model;
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(300, 300));
    }

    /**
     * Draws the x-axis increments for normal increment unit.
     *
     * @param i increment value
     * @param g2 graphics object
     */
    private void drawDegreesAxisIncrementX(double i, Graphics2D g2) {
        g2.draw(new Line2D.Double(model.getRelativeX(i), model.getRelativeY(0) - 4, model.getRelativeX(i),
                model.getRelativeY(0) + 4));

        if (i != 0) {
            FontMetrics fm = getFontMetrics(getFont());
            double height = fm.getHeight();
            double width = fm.stringWidth(String.format("%." + (model.getPrecisionX()) + "f", i));

            g2.drawString(String.format("%."+ (model.getPrecisionX()) + "f", i), (int) Math.round(model.getRelativeX(i) -
                    width /
                    2),
                    (int) Math.round(model.getRelativeY(0) + height + 3));
        }
    }

    /**
     * Draws the y-axis increments for normal increment unit.
     *
     * @param i increment value
     * @param g2 graphics object
     */
    private void drawDegreesAxisIncrementY(double i, Graphics2D g2) {
        g2.draw(new Line2D.Double(model.getRelativeX(0) - 4, model.getRelativeY(i), model.getRelativeX(0) + 4,
                model.getRelativeY(i)));

        if (i != 0) {
            FontMetrics fm = getFontMetrics(getFont());
            double height = fm.getHeight();
            double width = fm.stringWidth(String.format("%." + (model.getPrecisionY()) + "f", i));

            g2.drawString(String.format("%." + (model.getPrecisionY()) + "f", i), (int) Math.round(model.getRelativeX(0) -
                    width - 7), (int) Math.round(model.getRelativeY(i) + height / 3));
        }
    }

    /**
     * Draws the x-axis increments for radians increment unit.
     *
     * @param numer increment numerator value
     * @param denomin increment denominator value
     * @param g2 graphics object
     */
    private void drawRadiansAxisIncrementX(int numer, int denomin, Graphics2D g2) {
        g2.draw(new Line2D.Double(model.getRelativeX((double)numer * Math.PI/(double)denomin), model.getRelativeY(0) - 4,
                model.getRelativeX((double)numer * Math.PI/(double)denomin),
                model.getRelativeY(0) + 4));

        if ((double)numer * Math.PI/(double)denomin != 0) {
            FontMetrics fm = getFontMetrics(getFont());
            double height = fm.getHeight();
            double width = fm.stringWidth(numer + "\u03c0/" + denomin);

            g2.drawString(numer + "\u03c0/" + denomin, (int) Math.round(model.getRelativeX((double)numer * Math.PI/(double)denomin) - width / 2),
                    (int) Math.round(model.getRelativeY(0) + height + 3));
        }
    }

    /**
     * Draws the y-axis increments for radians increment unit.
     *
     * @param numer increment numerator value
     * @param denomin increment denominator value
     * @param g2 graphics object
     */
    private void drawRadiansAxisIncrementY(int numer, int denomin, Graphics2D g2) {
        g2.draw(new Line2D.Double(model.getRelativeX(0) - 4, model.getRelativeY((double)numer * Math.PI/(double)denomin), model.getRelativeX(0) + 4,
                model.getRelativeY((double)numer * Math.PI/(double)denomin)));

        if ((double)numer * Math.PI/(double)denomin != 0) {
            FontMetrics fm = getFontMetrics(getFont());

            double height = fm.getHeight();
            double width = fm.stringWidth(numer + "\u03c0/" + denomin);

            g2.drawString(numer + "\u03c0/" + denomin, (int) (Math.round(model.getRelativeX(0) - width - 7)),
                    (int) (Math.round(model.getRelativeY((double) numer * Math.PI / (double) denomin)) + height / 3));
        }
    }

    /**
     * Draws the axis on graph.
     *
     * @param g2 graphics object
     */
    private void drawAxis(Graphics2D g2) {
        // Draws axis
        double axisXx = model.getRelativeX(0); // x position of x-axis
        double axisXy = model.getRelativeY(0); // y position of y-axis

        g2.draw(new Line2D.Double(0, axisXy, width, axisXy));
        g2.draw(new Line2D.Double(axisXx, 0, axisXx, height));

        // Draws x-axis scale
        if (model.getUnitsX() == GraphModel.UNIT_DEGREES) {
            for (double i = (model.getAreaBounds().getMinX() <= 0) ? 0 : model.getAreaBounds().getMinX(); i < model
                    .getAreaBounds().getMaxX(); i += model.getAxisScaleX()) {

                drawDegreesAxisIncrementX(i, g2);
            }

            for (double i = (model.getAreaBounds().getMaxX() >= 0) ? 0 : model.getAreaBounds().getMaxX(); i > model
                    .getAreaBounds().getMinX(); i -= model.getAxisScaleX()) {

                drawDegreesAxisIncrementX(i, g2);
            }

        } else if (model.getUnitsX() == GraphModel.UNIT_RADIANS) {
            int fracNumer; // Numerator value
            int fracDenomin; // Denominator value

            int currentFracNumer; // Current numerator value
            int currentFracDenomin; // Current denominator value

            int frac[]; // Fraction array

            fracNumer = model.getAxisScaleXNumer();
            fracDenomin = model.getAxisScaleXDenomin();

            currentFracNumer = fracNumer;
            currentFracDenomin = fracDenomin;

            for (double i = (model.getAreaBounds().getMinX() <= 0) ? 0 : model.getAreaBounds().getMinX(); i < model
                    .getAreaBounds().getMaxX(); i += (double) model.getAxisScaleXNumer() * Math.PI / (double)model
                    .getAxisScaleXDenomin()) {

                drawRadiansAxisIncrementX(currentFracNumer, currentFracDenomin, g2);

                currentFracNumer = (currentFracNumer * fracDenomin) + (currentFracDenomin * fracNumer);
                currentFracDenomin *= fracDenomin;

                frac = model.getMathEngine().asFraction(currentFracNumer, currentFracDenomin);

                currentFracNumer = frac[0];
                currentFracDenomin = frac[1];
            }

            currentFracNumer = -fracNumer;
            currentFracDenomin = fracDenomin;

            for (double i = (model.getAreaBounds().getMaxX() >= 0) ? 0 : model.getAreaBounds().getMaxX(); i > model
                    .getAreaBounds().getMinX(); i -= (double)model.getAxisScaleXNumer() * Math.PI / (double)model
                    .getAxisScaleXDenomin()) {

                drawRadiansAxisIncrementX(currentFracNumer, currentFracDenomin, g2);

                currentFracNumer = (currentFracNumer * fracDenomin) - (currentFracDenomin * fracNumer);
                currentFracDenomin *= fracDenomin;

                frac = model.getMathEngine().asFraction(currentFracNumer, currentFracDenomin);

                currentFracNumer = frac[0];
                currentFracDenomin = frac[1];
            }
        }

        //Draws y-axis scale

        if (model.getUnitsY() == GraphModel.UNIT_DEGREES) {
            for (double i = (model.getAreaBounds().getMinY() <= 0) ? 0 : model.getAreaBounds().getMinY(); i < model.getAreaBounds().getMaxY(); i +=
                    model.getAxisScaleY()) {
                drawDegreesAxisIncrementY(i, g2);
            }

            for (double i = (model.getAreaBounds().getMaxY() >= 0) ? 0 : model.getAreaBounds().getMaxY(); i > model.getAreaBounds().getMinY(); i -=
                    model.getAxisScaleY()) {
                drawDegreesAxisIncrementY(i, g2);
            }
        } else if (model.getUnitsY() == GraphModel.UNIT_RADIANS) {
            int fracNumer; // Numerator value
            int fracDenomin; // Denominator value

            int currentFracNumer; // Current numerator value
            int currentFracDenomin; // Current denominator value

            int frac[]; // Fraction array

            fracNumer = model.getAxisScaleYNumer();
            fracDenomin = model.getAxisScaleYDenomin();

            currentFracNumer = fracNumer;
            currentFracDenomin = fracDenomin;

            for (double i = (model.getAreaBounds().getMinY() <= 0) ? 0 : model.getAreaBounds().getMinY(); i < model
                    .getAreaBounds().getMaxY(); i += (double)model.getAxisScaleYNumer() * Math.PI / (double)model
                    .getAxisScaleYDenomin()) {

                drawRadiansAxisIncrementY(currentFracNumer, currentFracDenomin, g2);

                currentFracNumer = (currentFracNumer * fracDenomin) + (currentFracDenomin * fracNumer);
                currentFracDenomin *= fracDenomin;

                frac = model.getMathEngine().asFraction(currentFracNumer, currentFracDenomin);

                currentFracNumer = frac[0];
                currentFracDenomin = frac[1];
            }

            currentFracNumer = -fracNumer;
            currentFracDenomin = fracDenomin;

            for (double i = (model.getAreaBounds().getMaxY() >= 0) ? 0 : model.getAreaBounds().getMaxY(); i > model
                    .getAreaBounds().getMinY(); i -= (double)model.getAxisScaleXNumer() * Math.PI / (double)model
                    .getAxisScaleYDenomin()) {

                drawRadiansAxisIncrementY(currentFracNumer, currentFracDenomin, g2);

                currentFracNumer = (currentFracNumer * fracDenomin) - (currentFracDenomin * fracNumer);
                currentFracDenomin *= fracDenomin;

                frac = model.getMathEngine().asFraction(currentFracNumer, currentFracDenomin);

                currentFracNumer = frac[0];
                currentFracDenomin = frac[1];
            }
        }

    }

    /**
     * Invalidate the graph to prepare for a recalculation.
     */
    public void invalidate() {
        graph = null;
    }

    /**
     * Invalidates and updates the graph.
     */
    public void update() {
        invalidate();
        this.repaint();
    }

    /**
     * Draws the graph on screen.
     *
     * @param g graphics object
     */
    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);

        // If graph is invalid
        if (graph == null) {
            // Component dimensions
            this.width = this.getWidth();
            this.height = this.getHeight();

            graph = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = graph.createGraphics();

            // Enable anti-aliasing
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Enable sub-pixel rendering
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, width, height);

            boolean draw = true;

            Path2D.Double path = new Path2D.Double();

            g2.setColor(model.getLineColor());
            g2.setStroke(new BasicStroke(model.getLineThickness()));

            if (model.getLineStyle() == GraphModel.LINE_DASHED) {
                Stroke dashed = new BasicStroke(model.getLineThickness(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
                        new float[] {6,4}, 0);
                g2.setStroke(dashed);
            }

            double[][] windowPoints = model.getWindowPoints(); // Points of graph

            if (windowPoints != null) {
                for (int i = 0; i < windowPoints.length; i++) {
                    if (i == 0) {
                        path.moveTo(windowPoints[i][0], windowPoints[i][1]);
                    }

                    if (draw) {
                        path.lineTo(windowPoints[i][0], windowPoints[i][1]);

                        if (windowPoints[i][1] < 0 || windowPoints[i][1] > height) {
                            draw = false;
                        }
                    } else if (windowPoints[i][1] < height && windowPoints[i][1] > 0) {
                        draw = true;
                        path.moveTo(windowPoints[i-1][0], windowPoints[i-1][1]);
                        path.lineTo(windowPoints[i][0], windowPoints[i][1]);
                    }
                }
            }

            // Draw graph
            g2.draw(path);

            // Draw axis
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1));
            drawAxis(g2);

            model.setGraphImage(graph);

        }

        g2d.drawImage(graph, null, 0, 0);
    }
}
