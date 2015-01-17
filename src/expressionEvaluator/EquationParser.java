package expressionEvaluator;

import java.util.*;

/**
 * EquationParser class that evaluates an equation using the PRN algorithm.
 *
 * @author Kevin Zhou
 * @since 12/11/13
 */

public class EquationParser {

    protected static final char NEGATIVE = 0x2122; // Negative character

    protected static final int LEFT_ASSOC = 0; // Left associative
    protected static final int RIGHT_ASSOC = 1; // Right associative

    protected static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>(); // Operators and functions key

    static {
        OPERATORS.put("+", new int[]{0, LEFT_ASSOC});
        OPERATORS.put("-", new int[]{0, LEFT_ASSOC});
        OPERATORS.put("*", new int[]{5, LEFT_ASSOC});
        OPERATORS.put("/", new int[]{5, LEFT_ASSOC});
        OPERATORS.put(String.valueOf(NEGATIVE), new int[]{5, RIGHT_ASSOC});
        OPERATORS.put("tan", new int[]{5, RIGHT_ASSOC});
        OPERATORS.put("sin", new int[]{5, RIGHT_ASSOC});
        OPERATORS.put("cos", new int[]{5, RIGHT_ASSOC});
        OPERATORS.put("sqrt", new int[]{5, RIGHT_ASSOC});
        OPERATORS.put("log", new int[]{5, RIGHT_ASSOC});
        OPERATORS.put("ln", new int[]{5, RIGHT_ASSOC});
        OPERATORS.put("^", new int[]{10, RIGHT_ASSOC});
    }

    protected static final List<String> FUNCTIONS = new ArrayList<String>(); // Functions key

    static {
        FUNCTIONS.add("sin");
        FUNCTIONS.add("cos");
        FUNCTIONS.add("tan");
        FUNCTIONS.add("sqrt");
        FUNCTIONS.add("log");
        FUNCTIONS.add("ln");
    }

    /**
     * Evaluates an equation at a x value.
     *
     * @param expression function to solve
     * @param x x value
     * @return solution to evaluation
     */
    static Double evalExpression(String expression, double x) {
        String finalExpression = expression.replaceAll("x", "(" + String.valueOf(x) + ")"); // Equation after x has been substituted
        Queue<String> postfix = infixToRPN(finalExpression); // Converted postfix notation

        Stack<Double> eval = new Stack<Double>(); // Evaluation stack

        for (String token : postfix) {
            if (token.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") || token.equals("Infinity")) {
                eval.push(Double.parseDouble(token));
            } else if (token.equals(String.valueOf(NEGATIVE))) {
                if (eval.size() > 0) {
                    eval.push(eval.pop() * -1);
                }
            } else if (FUNCTIONS.contains(token)) {
                if (eval.size() > 0) {
                    eval.push(evalFunction(eval.pop(), token));
                }
            } else {
                if (eval.size() >= 2) {
                    double x1 = eval.pop();
                    double x2 = eval.pop();
                    eval.push(evalOperation(x1, x2, token.charAt(0)));
                } else {
                    return null; // Invalid function
                }
            }
        }

        if (eval.size() > 0) {
            return eval.pop();
        } else {
            return null; // Invalid function
        }
    }

    /**
     * Solves one function in an equation.
     *
     * @param x x value to solve for
     * @param function function type
     * @return solution
     */
    private static Double evalFunction(double x, String function) {
        if (function.equals("sin")) {
            return Math.sin(x);
        } else if (function.equals("cos")) {
            return Math.cos(x);
        } else if (function.equals("tan")) {
            return Math.tan(x);
        } else if (function.equals("log")) {
            return Math.log10(x);
        } else if (function.equals("ln")) {
            return Math.log(x);
        } else if (function.equals("sqrt")) {
            return Math.sqrt(x);
        } else {
            return null; // Invalid function
        }
    }

    /**
     * Evaluates an operation between two numbers.
     * @param x1 first number
     * @param x2 second number
     * @param operator operation
     * @return solution
     */
    private static Double evalOperation(double x1, double x2, char operator) {
        switch (operator) {
            case '+':
                return x2 + x1;
            case '-':
                return x2 - x1;
            case '*':
                return x2 * x1;
            case '/':
                return x2 / x1;
            case '^':
                return Math.pow(x2, x1);
            default:
                return null; // Invalid function
        }
    }

    /**
     * Converts equation from infix notation to reverse polish notation (postfix)
     *
     * @param infix equation in infix notation
     * @return equation in postfix notation
     */
    static Queue<String> infixToRPN(String infix) {
        Stack<String> stack = new Stack<String>(); // Stack used to convert to reverse polish notation
        Queue<String> postfix = new LinkedList<String>(); // Queue used to store converted equation

        boolean isPrevOperator = true; // If the previous token was an operator

        String[] tokens = tokenize(infix); // Tokenized equation

        for (String token : tokens) {

            if (token.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {
                postfix.add(token);
                isPrevOperator = false;
            } else if (OPERATORS.containsKey(token)) {
                if (token.equals("-") && isPrevOperator) {
                    token = String.valueOf(NEGATIVE);
                }
                isPrevOperator = true;
                while (
                        (!stack.isEmpty() && OPERATORS.containsKey(stack.peek()))
                                && (
                                (OPERATORS.get(token)[1] == LEFT_ASSOC
                                        && OPERATORS.get(token)[0] <= OPERATORS.get(stack.peek())[0])
                        )
                        ) {
                    postfix.add(stack.pop());
                }
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfix.add(stack.pop());
                }
                stack.pop();
            } else if (token.length() > 0) {
                return null; // Invalid function
            }

        }

        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }

        return postfix;
    }

    /**
     * Converts the equation into tokens.
     *
     * @param infix equation in String format
     * @return tokenized equation
     */
    static private String[] tokenize(String infix) {
        infix = infix.replaceAll("E", "*10^"); // Converts scientific notation form
        return infix.replaceAll("\\s+\\d\\d(?=\\s+)", "").split("(?<=[-+*/()^])|(?=[-+*/()^])"); // Remove whitespace and tokenize
    }
}