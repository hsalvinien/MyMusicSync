package com.salvinien.gui.tableTree;

import java.util.HashMap;

import com.salvinien.discography.AlbumContainer;
import com.salvinien.discography.Song;

public class ArtistNode extends ADefaultMutableTreeNode
{
	private static final long	serialVersionUID	= 899997495513106967L;

	
	protected HashMap<Integer, AlbumNode> containerAlbum;
	
	
	
	public ArtistNode(String anArtist)	
	{
		super( anArtist);
		containerAlbum = new HashMap<Integer, AlbumNode> ();
	}
	

	public String getTypeName()	{ return "Artist";}

	public AlbumNode getAlbum(Song aSong)
	{
		int albumId = aSong.getAlbumID();
		AlbumNode anAlbumNode =containerAlbum.get(albumId);
		if( anAlbumNode==null)
		{
			anAlbumNode = new AlbumNode( AlbumContainer.getSingleton().getName(albumId));
			containerAlbum.put( albumId, anAlbumNode);
			this.add( anAlbumNode);
			
		}
		
		anAlbumNode.getSong(aSong);
		
		return anAlbumNode;
	}

	public Object getValueAt(int column)
	{
	    switch(column) 
	    {
		    case 0:	return this.getUserObject();
		    case 1: 
		    	int nbAlbum = this.getChildCount();
		    	int unkId = AlbumContainer.getSingleton().getUnknownId();
		    	Object obj =containerAlbum.get(unkId);
		    	if( obj!=null) nbAlbum--;
		    	
		    	String s;
		    	if( nbAlbum==1)
		    	{
		    		s =String.valueOf(nbAlbum)+" Album";
		    	}
		    	else if( nbAlbum>1)
		    	{
		    		s= String.valueOf(nbAlbum)+" Albums";
		    	}
		    	else
		    	{
		    		s= "Songs";
		    	}
		    	return s;

		    case 2: return " ";

		    default:
		    case 3: return " ";
	    }

	}

	
}

