package graph;

import graph.GraphModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ExtremaButtonController class that controls the action for the extrema button. Toggles the tool to select extrema
 * calculation.
 *
 * @author Kevin Zhou
 * @since 12/31/13
 */

public class ExtremaButtonController implements ActionListener {

    private GraphModel model; // Model linked to the controller
    private JButton button; // Button to activate tool to find function extremum

    /**
     * Main constructor.
     *
     * @param model  model linked to the controller
     * @param button button to activate tool to find function extremum
     */
    public ExtremaButtonController(GraphModel model, JButton button) {
        this.model = model;
        this.button = button;
    }

    /**
     * Activates or disables the tool.
     *
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        model.toggleExtremaTool();
    }

}
