package com.salvinien.gui.tableTree;

import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import com.salvinien.discography.Song;
import com.salvinien.synclists.Synclist;
import com.salvinien.utils.Converter;



/*
 * @class: SongNode
 * 
 * This class manages a node corresponding to a SongNode
 * 
 * it is an extrend of ADefaultMutableTreeNode
 * 
 *   
 *   the complete hierarchy is:
 *   
 *   (SynclistNode or MusicSyncNode)  contains ArtistNode(s) which contains AlbumNode(s) which contains SongNodes
 *  the synclistNode is for synclists
 *  MusicSyncNode is for a library
 * 
 */

public class SongNode extends ADefaultMutableTreeNode
{
	private static final long	serialVersionUID	= 1586769874979868677L;
	
	protected Song theSong;
	
	//CTORs
	public SongNode(String aSongName)	{super( aSongName);}
	public SongNode(Song aSong)	
	{	
		super( aSong.getName());
		theSong = aSong;
	}

	//ACCESSORS
	public String getTypeName()	{ return "Song";}	
	public void getSongIds( Vector<Integer> v)
	{
		v.add( theSong.getId());
	}

	

	//METHODS
	/*@method : Object getValueAt(int column)
	 * 
	 *
	 *returns the infos which has to displayed at this specifc column 
	 * 
	 */ 
	public Object getValueAt(int column)
	{
	    switch(column) 
	    {
		    case 0:	return theSong.getName();
		    case 1: return Converter.int2StringMB( theSong.getSize());
		    case 2:
		    	String shk = Long.toHexString(theSong.getHashkey());
		    	return shk;

		    default:
		    case 3:
		    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd - hh:mm:ss");
		    	String sd= formatter.format(theSong.getLastModification());
		    	return sd;
	    }

	}


	/*@method : void removeMe()
	 * 
	 *
	 *	remove a song from a synclist 
	 * 
	 */ 
	public void removeMe()
	{
		//1) we remove the song from the synclist
		//1-a) we retreive the synclist
		TreeNode tn[] = this.getPath();
		SyncListNode s = (SyncListNode) tn[1];  //root being the level 0, the synclist is level 1
		Synclist aSyncList = s.theSyncList;
		aSyncList.removeSong(theSong.getId());
		
		//2) we remove the songNode from the AlbumNode
		ADefaultMutableTreeNode mum = (ADefaultMutableTreeNode) this.getParent();
		mum.removeNode( this);
	}

	/*@method : removeNode(ADefaultMutableTreeNode aNode)
	 * 
	 *
	 *	remove a node form the node list, as it is a leaf...there is no children so we do nothing
	 * 
	 */ 
	public void removeNode(ADefaultMutableTreeNode aNode)
	{
		// do Nothing since it is the leaf
	}

}
