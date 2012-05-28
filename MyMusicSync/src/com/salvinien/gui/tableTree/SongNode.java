package com.salvinien.gui.tableTree;

import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import com.salvinien.discography.Song;
import com.salvinien.synclists.Synclist;
import com.salvinien.utils.Converter;

public class SongNode extends ADefaultMutableTreeNode
{
	private static final long	serialVersionUID	= 1586769874979868677L;
	
	protected Song theSong;
	
	public SongNode(String aSongName)	{super( aSongName);}
	public SongNode(Song aSong)	
	{	
		super( aSong.getName());
		theSong = aSong;
	}

	public String getTypeName()	{ return "Song";}
	
	public void getSongIds( Vector<Integer> v)
	{
		v.add( theSong.getId());
	}

	

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
	public void removeNode(ADefaultMutableTreeNode aNode)
	{
		// do Nothing since it is the leaf
	}



}
