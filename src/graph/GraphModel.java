package graph;

import expressionEvaluator.InputValidator;
import expressionEvaluator.MathEngine;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * GraphModel class that holds the methods and values used in the program.
 *
 * @author Kevin Zhou
 * @since 12/12/13
 */

public class GraphModel {

    private String function = "x"; // Function to plot / Default function

    private String functionRestore; // Function to restore if user enters invalid function
    private Color lineColor = Color.BLUE; // Graph line color
    private int lineThickness = 2; // Graph line thickness
    private int lineStyle = LINE_SOLID; // Graph line style

    public static final int LINE_SOLID = 0;
    public static final int LINE_DASHED = 1;

    private BufferedImage graphImage; // Image cache of graph

    private MathEngine mEngine; // Math engine

    private boolean useJSEval; // Whether or not to use Javascript engine to evaluate function

    boolean tableValuesVisible; // Whether the table of values is visible

    private GraphView view; // View of the graph

    public static final int UNIT_DEGREES = 0; // Degrees unit
    public static final int UNIT_RADIANS = 1; // Radians unit

    private double[][] windowPoints; // Pixel locations to plot graph

    private int unitsX; // Units of X-axis
    private int unitsY; // Units of Y-axis

    private double axisScaleX; // Scale of X-axis
    private double axisScaleY; // Scale of Y-axis

    private int axisScaleXNumer; // Numerator of scale of X-axis
    private int axisScaleXDenomin; // Denominator of scale of X-axis

    private int[][] axisXRadiansIncrements; // X-axis increments in radians

    private int axisScaleYNumer; // Numerator of scale of y-axis
    private int axisScaleYDenomin; // Denominator of scale of y-axis

    private int precisionX; // Precision of X-axis scale
    private int precisionY; // Precision of Y-axis scale

    private int state; // State of the graph

    private double graphHighlightX; // X value of highlight point
    private double graphHighlightY; // Y value of highlight point

    private boolean toolSelected; // Whether or not a tools is selected
    private boolean extremaToolSelected; // Whether or not the find extreme tool is selected
    private boolean zerosToolSelected; // Whether or not the find zero tool is selected

    private int width; // Width of the graph window
    private int height; // Height of the graph window

    private Bounds areaBounds; // graph.Bounds of graph

    private double scaleX; // Number of pixels per 1 unit along X-axis
    private double scaleY; // Number of pixels per 1 unit along Y-axis

    /**
     * Main constructor.
     */
    public GraphModel() {
        mEngine = new MathEngine(this);
        mEngine.setFunction(function);

        useJSEval = false;

        tableValuesVisible = true;

        areaBounds = new Bounds(-10, 10, -10, 10);

        axisScaleX = 1;
        axisScaleY = 1;

        unitsX = UNIT_DEGREES;
        unitsY = UNIT_DEGREES;

        precisionX = 0;
        precisionY = 0;

        toolSelected = false;
        extremaToolSelected = false;
        zerosToolSelected = false;
    }

    /**
     * Attaches view to model.
     *
     * @param view view to attach to model
     */
    public void setGUI(GraphView view) {
        this.view = view;
    }

    /**
     * Sets the function to be graphed.
     *
     * @param function function to be graphed
     */
    public void setFunction(String function) {
        this.function = function;
        mEngine.setFunction(function);

        this.setState(GraphInfo.STATE_AVAILABLE); // Resets messages from previous graph

        createWindowPoints();
        updateAllViews();
    }

