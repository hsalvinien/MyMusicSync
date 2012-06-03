package com.salvinien.gui.tableSync;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;



/*
 * @class: ColumnListener
 * 
 * This class manages the click on the column header on a TableSync
 * as we want want to manage only the click we have really implemented only void mouseClicked(MouseEvent e) 
 *
 */
public class ColumnListener implements MouseListener
{
	//a reference to the table
    protected JTable table;

    //CTOR
    public ColumnListener(JTable t)	{ table = t;}

    
	//METHODS
	/*@method : void mouseClicked(MouseEvent e)
	 * 
	 * the click event
	 * 
	 * this method has to exist since it is required by the MouseListner
	 * we really implement it, as it is this specific event we are waitting for  
	 * 
	 */
    public void mouseClicked(MouseEvent e) 
    {
    	//first what is the column which was clicked
      TableColumnModel colModel = table.getColumnModel();
      int column = colModel.getColumnIndexAtX(e.getX());

      
      //we are intested only in column 3/4/5
      if( (column <3) || ( column>5) )  return;
      
      //for each row of the column we set a new value
      for( int i=0; i <table.getRowCount(); i++ )
      {
    	  table.getModel().setValueAt(new Boolean(true), i, column); //actually for theses columns, which are Jcheckbox, we could set any value, as we are only interesd in a click 
      }
      
      
    }


	public void mouseEntered(MouseEvent arg0)	{} // to implement if needed
	public void mouseExited(MouseEvent arg0)	{} // to implement if needed
	public void mousePressed(MouseEvent arg0)	{} // to implement if needed
	public void mouseReleased(MouseEvent arg0)	{} // to implement if needed

}
