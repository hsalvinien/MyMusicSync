package com.salvinien.gui.tableSync;

import java.awt.Dimension;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/*
 * @class: TableSync
 * 
 * this class manages a table of sync on which the user has to tell what to do, the system being unable to decide by itself
 * it extends a JTable and its associated model
 */

public class TableSync extends JTable
{
	private static final long	serialVersionUID	= -7096672333942522731L;


	
	//CTOR
	public TableSync( TableSyncModel aTableModel) 
	{
		//set the model
		 setModel(aTableModel);
		 
	     //column size
	     for(int i=0; i< aTableModel.getColumnCount(); i++ )
	     {
	    	 
	 	    TableColumn column = this.getColumnModel().getColumn(i);
	    	 
	 		//fixes the size of some columns 
	    	 if( aTableModel.isColumnFixedSize(i))
	    	 {
				int width = aTableModel.ColumnSize(i);
				column.setPreferredWidth(width);	//fixes the column size
				column.setMaxWidth(width);
				column.setWidth(width);
	    		 
	    	 }
	    	 
	    	 //and align the display in the column
	    	 //@todo
	     }
	     
	     //set the editor of the imageIcon box to a JChecBox editor, in order to have simple click to change the values
	     getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JCheckBox()));
	     getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()));
	     getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JCheckBox()));
	     
	     
	     
		 setShowGrid(false); //don't show borders around cells
		 setIntercellSpacing(new Dimension(0, 0));//no space around cells
		 
		 //set actions for a click on a column
		 JTableHeader header = this.getTableHeader();
		 header.setUpdateTableInRealTime(true);
		 header.addMouseListener(new ColumnListener(this));
		 header.setReorderingAllowed(true);
	}	
	

}
