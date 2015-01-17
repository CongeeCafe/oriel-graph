package tableOfValues;

import expressionEvaluator.EquationItem;
import expressionEvaluator.MathEngine;
import graph.Bounds;

/**
 * TableOfValuesModel
 * @author Danny Ngo
 */

public class TableOfValuesModel extends Object
{ 
  private Double [][] data;// Table data
  private double maxX = 25; // X Max Table value
  private double minX = -25;// X Min table Value
  private double scale = 1;// X scale of the table 
  private EquationItem equationValue; // equation
  private Bounds tableBounds;// table bounds
  private TableOfValues tableView;// Table view
  private MathEngine math; // math engine
  
  /* Constructer */ 
  public TableOfValuesModel(MathEngine math)
  {
    super();
    this.math = math;
  }
  /*  // Set a certain value of the table
   * @param tableNumber    Table data at that x value 
   * @param row            Row of the table
   * @param col            Column of the table
   * */ 
  public void setValueAt(double tableNumber,int row,int col)
  {
    data [row][col] = tableNumber;
  }
  
  /* Set the bounds
   * @param max    MaxX value
   * @param min    MinX value
   * */ 
  public void setBounds(double max, double min)
  {
    maxX=max;
    minX=min;
  }
  
  /*Set the scale 
   *@param scale     Set the scale 
   */ 
  public void setScale(double scale)
  {
    this.scale = scale;
  }
  /*return the Table graph.Bounds  */
  public Bounds getBounds()
  {
    return tableBounds;
  }
  
  /*returns the new data for the new table */ 
  public Double [][] getTableData()
  {
    data = math.evalExpression(minX,maxX,scale);
    return data; 
  }
  
  /*Sets the current GUI */ 
  public void setGUI(TableOfValues currentGUI)
  {
    this.tableView = currentGUI;
  }
  
  /* Updates the Table Model */ 
  public void update()
  {
    tableView.update();
  }     
}