    /**
     * Sets the graph line color.
     *
     * @param lineColor graph line color
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
        updateAllViews();
    }

    /**
     * Sets the graph line thickness.
     *
     * @param lineThickness graph line thickness
     */
    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
        updateAllViews();
    }

    /**
     * Sets the graph line style.
     *
     * @param lineStyle graph line style
     */
    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
        updateAllViews();
    }

    /**
     * Sets whether to use JS to evaluate function.
     *
     * @param useJSEval whether to use JS for function evaluation
     */
    public void setUseJSEval(boolean useJSEval) {
        this.useJSEval = useJSEval;
    }

    /**
     * Sets whether the table of values is visible.
     *
     * @param tableValuesVisible whether the table of values is visible
     */
    public void setTableValuesVisible(boolean tableValuesVisible) {
        this.tableValuesVisible = tableValuesVisible;
        this.updateTableVisibility();
    }

    /**
     * Returns whether the table of values is visible.
     *
     * @return whether the table of values is visible
     */
    public boolean isTableValuesVisible() {
        return tableValuesVisible;
    }

    /**
     * Sets unit to use along x-axis.
     *
     * @param units unit
     */
    public void setUnitsX(int units) {
        this.unitsX = units;
    }

    /**
     * Sets unit to use along y-axis.
     *
     * @param units unit
     */
    public void setUnitsY(int units) {
        this.unitsY = units;
    }

    /**
     * Gets precision of user entered scale value.
     *
     * @param scale user entered scale
     * @return precision
     */
    private int getPrecision(String scale) {
        if (!scale.contains(".")) {
            return 0;
        } else {
            // Returns 0 if no digits after decimal (e.g. 2.)
            try {
                return scale.split("\\.")[1].length();
            } catch (ArrayIndexOutOfBoundsException e) {
                return 0;
            }
        }
    }

    /**
     * Set the current cached graph image;
     *
     * @param graphImage cached graph image
     */
    public void setGraphImage(BufferedImage graphImage) {
        this.graphImage = graphImage;
    }

    /**
    public BufferedImage getGraphImage() {
        return graphImage;
    }

    /**
     * Sets the scale of the x and y axis.
     *
     * @param scaleX scale of x-axis
     * @param scaleY scale of y-axis
     */
    public void setAxisScale(String scaleX, String scaleY) {
        if (!InputValidator.validateScale(scaleX) || !InputValidator.validateScale(scaleY)) {
            Alert.showSettingsError(view, "Invalid scale input.");
            return;
        }

        // Update x-axis scale
        if (scaleX.contains("/")) {
            String[] frac = scaleX.split("/"); // Splits numerator and denominator of fraction

            axisScaleXNumer = Integer.valueOf(frac[0]);
            axisScaleXDenomin = Integer.valueOf(frac[1]);

            axisScaleX = Double.valueOf(frac[0]) / Double.valueOf(frac[1]);
            precisionX = 2;
        } else {
            axisScaleXNumer = Double.valueOf(scaleX).intValue();
            axisScaleXDenomin = 1;

            axisScaleX = Double.valueOf(scaleX);
            precisionX = getPrecision(scaleX);
        }

        // Update y-axis scale
        if (scaleY.contains("/")) {
            String[] frac = scaleY.split("/"); // Splits numerator and denominator of fraction

            axisScaleYNumer = Integer.valueOf(frac[0]);
            axisScaleYDenomin = Integer.valueOf(frac[1]);

            axisScaleY = Double.valueOf(frac[0]) / Double.valueOf(frac[1]);
            precisionY = 2;
        } else {
            axisScaleYNumer = Double.valueOf(scaleY).intValue();
            axisScaleYDenomin = 1;

            axisScaleY = Double.valueOf(scaleY);
            precisionY = getPrecision(scaleY);
        }

        updateAllViews();
    }

    /**
     * Updates graph window dimensions.
     *
     * @param width graph window width
     * @param height graph window height
     */
    public void updateSize(int width, int height) {
        this.width = width;
        this.height = height;

        recalculateGraphData();
    }

    /**
     * Sets the bounds for the graph area.
     * @param bounds bounds for graph area
     */
    public void setAreaBounds(Bounds bounds) {
        this.areaBounds = bounds;

        recalculateGraphData();
    }

    /**
     * Calculates the X and Y scale.
     */
    private void updateWindowScale() {
        this.scaleX = (double) width / (this.areaBounds.getMaxX() - this.areaBounds.getMinX());
        this.scaleY = (double) height / (this.areaBounds.getMaxY() - this.areaBounds.getMinY());
    }

    /**
     * Get state of program.
     *
     * @return state of program
     */
    public int getState() {
        return state;
    }

    /**
     * Gets Y value of graph highlight point.
     *
     * @return y value of graph highlight point
     */
    public double getGraphHighlightY() {
        return graphHighlightY;
    }

    /**
     * Gets X value of graph highlight point.
     *
     * @return x value of graph highlight point
     */
    public double getGraphHighlightX() {
        return graphHighlightX;
    }

    /**
     * Calculates the X value of graph from X value of point on screen.
     *
     * @param x x value of point on screen
     * @return x value of graph
     */
    public double getGraphRelativeX(double x) {
        return (areaBounds.getMinX() + (x / width) * (width/scaleX));
    }

    /**
     * Calculates the X value of screen from X value of graph.
     *
     * @param x x value of graph
     * @return x value of screen
     */
    public double getRelativeX(double x) {
        return (x - areaBounds.getMinX()) * scaleX;
    }

    /**
     * Calculates Y value of screen from Y value of graph.
     *
     * @param y y value of graph
     * @return y value of screen
     */
    public double getRelativeY(double y) {
        return (-y + areaBounds.getMaxY()) * scaleY;
    }

    /**
     * Creates points of graph to be plotted.
     */
    private void createWindowPoints() {
        int range = width; // Number of points to plot

        double[][] newWindowPoints;

        if (range > 0) {
            newWindowPoints = new double[range + 1][range + 1]; // Points to plot

            double graphPointX; // X value of point on graph
            double graphPointY; // Y value of point on graph

            double windowPointX; // X value of point on screen
            double windowPointY; // Y value of point on screen

            int point = 0; // Point number

            for (int i = 0; i <= range; i++) {
                graphPointX = getGraphRelativeX(i);

                try {
                    graphPointY = mEngine.evalExpression(graphPointX);
                } catch (NullPointerException e) {
                    Alert.showGraphError();
                    function = functionRestore;
                    mEngine.setFunction(functionRestore);
                    return;
                }

                windowPointX = getRelativeX(graphPointX);
                windowPointY = getRelativeY(graphPointY);

                newWindowPoints[point][0] = windowPointX;
                newWindowPoints[point][1] = windowPointY;

                point++;
            }

            windowPoints = newWindowPoints;
            functionRestore = function;
        }
    }

    /**
     * Creates x-axis scale increments in radians.
     */
    private void createAxisRadiansXIncrements() {
        int[][] negIncrements = createXAxisNegRadiansIncrements();
        int[][] posIncrements = createXAxisPosRadiansIncrements();

        int[][] result = new int[negIncrements.length + posIncrements.length][];

        System.arraycopy(negIncrements, 0, result, 0, negIncrements.length);
        System.arraycopy(posIncrements, 0, result, negIncrements.length, posIncrements.length);

        axisXRadiansIncrements = result;
    }

    /**
     * Creates x-axis scale increments in radians on positive side.
     */
    private int[][] createXAxisPosRadiansIncrements() {
        int currentFracNumer;
        int currentFracDenomin;

        int tmpFrac[];

        int numIncrements = (int) (areaBounds.getMaxX() - areaBounds.getMinX() / scaleX);
        int[][] increments = new int[numIncrements / 2][2];

        currentFracNumer = this.axisScaleXNumer;
        currentFracDenomin = this.axisScaleXDenomin;

        int count = 0;

        for (double i = (areaBounds.getMinX() <= 0) ? 0 : areaBounds.getMinX(); i < areaBounds.getMaxX();
             i += (double) axisScaleXNumer * Math.PI / (double) axisScaleXDenomin) {

            increments[count][0] = currentFracNumer;
            increments[count][1] = currentFracDenomin;

            currentFracNumer = (currentFracNumer * this.axisScaleXDenomin) + (currentFracDenomin * this.axisScaleXNumer);
            currentFracDenomin *= this.axisScaleXDenomin;

            tmpFrac = mEngine.asFraction(currentFracNumer, currentFracDenomin);

            currentFracNumer = tmpFrac[0];
            currentFracDenomin = tmpFrac[1];

            count++;
        }

        return increments;
    }

    /**
     * Creates x-axis scale increments in radians on negative side.
     */
    private int[][] createXAxisNegRadiansIncrements() {
        int currentFracNumer;
        int currentFracDenomin;

        int tmpFrac[];

        int numIncrements = (int) (areaBounds.getMaxX() - areaBounds.getMinX() / scaleX);
        int[][] increments = new int[numIncrements / 2][2];

        currentFracNumer = -this.axisScaleXNumer;
        currentFracDenomin = this.axisScaleXDenomin;

        int count = 0;

        for (double i = (areaBounds.getMaxX() >= 0) ? 0 : areaBounds.getMaxX(); i > areaBounds.getMinX();
             i -= (double) this.axisScaleXNumer * Math.PI / (double) this.axisScaleXDenomin) {

            increments[count][0] = currentFracNumer;
            increments[count][1] = currentFracDenomin;

            currentFracNumer = (currentFracNumer * this.axisScaleXDenomin) - (currentFracDenomin * this.axisScaleXNumer);
            currentFracDenomin *= this.axisScaleXDenomin;

            tmpFrac = mEngine.asFraction(currentFracNumer, currentFracDenomin);

            currentFracNumer = tmpFrac[0];
            currentFracDenomin = tmpFrac[1];
        }

        return increments;
    }

    /**
     * Calculate function extremum closest to use guess.
     *
     * @param mouseX x value of user guess
     * @return location of extremum
     */
    public float[] getExtremum(int mouseX) {
        return mEngine.calcExtreme(getGraphRelativeX(mouseX));
    }

    /**
     * Calculate function zero closest to guess.
     *
     * @param mouseX x value of user guess
     * @return location of zero
     */
    public float[] getZero(int mouseX) {
        float[] result = new float[2];
        result[0] = mEngine.calcZero(getGraphRelativeX(mouseX))[0];
        result[1] = (float) mEngine.evalExpression(result[0]);

        return result;
    }

    /**
     * Set the state of the program.
     *
     * @param state state of program
     */
    public void setState(int state) {
        this.state = state;
        updateInfoView();
    }

    /**
     * Sets the graph highlight point.
     *
     * @param x x value of highlight point
     * @param y y value of highlight point
     */
    public void setGraphHighlightPoint(double x, double y) {
        graphHighlightX = x;
        graphHighlightY = y;
        updateInfoView();
    }

    /**
     * Clear overlays present on the graph.
     */
    public void clearOverlays() {
        setState(GraphInfo.STATE_AVAILABLE);
    }

    /**
     * Toggles the tool to find extrema.
     */
    public void toggleExtremaTool() {
        if (extremaToolSelected) {
            setExtremaTool(false);
        } else if (toolSelected) {
            deselectAllTools();
            setExtremaTool(true);
        } else {
            setExtremaTool(true);
        }
        updateToolsSelection();
    }

    /**
     * Toggles the tool to find zeros.
     */
    public void toggleZerosTool() {
        if (zerosToolSelected) {
            setZerosTool(false);
        } else if (toolSelected) {
            deselectAllTools();
            setZerosTool(true);
        } else {
            setZerosTool(true);
        }
        updateToolsSelection();
    }

    /**
     * Enable or disable the extrema tool.
     *
     * @param set tool selection
     */
    private void setExtremaTool(boolean set) {
        if (set) {
            extremaToolSelected = true;
            toolSelected = true;
        } else {
            extremaToolSelected = false;
            toolSelected = false;
        }
    }

    /**
     * Enable or disable the zeros tool.
     *
     * @param set tool selection
     */
    private void setZerosTool(boolean set) {
        if (set) {
            zerosToolSelected = true;
            toolSelected = true;
        } else {
            zerosToolSelected = false;
            toolSelected = false;
        }
    }

    /**
     * Deselects all tools.
     */
    public void deselectAllTools() {
        extremaToolSelected = false;
        zerosToolSelected = false;
        toolSelected = false;
    }

    /**
     * Returns user function.
     *
     * @return function
     */
    public String getFunction() {
        return function;
    }

    /**
     * Returns the graph line color.
     *
     * @return graph line color
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Returns the graph line thickness.
     *
     * @return graph line thickness
     */
    public int getLineThickness() {
        return lineThickness;
    }

    /**
     * Returns the graph line style.
     *
     * @return graph line style
     */
    public int getLineStyle() {
        return lineStyle;
    }

    /**
     * Returns graph bounds.
     *
     * @return graph bounds
     */
    public Bounds getAreaBounds() {
        return areaBounds;
    }

    /**
     * Returns x-axis scale.
     *
     * @return x-axis scale
     */
    public double getAxisScaleX() {
        return axisScaleX;
    }

    /**
     * Returns y-axis scale.
     *
     * @return y-axis scale
     */
    public double getAxisScaleY() {
        return axisScaleY;
    }

    /**
     * Returns numerator of x-axis scale.
     *
     * @return numerator of x-axis scale
     */
    public int getAxisScaleXNumer() {
        return axisScaleXNumer;
    }

    /**
     * Returns denominator of x-axis scale.
     *
     * @return denominator of x-axis scale
     */
    public int getAxisScaleXDenomin() {
        return axisScaleXDenomin;
    }

    /**
     * Returns numerator of y-axis scale.
     *
     * @return numerator of y-axis scale
     */
    public int getAxisScaleYNumer() {
        return axisScaleYNumer;
    }

    /**
     * Returns denominator of y-axis scale.
     *
     * @return denominator of y-axis scale
     */
    public int getAxisScaleYDenomin() {
        return axisScaleYDenomin;
    }

    /**
     * Returns precision of x-axis scale.
     *
     * @return precision of x-axis scale
     */
    public int getPrecisionX() {
        return precisionX;
    }

    /**
     * Returns precision of y-axis scale.
     *
     * @return precision of y-axis scale
     */
    public int getPrecisionY() {
        return precisionY;
    }

    /**
     * Returns units of y-axis.
     *
     * @return units of y-axis
     */
    public int getUnitsY() {
        return unitsY;
    }

    /**
     * Returns units of x-axis.
     *
     * @return units of x-axis
     */
    public int getUnitsX() {
        return unitsX;
    }

    /**
     * Returns function points to plot on window.
     *
     * @return function points to plot on window
     */
    public double[][] getWindowPoints() {
        return windowPoints;
    }

    /**
     * Returns whether the zero tool is selected.
     *
     * @return whether the zero tool is selected
     */
    public boolean isZerosToolSelected() {
        return zerosToolSelected;
    }

    /**
     * Returns whether any tools are selected.
     *
     * @return whether any tools are selected
     */
    public boolean isToolSelected() {
        return toolSelected;
    }

    /**
     * Returns whether the extrema tool is selected.
     *
     * @return whether the extrema tool is selected
     */
    public boolean isExtremaToolSelected() {
        return extremaToolSelected;
    }

    /**
     * Updates the selected tool.
     */
    public void updateToolsSelection() {
        view.updateToolsSelection();
    }

    /**
     * Returns the math engine.
     *
     * @return math engine
     */
    public MathEngine getMathEngine() {
        return mEngine;
    }

    /**
     * Returns whether to use Javascript evaluation engine.
     *
     * @return whether to use Javascript evaluation engine
     */
    public boolean isUseJSEval() {
        return useJSEval;
    }

    /**
     * Updates the graph values.
     */
    private void recalculateGraphData() {
        updateWindowScale();
        createWindowPoints();
        updateAllViews();
    }

    public BufferedImage requestGraphSnapshot() {
        return graphImage;
    }

    /**
     * Updates the graph info.
     */
    private void updateInfoView() {
        view.updateInfo();
    }

    /**
     * Updates visibility of table of values
     */
    private void updateTableVisibility() {
        view.updateTableVisibility();
    }

    /**
     * Updates all views.
     */
    private void updateAllViews() {
        view.updateAll();
    }

}
