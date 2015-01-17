package graph;

import graph.GraphModel;
import graph.GraphView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * LineStyleController that controls the action for the line style button.
 *
 * @author Kevin Zhou
 * @since 12/31/13
 */

public class LineStyleController implements ActionListener {

    private GraphModel model; // Model linked to the controller
    private GraphView view; // View linked to the controller

    /**
     * Main Constructor. Links model view to this controller.
     *
     * @param model the model linked to the controller
     */
    public LineStyleController(GraphView view, GraphModel model) {
        this.model = model;
        this.view = view;
    }

    /**
     * Opens line style chooser
     *
     * @param e the button click action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        Icon styleIcon = null; // Icon for the prompt
        try {
            styleIcon = new ImageIcon(ImageIO.read(getClass().getResource("../img/LineStyleLarge.png")));
        } catch (IOException eio) {

        }

        Object[] possibilities = {"Solid", "Dashed"};

        String selectedValue = (String) JOptionPane.showInputDialog(null, "Graph line style:", "Select Line Style",
                JOptionPane.PLAIN_MESSAGE
                ,styleIcon, possibilities, possibilities[0]);

        //If a string was returned
        if ((selectedValue != null) && (selectedValue.length() > 0)) {
            if (selectedValue.equals(possibilities[0])) {
                model.setLineStyle(GraphModel.LINE_SOLID);
            } else if (selectedValue.equals(possibilities[1])) {
                model.setLineStyle(GraphModel.LINE_DASHED);
            }
        }
    }

}
