package expressionEvaluator;

/**
 *  The EquationItem class that stores attributes of an equation. This class is unused.
 *
 *  @author Danny Ngo
 */

import java.awt.*;

public class EquationItem extends Object
{
    private String equation ;// User Equation
    private Color lineColor;// The equation colour
    private int lineThickness;// Line thickness

    /** Main Constucter
     * @param equation equation the equation
     * @param color the color of the equation
     * @param lineThickness the line thickness of the equation
     */
    public EquationItem(String equation,Color color, int lineThickness)
    {
        lineColor = color;
        this.lineThickness = lineThickness;
        this.equation = equation;
    }

    /*Get returns the color */
    public Color getColor()
    {
        return lineColor;
    }
    /*Get returns the equation */
    public String getEquation()
    {
        return equation;
    }
    /*Get returns the line thickness */
    public int getLineThickness()
    {
        return lineThickness;
    }

}
