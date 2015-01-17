package graph;

import expressionEvaluator.InputValidator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * LineThicknessController that controls the action for the line thickness button.
 *
 * @author Kevin Zhou
 * @since 12/31/13
 */

public class LineThicknessController implements ActionListener {

    private GraphModel model; // Model linked to the controller
    private GraphView view; // View linked to the controller

    /**
     * Main Constructor. Links model view to this controller.
     *
     * @param model the model linked to the controller
     */
    public LineThicknessController(GraphView view, GraphModel model) {
        this.model = model;
        this.view = view;
    }

    /**
     * Opens line thickness chooser
     *
     * @param e the button click action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Icon thicknessIcon = null; // Icon for the prompt
        try {
            thicknessIcon = new ImageIcon(ImageIO.read(getClass().getResource("../img/LineWeightLarge.png")));
        } catch (IOException eio) {

        }

        String thickness = (String) JOptionPane.showInputDialog(view, "Graph thickness between 1-10:",
                "Select Line Width",
                JOptionPane.QUESTION_MESSAGE, thicknessIcon, null, null);

        if (InputValidator.validateThickness(thickness) == true) {
            model.setLineThickness(Integer.parseInt(thickness));
        } else if (thickness != null) {
            Alert.showSettingsError(view, "Input must be between 1 and 10.");
        }
    }

}
