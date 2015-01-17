package tableOfValues;
/**
 * ApplyTableBoundsController
 * The controller for the Bounds button
 *
 * @author Danny Ngo
 */

import expressionEvaluator.InputValidator;
import graph.Alert;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ApplyTableBoundsController implements ActionListener {

    private TableOfValuesModel view;
    private JButton button;

    private JFormattedTextField boundMinX;
    private JFormattedTextField boundMaxX;
    private JFormattedTextField scale;

    // Constructer
    public ApplyTableBoundsController(TableOfValuesModel model, JFormattedTextField boundMinX,
                                      JFormattedTextField boundMaxX, JFormattedTextField scale,
                                      JButton button) {
        this.boundMinX = boundMinX;
        this.boundMaxX = boundMaxX;
        this.scale = scale;

        //this.model = model;
        this.view = model;
        this.button = button;
    }

    /* actionPeformed
     * @Parm ActionEvent e  When the user click the button
     * When button is click get the new scale and table data
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        double minX = Double.parseDouble(boundMinX.getText());
        double maxX = Double.parseDouble(boundMaxX.getText());
        double newScale = Double.parseDouble(scale.getText());

        view.setBounds(maxX, minX);

        if (InputValidator.validateTableScale(newScale)) {
            view.setScale(newScale);
        } else {
            Alert.showSettingsError(null, "Invalid scale.");
        }
        view.update();
    }

}
