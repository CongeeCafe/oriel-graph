package tableOfValues;

import graph.GraphModel;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * TableVisibilityController class that controls the action for the table visibility checkbox.
 *
 * @author Kevin Zhou
 * @since 11/14/13
 */

public class TableVisibilityController extends Object implements ItemListener {

    private GraphModel model; // Model for the graph
    private JCheckBox checkBox; // Checkbox linked to the controller

    /**
     * Main constructor. Links the component to the model.
     *
     * @param model    the model linked to the controller
     * @param checkBox the checkbox component being linked to the controller
     */
    public TableVisibilityController(GraphModel model, JCheckBox checkBox) {
        this.model = model;
        this.checkBox = checkBox;
    }

    /**
     * Toggles the radian option selection.
     *
     * @param e the change event of the checkbox
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        // Changes model data
        if (e.getStateChange() == ItemEvent.SELECTED) {
            this.model.setTableValuesVisible(true);
        } else {
            this.model.setTableValuesVisible(false);
        }
    }
}

