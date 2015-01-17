package expressionEvaluator;

import graph.GraphModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * EngineOptionController class that controls the actions for the graph engine selection. This class is no longer
 * used in the final program.
 *
 * @author Kevin Zhou
 * @since 12/11/13
 */

public class EngineOptionsController implements ActionListener {

    GraphModel model; // Model linked to controller

    /**
     * Main Constructor.
     *
     * @param model model to link with the controller
     */
    public EngineOptionsController(GraphModel model) {
        this.model = model;
    }

    /**
     * Updates engine option.
     *
     * @param e action event
     */
    public void actionPerformed(ActionEvent e) {
        JRadioButton button = (JRadioButton) e.getSource(); // Radio button changed

        if (button.getText().equals("RPN")) {
            model.setUseJSEval(false);
        } else if (button.getText().equals("JavaScript")) {
            model.setUseJSEval(true);
        }
    }
}
