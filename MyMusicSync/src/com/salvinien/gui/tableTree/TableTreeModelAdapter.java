package com.salvinien.gui.tableTree;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.AbstractTableModel;


/**
 * @class: TableTreeModelAdapter  
 * 
 * this is the tableTreeModel for the root library (left size of the screen)
 * 
 * 
 */
public class TableTreeModelAdapter extends AbstractTableModel
{
	private static final long	serialVersionUID	= 966602946992519024L;
	JTree theTree;
    TableTreeModel theTableTreeModel;

    //CTOR
    public TableTreeModelAdapter(TableTreeModel aTableTreeModel, JTree aTree) 
    {
    	theTree = aTree;
        theTableTreeModel = aTableTreeModel;

        theTree.addTreeExpansionListener(new TreeExpansionListener() 
		{
		    // Don't use fireTableRowsInserted() here; 
		    // the selection model would get  updated twice. 
		    public void treeExpanded(TreeExpansionEvent event) 	{ fireTableDataChanged();}
	        public void treeCollapsed(TreeExpansionEvent event) { fireTableDataChanged();}
		});
    }

  // Wrappers, implementing TableModel interface. 
    //ACCESSORS
    public int getColumnCount() 								{ return theTableTreeModel.getColumnCount();}
    public int getRowCount() 									{ return theTree.getRowCount();} 
    public String getColumnName(int column) 					{ return theTableTreeModel.getColumnName(column);}
    public Class<?> getColumnClass(int column) 					{ return theTableTreeModel.getColumnClass(column);}
    protected Object nodeForRow(int row) 						{ return theTree.getPathForRow(row).getLastPathComponent();}
    public Object getValueAt(int row, int column) 				{ return theTableTreeModel.getValueAt(nodeForRow(row), column);}
    public boolean isCellEditable(int row, int column) 			{ return theTableTreeModel.isCellEditable(nodeForRow(row), column);}
    public void setValueAt(Object value, int row, int column) 	{ theTableTreeModel.setValueAt(value, nodeForRow(row), column);}
}


