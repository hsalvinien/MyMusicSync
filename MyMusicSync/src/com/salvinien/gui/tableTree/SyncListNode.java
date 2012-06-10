package com.salvinien.gui.tableTree;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import com.salvinien.discography.Song;
import com.salvinien.synclists.Synclist;


/**
 * @class: SyncListNode
 * 
 * This class manages a node corresponding to a syncList
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

public class SyncListNode extends ADefaultMutableTreeNode
{
	private static final long	serialVersionUID	= 1329348084639690276L;
	
	protected HashMap<Integer, ArtistNode> containerArtist;
	protected Synclist theSyncList;
	

	//CTOR
	public SyncListNode(Synclist aSyncList)	
	{
		super( aSyncList.getName());
		containerArtist = new HashMap<Integer, ArtistNode> ();
		theSyncList = aSyncList;
	}
	
	//ACCESSORS
	public 	Synclist getSynclist() { return theSyncList;}
	public String getTypeName()	{ return "SyncList";}


	
	
	/**
	 * @method : ArtistNode getArtist( Song aSong)
	 * 
	 *
	 *	returns an ArtistNode from the MusicNode depending aSong
	 *
	 *  it creates it if it doesn't exist
	 * 
	 */ 
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
	

	/**
	 * @method : Object getValueAt(int column)
	 * 
	 *
	 *returns the infos which has to displayed at this specifc column 
	 * 
	 */ 
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



	/**
	 * @method : void removeMe()
	 * 
	 *
	 *	remove a synclist,   
	 * 
	 */ 
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



	/**
	 * @method : removeNode(ADefaultMutableTreeNode aNode)
	 * 
	 *
	 *	remove all nodes form the node synclist,  
	 * 
	 */ 
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
	
	/**
	 * @method : removeMeCompletly()
	 * 
	 *
	 *	remove the  SyncListnode   
	 * 
	 */ 
	public void removeMeCompletly()
	{
		//! this is for removing a synclist from the device SO we reomve only the assoication not the songs from the synclist 
		DefaultMutableTreeNode mum = (DefaultMutableTreeNode) this.getParent();
		mum.remove( this);		  
	}


}

