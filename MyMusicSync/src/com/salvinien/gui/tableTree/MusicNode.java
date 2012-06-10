package com.salvinien.gui.tableTree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import com.salvinien.discography.ArtistContainer;
import com.salvinien.discography.Song;
import com.salvinien.discography.SongContainer;

/**
 * @class: MusicNode
 * 
 * This class manages a node corresponding to a MusicNode
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

public class MusicNode extends ADefaultMutableTreeNode
{
	private static final long	serialVersionUID	= -5304314515831008863L;
	
	HashMap< Integer, ArtistNode> containerArtist = new HashMap< Integer, ArtistNode>();

	//CTOR
	public MusicNode( SongContainer aContainer)
	{
		super( "My Music");
		
      //pre-fill of the artists ordered by name
      String[] t = ArtistContainer.getSingleton().getLibelles();
      Arrays.sort(t);
      for( int i=0; i< t.length; i++)
      {
    	  getArtist( t[i]);
      }

      Iterator<Song>  it =   SongContainer.getSingleton().getSongIteratorID();
      while( it.hasNext())
      {
    	  Song aSong = it.next();
    	  
    	  getAlbum( aSong);
      }
  
	}

	
	//ACCESSORS
	public String getTypeName()	{ return "MusicNode";}
	
	
	//METHODS
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
		    case 1: return String.valueOf(this.getChildCount() )+" Artists";
		    case 2: return " ";

		    default:
		    case 3: return " ";
	    }

	}
	

	/**
	 * @method : AlbumNode getAlbum( Song aSong)
	 * 
	 *
	 *	returns an AlbumNode from the MusicNode depending on a Song
	 * 
	 */ 
	protected AlbumNode getAlbum( Song aSong)
	{
		
		ArtistNode anArtistNode = getArtist( aSong.getArtistID());
		
		AlbumNode anAlbumNode = anArtistNode.getAlbum(aSong);
		
		return anAlbumNode;
	}

	/**
	 * @method : ArtistNode getArtist( String artistName)
	 * 
	 *
	 *	returns an ArtistNode from the MusicNode depending the artist name

	 *  it creates it if it doesn't exist
	 * 
	 */ 
	protected ArtistNode getArtist( String artistName)
	{
		int artistId = ArtistContainer.getSingleton().getLibelle(artistName).getId();
		
		return getArtist( artistId);
	}
	
	/**
	 * @method : ArtistNode getArtist( int artistId)
	 * 
	 *
	 *	returns an ArtistNode from the MusicNode depending the int artistId
	 *
	 *  it creates it if it doesn't exist
	 * 
	 */ 
	protected ArtistNode getArtist( int artistId)
	{
		ArtistNode  aNodeArtist = containerArtist.get(artistId);
		
		if( aNodeArtist == null)
		{
			aNodeArtist = new ArtistNode( artistId);
			
			containerArtist.put(artistId, aNodeArtist);
			this.add( aNodeArtist);
		}
		
		return aNodeArtist;
	}

	
	
	

	/**
	 * @method : void removeMe()
	 * 
	 *
	 *	remove a MusicNode,  however ...that's not possible 
	 * 
	 */ 
	public void removeMe()
	{
		System.out.println( "MusicNode");		
	}

	/**
	 * @method : removeNode(ADefaultMutableTreeNode aNode)
	 * 
	 *
	 *	remove a node form the node list, however as we are in a MusicNode,...that's not possible 
	 * 
	 */ 
	public void removeNode(ADefaultMutableTreeNode aNode)
	{
	}		
	
}
