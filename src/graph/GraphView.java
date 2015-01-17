package graph;

import expressionEvaluator.EngineOptionsController;
import tableOfValues.TableOfValues;
import tableOfValues.TableOfValuesModel;
import tableOfValues.TableVisibilityController;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * GraphView class that holds the main graph component and graph layers.
 *
 * @author Kevin Zhou
 * @since 12/11/13
 */

public class GraphView extends JPanel {

    private GraphModel model; // The model linked to the view
    private TableOfValuesModel tableModel; // Model for table of values

    private JPanel toolsPanel = new JPanel(); // Panel to hold graph tools
    private JPanel equationPanel = new JPanel(); // Panel to hold equation tools

    private JLayeredPane layeredPane = new JLayeredPane(); // Layered pane for layers that makeup the graph

    private GraphInfo graphInfo; // Graph info layer
    private GraphSelection graphSelection = new GraphSelection(); // Graph selection layer
    private GraphArea graphArea; // Graph area layer

    private BorderLayout mainLayout = new BorderLayout(); // Main layout to hold graph and toolbars
    private BorderLayout masterLayout = new BorderLayout(); // Master layout to hold all components

    private JPanel mainPanel = new JPanel(); // Main panel to hold graph and toolbars

    private TableOfValues tableOfValues; // Table of values
    private GridBagLayout toolsLayout = new GridBagLayout(); // Layout for toolbar

    private JTextField equationField = new JTextField(30); // Text field to enter user function
    private JLabel yEqualsLabel = new JLabel("Y="); // Label next to equation field
    private JButton graphEquationButton = new JButton("Graph"); // Button to graph the function
    private JButton colorChangeButton = new JButton(); // Button to change graph color
    private JButton lineStrokeButton = new JButton(); // Button to change graph stroke width
    private JButton lineStyleButton = new JButton(); // Button to change graph line style

    private JCheckBox showTableOfValues = new JCheckBox("Show Table of Values", true);

    private JButton findMaxButton = new JButton("Extrema"); // Button to toggle extrema calculation tool
    private JButton findZerosButton = new JButton("Zero"); // Button to toggle zeros calculation tool
    private JButton saveAsImageButton = new JButton("Export"); // Button to save graph as image
    private JButton clearOverlaysButton = new JButton("Clear"); // Button to clear graph overlays

    private JButton applyBounds = new JButton("Apply"); // Button to apply graph settings

    private JPanel engineOptions = new JPanel(); // Panel to hold engine option components
    private BoxLayout engineLayout = new BoxLayout(engineOptions, BoxLayout.Y_AXIS); // Layout for engine options panel

    ButtonGroup engineGroup = new ButtonGroup(); // Button group for engine options
    private JRadioButton engineJS = new JRadioButton("JavaScript"); // Option to select JS engine
    private JRadioButton engineNative = new JRadioButton("RPN"); // Option to select native engine

    private JLabel scaleLabelX = new JLabel("Scale: "); // Scale label for x-axis
    private JLabel scaleLabelY = new JLabel("Scale: "); // Scale label for y-axis

    private JPanel xAxisOptions = new JPanel(new BorderLayout()); // Layout for x-axis scale options
    private JPanel yAxisOptions = new JPanel(new BorderLayout()); // Layout for y-axis scale options

    private JCheckBox xAxisRadians = new JCheckBox("Radians"); // Checkbox to select x-axis radians option
    private JCheckBox yAxisRadians = new JCheckBox("Radians"); // Checkbox to select y-axis radians option

    private JTextField xAxisScale; // Field to enter x-axis scale
    private JTextField yAxisScale; // Field to enter y-axis scale

