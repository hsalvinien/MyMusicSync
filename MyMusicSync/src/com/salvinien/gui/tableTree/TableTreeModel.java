package com.salvinien.gui.tableTree;

import javax.swing.tree.TreeModel;

/**
 * @interface: TableTreeModel
 * 
 * This interface defines what the methods needs a tabletree
 * 	=> it extends the TreeModel for the methods from trees
 *  => it defines the methods needed by a tableModel
 *  
 *    for memory the model defines the relation between the raw data and the TableTree 
 * 
 */

public interface TableTreeModel extends TreeModel
{
    public int 		getColumnCount();
    public String 	getColumnName(int column);
    public boolean 	isCellEditable(Object node, int column);


    public Object getValueAt(Object node, int column);
    public void setValueAt(Object aValue, Object node, int column);

    
    public Class<?> getColumnClass(int column);

}
