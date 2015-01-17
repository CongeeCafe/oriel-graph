import graph.GraphModel;
import graph.GraphView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Startup class that starts the main program.
 *
 * @author Kevin Zhou
 * @since 12/11/13
 */
public class Startup {

    /**
     * Main class; starts up the program.
     *
     * @param args execution arguments
     */
    public static void main(String[] args) {
        // Set native look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

        }

        JFrame window = new JFrame(); // Main window

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBackground(Color.WHITE);
        window.setTitle("Oriel Graph");
        setProgramIcons(window);

        GraphModel model = new GraphModel();
        GraphView view = new GraphView(model);

        window.add(view);

        window.setVisible(true);
        window.pack();
    }

    /**
     * Sets program icon.
     *
     * @param window program window
     */
    private static void setProgramIcons(JFrame window) {
        BufferedImage appIcon16 = null; // Application icon 16x16
        BufferedImage appIcon32 = null; // Application icon 32x32
        ArrayList<Image> images = new ArrayList<Image>(); // Images of all sizes

        try {
            appIcon16 = ImageIO.read(Startup.class.getResource("img/AppIcon_16x16.png"));
            appIcon32 = ImageIO.read(Startup.class.getResource("img/AppIcon_32x32.png"));

            images.add(appIcon16);
            images.add(appIcon32);

            window.setIconImages(images); // Set icons

        } catch (Exception e) {

        }
    }
}
