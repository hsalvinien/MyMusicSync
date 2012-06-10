package com.salvinien.gui.tableTree;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;



/**
 * @class: ADefaultMutableTreeNode
 * 
 * This class is the base class to manage a nodes 
 * 
 *   the complete hierarchy is:
 *   
 *   (SynclistNode or MusicSyncNode)  contains ArtistNode(s) which contains AlbumNode(s) which contains SongNodes
 *  the synclistNode is for synclists
 *  MusicSyncNode is for a library
 * 
 */

public abstract class ADefaultMutableTreeNode extends DefaultMutableTreeNode
{
	
	private static final long	serialVersionUID	= -3885663226595474571L;
	
	//Ctor
	public ADefaultMutableTreeNode( String aString) { super( aString);}	
	
	
	
	/**
	 * @method : void getSongIds( Vector<Integer> v)
	 * 
	 *
	 *	returns a vector of all song ids from the children nodes
	 *
	 *  it creates it if it doesn't exist
	 * 
	 */ 
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