    private JPanel windowBoundsOptions = new JPanel(new BorderLayout()); // Panel to hold window bounds options
    private JPanel windowBoundsSubTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 2)); // Window top subpanel
    private JPanel windowBoundsSubBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3)); // Window bottom subpanel

    private JLabel minXLabel = new JLabel("Min X:"); // Label for min graph x
    private JLabel maxXLabel = new JLabel(" Max X:"); // Label for max graph x
    private JLabel minYLabel = new JLabel("Min Y:"); // Label for min graph y
    private JLabel maxYLabel = new JLabel(" Max Y:"); // Label for max graph y

    private JFormattedTextField boundMinX; // Field for min graph x
    private JFormattedTextField boundMaxX; // Field for max graph x
    private JFormattedTextField boundMinY; // Field for min graph y
    private JFormattedTextField boundMaxY; // Field for max graph y

    /**
     * Main constructor.
     *
     * @param model model to link to view
     */
    public GraphView(GraphModel model) {
        super();

        this.model = model;
        this.model.setGUI(this);

        tableModel = new TableOfValuesModel(model.getMathEngine());
        tableOfValues = new TableOfValues(tableModel);

        this.layoutView();
        this.registerControllers();
    }

    /**
     * Sets up layout of view.
     */
    public void layoutView() {
        this.setLayout(masterLayout);

        mainPanel.setLayout(mainLayout);

        graphArea = new GraphArea(model);
        graphInfo = new GraphInfo(model);

        layeredPane.setPreferredSize(new Dimension(480, 480));

        // Set layers to transparent
        graphInfo.setOpaque(false);
        graphSelection.setOpaque(false);

        // Adds layers to layer pane
        layeredPane.add(graphSelection, new Integer(2));
        layeredPane.add(graphInfo, new Integer(1));
        layeredPane.add(graphArea, new Integer(0));

        // Add components to equation panel
        equationPanel.add(yEqualsLabel);
        equationPanel.add(equationField);
        equationPanel.add(graphEquationButton);
        equationPanel.add(colorChangeButton);
        equationPanel.add(lineStrokeButton);
        equationPanel.add(lineStyleButton);
        equationPanel.add(showTableOfValues);

        // Add components to engine options panel
        engineOptions.setLayout(engineLayout);
        engineOptions.add(engineNative);
        engineOptions.add(engineJS);

        engineOptions.setBorder(BorderFactory.createTitledBorder("Engine"));

        // Group engine options
        engineGroup.add(engineNative);
        engineGroup.add(engineJS);
        engineNative.setSelected(true);

        // Add listener to manually resize layers on window size change, since LayeredPane does not offer this feature
        layeredPane.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                graphArea.invalidate();

                int width = layeredPane.getWidth();
                int height = layeredPane.getHeight();

                graphInfo.setBounds(0, 0, width, height);
                graphArea.setBounds(0, 0, width, height);
                graphSelection.setBounds(0, 0, width, height);

                model.updateSize(width, height);
            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }

            @Override
            public void componentMoved(ComponentEvent event) {

            }
        });

        // Adds graph layers and tools panel
        mainPanel.add(toolsPanel, BorderLayout.NORTH);
        mainPanel.add(layeredPane, BorderLayout.CENTER);

        this.add(mainPanel, BorderLayout.CENTER);
        this.add(tableOfValues, BorderLayout.EAST);

        tableOfValues.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(171, 173, 179)));

        // Input format for bounds fields
        NumberFormat numFormat = NumberFormat.getNumberInstance();
        numFormat.setGroupingUsed(false);

        boundMinX = new JFormattedTextField(numFormat);
        boundMaxX = new JFormattedTextField(numFormat);
        boundMinY = new JFormattedTextField(numFormat);
        boundMaxY = new JFormattedTextField(numFormat);

        boundMinX.setColumns(5);
        boundMaxX.setColumns(5);
        boundMinY.setColumns(5);
        boundMaxY.setColumns(5);

        // Input format for scale fields
        xAxisScale = new JFormattedTextField();
        yAxisScale = new JFormattedTextField();

        xAxisScale.setColumns(5);
        yAxisScale.setColumns(5);

        xAxisOptions.setBorder(BorderFactory.createTitledBorder("X-axis"));
        yAxisOptions.setBorder(BorderFactory.createTitledBorder("Y-axis"));

        xAxisOptions.add(xAxisScale, BorderLayout.CENTER);
        xAxisOptions.add(scaleLabelX, BorderLayout.WEST);
        xAxisOptions.add(xAxisRadians, BorderLayout.SOUTH);

        yAxisOptions.add(yAxisScale, BorderLayout.CENTER);
        yAxisOptions.add(scaleLabelY, BorderLayout.WEST);
        yAxisOptions.add(yAxisRadians, BorderLayout.SOUTH);

        windowBoundsOptions.setBorder(BorderFactory.createTitledBorder("Window graph.Bounds"));

        windowBoundsSubTop.add(minXLabel);
        windowBoundsSubTop.add(boundMinX);
        windowBoundsSubTop.add(maxXLabel);
        windowBoundsSubTop.add(boundMaxX);
        windowBoundsSubBottom.add(minYLabel);
        windowBoundsSubBottom.add(boundMinY);
        windowBoundsSubBottom.add(maxYLabel);
        windowBoundsSubBottom.add(boundMaxY);

        windowBoundsOptions.add(windowBoundsSubTop, BorderLayout.NORTH);
        windowBoundsOptions.add(windowBoundsSubBottom, BorderLayout.SOUTH);

        // Tools panel style
        toolsPanel.setLayout(toolsLayout);

        toolsPanel.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(171, 173, 179)),
                BorderFactory.createEmptyBorder(2, 4, 2, 4)));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 3); // Padding

        // Add tool buttons
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;
        c.anchor = GridBagConstraints.LINE_START;
        toolsPanel.add(findMaxButton, c);

        c.gridx = 1;
        toolsPanel.add(findZerosButton, c);

        c.gridx = 2;
        toolsPanel.add(clearOverlaysButton, c);

        c.gridx = 3;
        c.weightx = 1;
        toolsPanel.add(saveAsImageButton, c);

        // Add equation panel
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 11;
        c.weightx = 1;
        toolsPanel.add(equationPanel, c);

        // Apply button
        c.gridx = 11;
        c.gridy = 0;
        c.weightx = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        toolsPanel.add(applyBounds, c);

        // Add engine options (disabled due to lack of screen real estate)
 /*
        c.gridx = 4;
        c.gridy = 0;
        c.gridheight = 2;
        c.weighty = 1.0;
        toolsPanel.add(engineOptions, c);
*/

        // Add x-axis options
        c.gridx = 5;
        c.gridy = 0;
        toolsPanel.add(xAxisOptions, c);

        // Add y-axis options
        c.gridx = 6;
        c.gridy = 0;
        c.gridheight = 2;
        toolsPanel.add(yAxisOptions, c);

        // Graph bounds options

        c.gridx = 7;
        c.gridy = 0;
        toolsPanel.add(windowBoundsOptions, c);

        // Set button images
        try {
            Image maxButtonImg = ImageIO.read(getClass().getResource("../img/GraphMax.png")); // Max button icon
            Image zeroButtonImage = ImageIO.read(getClass().getResource("../img/GraphZeros.png")); // Zero button icon
            Image clearOverlaysImage= ImageIO.read(getClass().getResource("../img/ClearOverlays.png")); // Clear overlays
                                                                                                 // button icon
            Image saveImage = ImageIO.read(getClass().getResource("../img/SaveImage.png")); // Save button icon
            Image lineThicknessImage = ImageIO.read(getClass().getResource("../img/LineWeight.png")); // Line weight button
                                                                                               // icon
            Image lineStyleImage = ImageIO.read(getClass().getResource("../img/LineStyle.png")); // Line style button icon
            Image applyImage = ImageIO.read(getClass().getResource("../img/Apply.png")); // Apply graph settings button icon
            Image colorImage = ImageIO.read(getClass().getResource("../img/ColorChooser.png")); // Color chooser button icon

            findMaxButton.setIcon(new ImageIcon(maxButtonImg));
            findZerosButton.setIcon(new ImageIcon(zeroButtonImage));
            clearOverlaysButton.setIcon(new ImageIcon(clearOverlaysImage));
            saveAsImageButton.setIcon(new ImageIcon(saveImage));
            applyBounds.setIcon(new ImageIcon(applyImage));

            colorChangeButton.setIcon(new ImageIcon(colorImage));
            lineStrokeButton.setIcon(new ImageIcon(lineThicknessImage));
            lineStyleButton.setIcon(new ImageIcon(lineStyleImage));

            // Sets button styles
            findMaxButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            findMaxButton.setHorizontalTextPosition(SwingConstants.CENTER);
            findMaxButton.setMargin(new Insets(2, 10, 2, 10));

            findZerosButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            findZerosButton.setHorizontalTextPosition(SwingConstants.CENTER);

            clearOverlaysButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            clearOverlaysButton.setHorizontalTextPosition(SwingConstants.CENTER);

            saveAsImageButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            saveAsImageButton.setHorizontalTextPosition(SwingConstants.CENTER);

            applyBounds.setVerticalTextPosition(SwingConstants.BOTTOM);
            applyBounds.setHorizontalTextPosition(SwingConstants.CENTER);

        } catch (IOException e) {

        }

        // Set default text
        equationField.setText(model.getFunction());

        boundMinX.setText(String.valueOf(model.getAreaBounds().getMinX()));
        boundMaxX.setText(String.valueOf(model.getAreaBounds().getMaxX()));
        boundMinY.setText(String.valueOf(model.getAreaBounds().getMinY()));
        boundMaxY.setText(String.valueOf(model.getAreaBounds().getMaxY()));

        xAxisScale.setText(String.valueOf(model.getAxisScaleX()));
        yAxisScale.setText(String.valueOf(model.getAxisScaleY()));
    }

    /**
     * Links controllers with view components.
     */
    public void registerControllers() {
        GraphFunctionController graphFunctionController = new GraphFunctionController(model, equationField);
        graphEquationButton.addActionListener(graphFunctionController);

        ExtremaButtonController extremaController = new ExtremaButtonController(model, findMaxButton);
        findMaxButton.addActionListener(extremaController);

        ZerosButtonController zerosController = new ZerosButtonController(model, findZerosButton);
        findZerosButton.addActionListener(zerosController);

        ClearOverlaysController clearOverlaysController = new ClearOverlaysController(model);
        clearOverlaysButton.addActionListener(clearOverlaysController);

        EngineOptionsController eoController = new EngineOptionsController(model);
        engineNative.addActionListener(eoController);
        engineJS.addActionListener(eoController);

        SaveImageController saveImgController = new SaveImageController(this, model);
        saveAsImageButton.addActionListener(saveImgController);

        ColorChooserController ccController = new ColorChooserController(this, model);
        colorChangeButton.addActionListener(ccController);

        LineThicknessController ltController = new LineThicknessController(this, model);
        lineStrokeButton.addActionListener(ltController);

        LineStyleController lsController = new LineStyleController(this, model);
        lineStyleButton.addActionListener(lsController);

        ApplyGraphSettingsController applyBoundsController = new ApplyGraphSettingsController(model, xAxisScale,
                yAxisScale, boundMinX, boundMaxX, boundMinY, boundMaxY, applyBounds);
        applyBounds.addActionListener(applyBoundsController);

        TableVisibilityController tableVisibilityController = new TableVisibilityController(model, showTableOfValues);
        showTableOfValues.addItemListener(tableVisibilityController);

        RadiansUnitController rUnitControllerX = new RadiansUnitController(model, xAxisRadians);
        RadiansUnitController rUnitControllerY = new RadiansUnitController(model, yAxisRadians);
        xAxisRadians.addItemListener(rUnitControllerX);
        yAxisRadians.addItemListener(rUnitControllerY);
        xAxisRadians.setName("x");
        yAxisRadians.setName("y");
    }

    /**
     * Creates selection controller to track mouse location.
     */
    private void createSelectionListener() {
        layeredPane.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                graphSelection.setSelection(e.getX());
            }
        });
    }

    /**
     * Removes selection listener
     */
    private void removeSelectionListener() {
        for (MouseMotionListener listener : layeredPane.getMouseMotionListeners()) {
            layeredPane.removeMouseMotionListener(listener);
        }
    }

    /**
     * Creates listener for extrema calc tool.
     */
    private void createExtremaListeners() {
        layeredPane.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                float[] result = (model.getExtremum(e.getX()));

                if (result != null) {
                    model.setGraphHighlightPoint(result[0], result[1]);
                    model.setState(GraphInfo.STATE_DISPLAY_EXTREMUM);
                } else {
                    model.setState(GraphInfo.STATE_EXTREMUM_FAIL);
                }

                deselectTools();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    /**
     * Creates listener for zeros calc tool.
     */
    private void createZerosListeners() {
        layeredPane.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                float[] result = (model.getZero(e.getX()));

                if (result != null) {
                    model.setGraphHighlightPoint(result[0], result[1]);
                    model.setState(GraphInfo.STATE_DISPLAY_ZERO);
                } else {
                    model.setState(GraphInfo.STATE_ZERO_FAIL);
                }

                deselectTools();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    /**
     * Removes all mouse listeners from graph layers
     */
    private void removeMouseListeners() {
        for (MouseListener listener : layeredPane.getMouseListeners()) {
            layeredPane.removeMouseListener(listener);
        }
    }

    /**
     * Updates the selection state of all tools.
     */
    public void updateToolsSelection() {
        if (model.isToolSelected()) {
            if (model.isExtremaToolSelected()) {
                createExtremaListeners();
            } else if (model.isZerosToolSelected()) {
                createZerosListeners();
            }
            createSelectionListener();
            graphSelection.setSelectionVisibility(true);
        } else {
            deselectTools();
        }
    }

    /**
     * Deselects all tools.
     */
    private void deselectTools() {
        removeMouseListeners();
        removeSelectionListener();
        model.deselectAllTools();
        graphSelection.setSelectionVisibility(false);
    }

    /**
     * Update graph info layer
     */
    public void updateInfo() {
        graphInfo.update();
    }

    /**
     * Updates visibility of table of values
     */
    public void updateTableVisibility() {
        if (model.isTableValuesVisible()) {
            tableOfValues.setVisible(true);
        } else {
            tableOfValues.setVisible(false);
        }
    }

    /**
     * Updates all graph layers
     */
    public void updateAll() {
        graphArea.update();
        graphInfo.update();
        tableModel.update();
    }
}
