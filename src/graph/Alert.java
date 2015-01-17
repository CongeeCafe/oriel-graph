package graph;

import graph.GraphView;

import javax.swing.*;

/**
 * Alert class that displays alerts to the user.
 *
 * @author Kevin Zhou
 * @since 1/15/14
 */

public class Alert {

    /**
     * Show alert for invalid function.
     */
    public static void showGraphError() {
        JOptionPane.showMessageDialog(null,
                "Something went wrong while trying to graph this function.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show alert for invalid settings.
     *
     * @param view parent view
     * @param message message to display
     */
    public static void showSettingsError(GraphView view, String message) {
        JOptionPane.showMessageDialog(view,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

}
