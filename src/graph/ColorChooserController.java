package graph;

import graph.GraphModel;
import graph.GraphView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ColorChooserController class that controls the action of the color chooser button. Shows a dialog for user to
 * select graph color.
 *
 * @author Kevin Zhou
 * @since 12/31/13
 */

public class ColorChooserController implements ActionListener {

    private GraphModel model; // Model linked to the controller
    private GraphView view; // View linked to the controller

    /**
     * Main Constructor. Links model view to this controller.
     *
     * @param model the model linked to the controller
     */
    public ColorChooserController(GraphView view, GraphModel model) {
        this.model = model;
        this.view = view;
    }

    /**
     * Opens color chooser.
     *
     * @param e the button click action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(view, "Choose Graph Color", model.getLineColor()); // Color chooser
                                                                                                     // color value

        if (newColor != null) {
            model.setLineColor(newColor);
        }
    }

}
