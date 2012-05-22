package com.salvinien.gui.tableTree;

import java.util.HashMap;

import com.salvinien.discography.ArtistContainer;
import com.salvinien.discography.Song;
import com.salvinien.synclists.Synclist;

public class SyncListNode extends ADefaultMutableTreeNode
{
	private static final long	serialVersionUID	= 1329348084639690276L;
	
	protected HashMap<Integer, ArtistNode> containerArtist;
	Synclist thePlayList;
	
	
	public SyncListNode(Synclist aPlayList)	
	{
		super( aPlayList.getName());
		containerArtist = new HashMap<Integer, ArtistNode> ();
		thePlayList = aPlayList;
	}
	

	
	public 	Synclist getSynclist() { return thePlayList;}

	public ArtistNode getArtist(Song aSong)
	{
		int artistId = aSong.getArtistID();
		ArtistNode anArtistNode =containerArtist.get(artistId);
		if( anArtistNode==null)
		{
			anArtistNode = new ArtistNode( ArtistContainer.getSingleton().getName(artistId));
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

	
}
