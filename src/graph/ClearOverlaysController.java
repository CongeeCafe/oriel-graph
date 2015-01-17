package graph;

import graph.GraphModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ClearOverlaysController class that controls the clear overlays button action. Clears the overlays on the graph.
 *
 * @author Kevin Zhou
 * @since 12/31/13
 */

public class ClearOverlaysController implements ActionListener {

    private GraphModel model; // Model linked to the controller

    /**
     * Main Constructor. Links model view to this controller.
     *
     * @param model the model linked to the controller
     */
    public ClearOverlaysController(GraphModel model) {
        this.model = model;
    }

    /**
     * Clears the overlays from the view
     *
     * @param e the button click action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        model.clearOverlays();
    }

}
