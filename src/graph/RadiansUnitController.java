package graph;

import graph.GraphModel;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * RadiansUnitController class that controls the unit selection option.
 *
 * @author Kevin Zhou
 * @since 11/14/13
 */

public class RadiansUnitController extends Object implements ItemListener {

    private GraphModel model; // Model for the graph
    private JCheckBox checkBox; // Checkbox linked to the controller

    /**
     * Main constructor. Links the component to the model.
     *
     * @param model    the model linked to the controller
     * @param checkBox the checkbox component being linked to the controller
     */
    public RadiansUnitController(GraphModel model, JCheckBox checkBox) {
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
        String checkBoxName = checkBox.getName(); // Checkbox name

        // Changes model data
        if (checkBoxName.equals("x")) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.model.setUnitsX(GraphModel.UNIT_RADIANS);
            } else {
                this.model.setUnitsX(GraphModel.UNIT_DEGREES);
            }
        } else if (checkBoxName.equals("y")) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.model.setUnitsY(GraphModel.UNIT_RADIANS);
            } else {
                this.model.setUnitsY(GraphModel.UNIT_DEGREES);
            }
        }
    }

}
