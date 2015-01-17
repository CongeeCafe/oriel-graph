package graph;

import graph.GraphModel;
import graph.GraphView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * SaveImageController that controls the action for the export button.
 *
 * @author Kevin Zhou
 * @since 12/31/13
 */

public class SaveImageController implements ActionListener {

    private GraphModel model; // Model linked to the controller
    private GraphView view; // View linked to the controller

    /**
     * Main Constructor. Links model view to this controller.
     *
     * @param model the model linked to the controller
     */
    public SaveImageController(GraphView view, GraphModel model) {
        this.model = model;
        this.view = view;
    }

    /**
     * Opens color chooser
     *
     * @param e the button click action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        JFileChooser c = new JFileChooser(); // FileChoose window

        // Demonstrate "Save" dialog:
        int rVal = c.showSaveDialog(view);
        String filename = null; // Filename
        String dir = null; // Directory name

        if (rVal == JFileChooser.APPROVE_OPTION) {
            filename = (c.getSelectedFile().getName()); // Filename
            dir = (c.getCurrentDirectory().toString()); // Directory name

            File outputFile = new File(dir + "\\" + filename + ".png"); // Output file path

            try {
                ImageIO.write(model.requestGraphSnapshot(), "png", outputFile);
                JOptionPane.showMessageDialog(view, "Image Saved", "Success", JOptionPane.PLAIN_MESSAGE);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(view, "Unable to save image", "Failure", JOptionPane.PLAIN_MESSAGE);
            }

        } else if (rVal == JFileChooser.CANCEL_OPTION) {
            // Do nothing
        }

    }

}
