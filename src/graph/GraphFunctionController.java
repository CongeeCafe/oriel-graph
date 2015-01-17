package graph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GraphFunctionController that controls the action for the graph function button.
 *
 * @author Kevin Zhou
 * @since 12/31/13
 */

public class GraphFunctionController implements ActionListener {

    private GraphModel model; // Model linked to the controller
    private JTextField equationField; // Text field where the user enters the function

    /**
     * Main Constructor. Links model and text field from view to this controller.
     *
     * @param model the model linked to the controller
     * @param equationField
     */
    public GraphFunctionController(GraphModel model, JTextField equationField) {
        this.model = model;
        this.equationField = equationField;
    }

    /**
     * Retrieves the user entered function from the text field and updates it in the model.
     *
     * @param e the button click action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String function = equationField.getText(); // Function
        model.setFunction(function);
    }

}
