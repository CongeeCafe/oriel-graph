package expressionEvaluator;

/**
 * InputValidator class that validates user input in the program.
 *
 * @author Kevin Zhou
 * @since 1/12/14
 */

public class InputValidator {

    /**
     * Validates scale input.
     *
     * @param input scale input
     * @return whether input is valid
     */
    public static boolean validateScale(String input) {
        if (input.matches("^[0-9/.]*$")) {
            return true;
        }
        return false;
    }

    /**
     * Validates thickness input.
     *
     * @param input thickness input
     * @return whether input is valid
     */
    public static boolean validateThickness(String input) {
        try {
            int thickness = Integer.parseInt(input); // Input

            if (thickness > 0 && thickness <= 10) {
                return true;
            }
            return false;

        } catch( NumberFormatException e ) {
            return false;
        }
    }

    /**
     * Validates the scale for the table of values.
     *
     * @param scale scale of table of values
     * @return whether the scale is valid
     */
    public static boolean validateTableScale(double scale) {
        if (scale > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateGraphBounds(double minX, double maxX, double minY, double maxY) {
        if (minX < maxX && minY < maxY) {
            return true;
        } else {
            return false;
        }
    }

}
