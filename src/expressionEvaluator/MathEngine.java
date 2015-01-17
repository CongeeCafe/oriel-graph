package expressionEvaluator;

import graph.GraphModel;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * MathEngine class that provides methods to calculate graph information.
 *
 * @author Kevin Zhou
 * @since 12/11/13
 */

public class MathEngine {

    private String function; // User entered function
    private String functionJS; // Function for use in JS engine

    GraphModel model; // Model

    private ScriptEngineManager scriptManager; // Script manager
    private ScriptEngine sEngine; // Script engine

    /**
     * Main constructor.
     *
     * @param model model the class is linked to
     */
    public MathEngine(GraphModel model) {
        this.model = model;

        scriptManager = new ScriptEngineManager();
        sEngine = scriptManager.getEngineByName("JavaScript");
    }

    /**
     * Prepares the user function to be evaluated and sets the active function.
     *
     * @param inFunction user entered function
     */
    public void setFunction(String inFunction) {
        // Check hidden multiplication
        inFunction = inFunction.replaceAll("([0-9])([a-z])", "$1*$2"); // e.g. 4x -> 4*x
        inFunction = inFunction.replaceAll("([a-z])([0-9])", "$1*$2"); // e.g. x4 -> x*4
        inFunction = inFunction.replaceAll("([0-9a-z]\\))\\(", "$1*("); // e.g. 4(x) -> 4*(x) or x(4) -> x*(4) or (x)(4) -> (x)*(4)
        inFunction = inFunction.replaceAll("\\)([0-9a-z])", ")*$1"); // e.g. (x)4 -> (x)*4 or (4)x -> (4)*x

        this.function = inFunction;

        // Convert functions to javascript-readable
        inFunction = inFunction.replace("sin", "Math.sin");
        inFunction = inFunction.replace("cos", "Math.cos");
        inFunction = inFunction.replace("tan", "Math.tan");
        inFunction = inFunction.replace("ln", "Math.log");
        inFunction = inFunction.replace("sqrt", "Math.sqrt");

        this.functionJS = inFunction;
    }

    /**
     * Returns function.
     *
     * @return function
     */
    public String getFunction() {
        return function;
    }

    /**
     * Evaluates the expression for variable x.
     *
     * @param x variable x
     * @return y value at x of function
     */
    public double evalExpression(double x) {
        if (model.isUseJSEval()) {
            return evalExpJS(x);
        } else {
            return evalExp(x);
        }
    }

    /**
     * Evaluates the expression for variable x.
     *
     * @param x variable x
     * @return y value at x of function
     */
    public double f(double x) {
        return evalExpression(x);
    }

    /**
     * Evaluates the function using the native equation parser.
     *
     * @param x x value to evaluate for
     * @return y value at x of function
     */
    private double evalExp(double x) {
        return EquationParser.evalExpression(function, x);
    }

    /**
     * Evaluates the function using the Javascript engine.
     *
     * @param x x value to evaluate for
     * @return y value at x of function
     */
    private double evalExpJS(double x) {
        String expression = functionJS.replaceAll("x", x + "");
        Double result = null; // Result of evaluation

        try {
            result = (Double) sEngine.eval(expression);
        } catch (ScriptException e) {

        }

        return result;
    }

    /**
     * Evaluates function for multiple x values.
     *
     * @param minX  minimum x value
     * @param maxX  maximum x value
     * @param scale how much the x values step by
     * @return array of values for function
     */
    public Double[][] evalExpression(double minX, double maxX, double scale) {
        Double[][] values; // X and y values calculated for function
        int numValues = 0; // The number of values to calculate

        for (double i = minX; i <= maxX; i += scale) {
            numValues++;
        }

        values = new Double[numValues][2];

        int count = 0; // Keeps track of iterations in array

        for (double i = minX; i <= maxX; i += scale) {
            values[count][0] = i;
            values[count][1] = evalExpression(i);

            count++;
        }

        return values;
    }

    /**
     * Finds first derivative of function at x.
     *
     * @param x x value
     * @return first derivative
     */
    private double deriv(double x) {
        double h = 1e-6; // A really small number for use in calculation
        return (f(x + h) - f(x)) / h;
    }

    /**
     * Find second derivative of function at x.
     *
     * @param x x value
     * @return second derivative
     */
    private double deriv2(double x) {
        double h = 1e-6; // A really small number for use in calculation
        return (f(x + h) - 2.0 * f(x) + f(x - h)) / (h * h);
    }

    /**
     * Calculates extremum of function closest to guess point.
     *
     * @param x x value of guess point
     * @return x and y of extremum in array
     */
    public float[] calcExtreme(double x) {
        double tolerance = 1E-8; // Stop if  close enough
        int max_count = 500; // Maximum number of Newton's method iterations
        float[] result = new float[2]; // Result of calculation

        for (int count = 1; (java.lang.Math.abs(deriv(x)) > tolerance) && (count < max_count); count++) {
            x = x - deriv(x) / deriv2(x);  //Newtons method.
        }

        // Results
        if (Math.abs(deriv(x)) <= tolerance) {
            result[0] = (float) x;
            result[1] = (float) f(x);
        } else {
            result = null; // Failed to find a local extremum
        }

        return result;
    }

    /**
     * Calculates zero of function closest to the guess point.
     *
     * @param x x value of guess point
     * @return x and y values of zero in array
     */
    public float[] calcZero(double x) {
        double tolerance = 1E-8; // Stop if close enough
        int max_count = 500; // Maximum number of Newton's method iterations
        float[] result = new float[1]; // Result of calculation

        for (int count = 1; (Math.abs(f(x)) > tolerance) && (count < max_count); count++) {
            x = x - f(x) / deriv(x);  //Newtons method.
        }

        // Results
        if (Math.abs(f(x)) <= tolerance) {
            result[0] = (float) x;
        } else {
            result = null; // Failed to find a zero
        }

        return result;
    }

    /**
     * Finds the greatest common denominator of a fraction.
     *
     * @param a numerator of fraction
     * @param b denominator of fraction
     * @return the greatest common denominator
     */
    private int gcm(int a, int b) {
        return b == 0 ? a : gcm(b, a % b);
    }

    /**
     * Changes the numerator and denominator to its most reduced form.
     *
     * @param a numerator of fraction
     * @param b denominator of fraction
     * @return reduced fraction in array form
     */
    public int[] asFraction(int a, int b) {
        int[] frac = new int[2]; // Fraction
        int gcm = gcm(a, b); // Greatest common denominator

        frac[0] = (a / gcm);
        frac[1] = (b / gcm);

        return frac;
    }

}