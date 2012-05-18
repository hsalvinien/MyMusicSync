package com.salvinien.gui.tableTree;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class ADefaultMutableTreeNode extends DefaultMutableTreeNode
{
	
	private static final long	serialVersionUID	= -3885663226595474571L;
	
	//Ctor
	public ADefaultMutableTreeNode( String aString) { super( aString);}	
	
	//to be implemented
	public abstract Object getValueAt(int column); 

}
