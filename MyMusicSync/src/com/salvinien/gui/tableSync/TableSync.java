package com.salvinien.gui.tableSync;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class TableSync extends JTable
{
	private static final long	serialVersionUID	= -7096672333942522731L;


	
	
	public TableSync( TableSyncModel aTableModel) 
	{
		super(aTableModel);
		
		

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

	     }
	     
	     //set the editor of the imageIcon box to a JChecBox editor, in order to have simple click to change the values
	     getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JCheckBox()));
	     getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()));
	     getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JCheckBox()));
	     
		 setShowGrid(false);
		 setIntercellSpacing(new Dimension(0, 0));
		 
		 
		 //set actions for a click on a column
		 JTableHeader header = this.getTableHeader();
		 header.setUpdateTableInRealTime(true);
		 header.addMouseListener(new ColumnListener(this));
		 header.setReorderingAllowed(true);
	}	
	
	
	

}
