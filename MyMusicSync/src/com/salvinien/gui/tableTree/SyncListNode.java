package com.salvinien.gui.tableTree;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import com.salvinien.discography.Song;
import com.salvinien.synclists.Synclist;

public class SyncListNode extends ADefaultMutableTreeNode
{
	private static final long	serialVersionUID	= 1329348084639690276L;
	
	protected HashMap<Integer, ArtistNode> containerArtist;
	protected Synclist theSyncList;
	
	
	public SyncListNode(Synclist aSyncList)	
	{
		super( aSyncList.getName());
		containerArtist = new HashMap<Integer, ArtistNode> ();
		theSyncList = aSyncList;
	}
	
	
	public 	Synclist getSynclist() { return theSyncList;}

	public ArtistNode getArtist(Song aSong)
	{
		int artistId = aSong.getArtistID();
		ArtistNode anArtistNode =containerArtist.get(artistId);
		if( anArtistNode==null)
		{
			anArtistNode = new ArtistNode( artistId);
			containerArtist.put( artistId, anArtistNode);
			this.add( anArtistNode);
		}
		
		
		anArtistNode.getAlbum(aSong);
		
		
		return anArtistNode;
	}
	
	public String getTypeName()	{ return "SyncList";}


	public Object getValueAt(int column)
	{
	    switch(column) 
	    {
		    case 0:	return this.getUserObject();
		    case 1: 
		    	return " ";

		    case 2: return " ";

		    default:
		    case 3: return " ";
	    }

	}



	@Override
	public void removeMe()
	{
		//0) if no artist, then do nothing
		if( this.getChildCount()==0) return;
		
		//1) we remove the songs from the synclist
		//1.a we retreive all the AlbumNodeS in an array
		ArtistNode ta[] = new ArtistNode[this.getChildCount()];
		this.children.copyInto(ta);
		
		for( int i=0; i< ta.length; i++ )
		{
			ta[i].removeMe();
		}
	
		//2) we remove the ArtistNode from the SyncList 
		//no need to do anyThing since the last ta[i].removeMe(); will implicitly call the removeNode of the ArtistNode.. 
	}



	public void removeNode(ADefaultMutableTreeNode aNode)
	{
		  int id= ((ArtistNode) aNode).theArtistId;
		  //remove the node from the container
		  containerArtist.remove((Integer) id);
		  //remove the node from the node
		  this.remove(aNode);
		  
		  //now if there is no more artist for the SyncList ...  we do nothing we keep the synclist
		  //to remove the node, we hae to call removeMeCompletly
		  if( containerArtist.size()==0)
		  {
		  }
		  
	}
	
	public void removeMeCompletly()
	{
		//! this is for removing a synclist from the device SO we reomve only the assoication not the songs from the synclist 
		DefaultMutableTreeNode mum = (DefaultMutableTreeNode) this.getParent();
		mum.remove( this);		  
	}


}

