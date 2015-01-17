package tableOfValues;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.NumberFormat;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TableOfValues extends JPanel {
    private TableOfValuesModel model; // The model of the table
    private BorderLayout mainLayout = new BorderLayout(); // The main menu layout
    private JFormattedTextField minXTextField;// Default Minimum of the table
    private JFormattedTextField maxXTextField;//Default Maximum of the table
    private JFormattedTextField scaleTextField;// Defualt Scale of the table
    private JLabel maxLabel = new JLabel();// Label for the Max Text Field
    private JLabel minLabel = new JLabel();// Label for the Min Text Field
    private JLabel scaleLabel = new JLabel();// Label for the Scale Text Field
    private JButton applyBound = new JButton("Apply"); // Create the apply button
    private String[] columnNames = {" X ", " Y "};// The Column Name
    private Double[][] data; // The table data
    private DefaultTableModel tableModel; // Model for table
    private JTable table = new JTable();// creates the default data

    /* Constructor
     *@param the model
     */
    public TableOfValues(TableOfValuesModel newModel) {

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(tableModel);

        model = newModel;
        this.model.setGUI(this);


        this.tableLayout();
        this.registerController();
//        this.update();
    }

    /* Creates the Table Panel */
    public void tableLayout() {
        this.setLayout(mainLayout);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setPreferredSize(new Dimension(140, 140));

        GridBagLayout panelLayout = new GridBagLayout();
        JPanel btnPanel = new JPanel(panelLayout);

        NumberFormat numFormat = NumberFormat.getNumberInstance();
        numFormat.setGroupingUsed(false);

        minXTextField = new JFormattedTextField(numFormat);
        maxXTextField = new JFormattedTextField(numFormat);
        scaleTextField = new JFormattedTextField(numFormat);

        minXTextField.setText("-25");
        maxXTextField.setText("25");
        scaleTextField.setText("1");

        table.setRowSelectionAllowed(false);
        table.setDragEnabled(false);

        maxLabel.setText("Max:");
        minLabel.setText("Min:");
        scaleLabel.setText("Scale:");
        maxXTextField.setColumns(5);
        minXTextField.setColumns(5);
        scaleTextField.setColumns(5);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 3); // Padding

        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        btnPanel.add(minLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        btnPanel.add(minXTextField, c);

        c.gridx = 0;
        c.gridy = 1;
        btnPanel.add(maxLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        btnPanel.add(maxXTextField, c);

        c.gridx = 0;
        c.gridy = 2;
        btnPanel.add(scaleLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        btnPanel.add(scaleTextField, c);

        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 3;
        btnPanel.add(applyBound, c);

        btnPanel.setBorder(new EmptyBorder(2, 4, 2, 4));

        this.add(btnPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);


        try {
            Image applyImage = ImageIO.read(getClass().getResource("../img/TableApply.png"));
            applyBound.setIcon(new ImageIcon(applyImage));

            applyBound.setVerticalTextPosition(SwingConstants.BOTTOM);
            applyBound.setHorizontalTextPosition(SwingConstants.CENTER);
        } catch (IOException e) {

        }
    }

    /*  Updates the table data */
    public void update() {
        data = model.getTableData();
        tableModel.setDataVector(data, columnNames);
        tableModel.fireTableDataChanged();
    }

    /* Apply the controller to the button */
    private void registerController() {
        ApplyTableBoundsController apply = new ApplyTableBoundsController(this.model, this.minXTextField, this.maxXTextField,
                this.scaleTextField, this.applyBound);
        this.applyBound.addActionListener(apply);
    }

}
 
     