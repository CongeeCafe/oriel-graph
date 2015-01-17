package graph;

import graph.GraphModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ZerosButtonController that controls the action for the zero button.
 *
 * @author Kevin Zhou
 * @since 12/31/13
 */

public class ZerosButtonController implements ActionListener {

    private GraphModel model; // The model linked to the controller
    private JButton button; // The button linked to the controller

    /**
     * Main constructor.
     *
     * @param model  the model linked to the controller
     * @param button the button linked to the controller
     */
    public ZerosButtonController(GraphModel model, JButton button) {
        this.model = model;
        this.button = button;
    }

    /**
     * Toggles the tool to calculate function zeros.
     *
     * @param e click event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        model.toggleZerosTool();
    }

}
