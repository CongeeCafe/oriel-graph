package graph;

/**
 * Bounds class that hold the graph bounds values.
 *
 * @author Kevin Zhou
 * @since 12/13/13
 */

public class Bounds extends Object{

    double minX; // Min x value
    double maxX; // Max x value
    double minY; // Min y value
    double maxY; // Max y value

    /**
     * Constructor to create a blank graph.Bounds object.
     */
    public Bounds() {
        super();
    }

    /**
     * Constructor to create a graph.Bounds object with assigned values.
     *
     * @param minX min x value
     * @param maxX max x value
     * @param minY min y value
     * @param maxY max y value
     */
    public Bounds(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    /**
     * Sets the min x value.
     *
     * @param minX min x value
     */
    public void setMinX(double minX) {
        this.minX = minX;
    }

    /**
     * Sets the max x value.
     *
     * @param maxX max x value
     */
    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    /**
     * Sets the min y value.
     *
     * @param minY min y value
     */
    public void setMinY(double minY) {
        this.minY = minY;
    }

    /**
     * Sets the max y value.
     *
     * @param maxY max y value
     */
    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    /**
     * Gets the min x value.
     *
     * @return min x value
     */
    public double getMinX() {
        return minX;
    }

    /**
     * Gets the max x value.
     *
     * @return max x value
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     * Gets the min y value.
     *
     * @return min y value
     */
    public double getMinY() {
        return minY;
    }

    /**
     * Gets the max y value.
     *
     * @return max y value
     */
    public double getMaxY() {
        return maxY;
    }
}
