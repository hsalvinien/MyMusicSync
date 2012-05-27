package com.salvinien.gui.tableTree;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class ADefaultMutableTreeNode extends DefaultMutableTreeNode
{
	
	private static final long	serialVersionUID	= -3885663226595474571L;
	
	//Ctor
	public ADefaultMutableTreeNode( String aString) { super( aString);}	
	
	
	
	public void getSongIds( Vector<Integer> v)
	{
		for( int i=0; i<getChildCount(); i++)
		{
			ADefaultMutableTreeNode aNode = (ADefaultMutableTreeNode) this.getChildAt(i);
			aNode.getSongIds(v);
		}
	}


	//to be implemented
	public abstract Object getValueAt(int column); 
	public abstract String getTypeName();
	public abstract void removeMe();
	public abstract void removeNode( ADefaultMutableTreeNode aNode);

}
