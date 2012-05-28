package com.salvinien.gui.tableTree;

import java.util.HashMap;

import javax.swing.tree.TreeNode;

import com.salvinien.discography.AlbumContainer;
import com.salvinien.discography.Song;
import com.salvinien.synclists.Synclist;


public class AlbumNode extends ADefaultMutableTreeNode
{
	private static final long	serialVersionUID	= 5139317989238918397L;

	protected HashMap<Integer, SongNode> container= new HashMap<Integer, SongNode>();
	protected int theAlbumId;
	
	
	public AlbumNode(int albumId)	
	{
		super( AlbumContainer.getSingleton().getName(albumId));
		theAlbumId = albumId;
	}

	public Object getValueAt(int column)
	{
	    switch(column) 
	    {
		    case 0:	return this.getUserObject();
		    case 1: return String.valueOf(this.getChildCount())+" Songs";
		    case 2: return " ";

		    default:
		    case 3: return " ";
	    }

	}


	public SongNode getSong( Song aSong)
	{
		SongNode aSongNode =null;

		aSongNode = container.get( aSong.getId());
		if( aSongNode ==null)
		{
			aSongNode = new SongNode( aSong);
			container.put( aSong.getId(), aSongNode);
			this.add( aSongNode);
		}
		
		return aSongNode;
	}
	public String getTypeName()	{ return "Album";}

	
	
	
	public void removeMe()
	{
		//1) we remove the songs from the synclist
		//1-a) we retreive the synclist
		TreeNode tn[] = this.getPath();
		SyncListNode s = (SyncListNode) tn[1];  //root being the level 0, the synclist is level 1
		Synclist aSyncList = s.theSyncList;

		for( int i=0; i<this.getChildCount(); i++)
		{
			int id  = ((SongNode)this.getChildAt(i)).theSong.getId();
			aSyncList.removeSong(id);
		}
		//2) we remove the AlbumNode from the ArtistNode 
		ADefaultMutableTreeNode mum = (ADefaultMutableTreeNode) this.getParent();
		mum.removeNode( this);
	}

	public void removeNode(ADefaultMutableTreeNode aNode)
	{
	  Song aSong = ((SongNode) aNode).theSong;
	  //remove the node from the container
	  container.remove((Integer) aSong.getId());
	  //remove the node from the node
	  this.remove(aNode);
	  
	  //now if there is no more song in the album, it has to be removed
	  if( container.size()==0)
	  {
		ADefaultMutableTreeNode mum = (ADefaultMutableTreeNode) this.getParent();
		mum.removeNode( this);
	  }
	  
	}

	
}
