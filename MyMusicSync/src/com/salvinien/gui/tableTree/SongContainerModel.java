package com.salvinien.gui.tableTree;


import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;

public class SongContainerModel extends AbstractTableTreeModel  implements TableTreeModel 
{

    
    
    public SongContainerModel( DefaultMutableTreeNode aRoot) 
    { 	
    	super(aRoot);

        // Names of the columns.
        Titles = new String[]{"Name", "Size", "haskey", "Modified"};
        ColumnTypes = new Class<?>[]{TableTreeModel.class, String.class, String.class, String.class};

        FixeSize = new Boolean[] {false,true,true,true};  	// 1 column has not a fixed size, the 3 others have
        ColumnSize = new int[]  {-1, 70, 80, 140};  	// column size

        ColumnAlignement = new int[]{ SwingConstants.LEFT, SwingConstants.RIGHT, SwingConstants.RIGHT, SwingConstants.CENTER}; //= SwingConstants.CENTER;

    }


    

    //
    // The TreeModel interface
    //

    public boolean isRootVisible()	{ return true;}
        
    public Object getValueAt(Object node, int column) 
    {
    	ADefaultMutableTreeNode n= (ADefaultMutableTreeNode) node; 
		
	 
		return n.getValueAt(column); 
    }

    
}



