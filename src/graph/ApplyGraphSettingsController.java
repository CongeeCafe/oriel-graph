package graph;

import expressionEvaluator.InputValidator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ApplyGraphSettingsController that controls the button actions for the graph Apply button.
 *
 * @author Kevin Zhou
 * @since 12/31/13
 */

public class ApplyGraphSettingsController implements ActionListener {

    private GraphModel model; // Model linked to the controller
    private JButton button; // Button linked to the controller

    private JTextField yAxisScale; // Text field for scale of y-axis
    private JTextField xAxisScale; // Text field for scale of x-axis

    private JFormattedTextField boundMinX; // Text field for min x bound
    private JFormattedTextField boundMaxX; // Text field for max x bound
    private JFormattedTextField boundMinY; // Text field for min y bound
    private JFormattedTextField boundMaxY; // Text field for max y bound

    /**
     * Main Constructor.
     *
     * @param model model linked to the controller
     * @param xAxisScale text field for scale of y-axis
     * @param yAxisScale text field for scale of x-axis
     * @param boundMinX text field for min x bound
     * @param boundMaxX text field for max x bound
     * @param boundMinY text field for min y bound
     * @param boundMaxY text field for max y bound
     * @param button button linked to the controller
     */
    public ApplyGraphSettingsController(GraphModel model, JTextField xAxisScale,
                                        JTextField yAxisScale, JFormattedTextField boundMinX,
                                        JFormattedTextField boundMaxX,
                                        JFormattedTextField boundMinY, JFormattedTextField boundMaxY,
                                        JButton button) {
        this.xAxisScale = xAxisScale;
        this.yAxisScale = yAxisScale;

        this.boundMinX = boundMinX;
        this.boundMaxX = boundMaxX;
        this.boundMinY = boundMinY;
        this.boundMaxY = boundMaxY;

        this.model = model;
        this.button = button;
    }

    /**
     * Apply settings to model.
     *
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        double minX = Double.parseDouble(boundMinX.getText());
        double maxX = Double.parseDouble(boundMaxX.getText());
        double minY = Double.parseDouble(boundMinY.getText());
        double maxY = Double.parseDouble(boundMaxY.getText());

        if (InputValidator.validateGraphBounds(minX, maxX, minY, maxY)) {
            model.setAreaBounds(new Bounds(minX, maxX, minY, maxY));
        } else {
            Alert.showSettingsError(null, "Invalid graph bounds.");
        }
        model.setAxisScale(xAxisScale.getText(), yAxisScale.getText());
    }

}
