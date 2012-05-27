package com.salvinien.gui.tableTree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import com.salvinien.discography.ArtistContainer;
import com.salvinien.discography.Song;
import com.salvinien.discography.SongContainer;


public class MusicNode extends ADefaultMutableTreeNode
{
	private static final long	serialVersionUID	= -5304314515831008863L;
	
	HashMap< Integer, ArtistNode> containerArtist = new HashMap< Integer, ArtistNode>();
	
	
	
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
	
	public String getTypeName()	{ return "MusicNode";}

	
	protected AlbumNode getAlbum( Song aSong)
	{
		
		ArtistNode anArtistNode = getArtist( aSong.getArtistID());
		
		AlbumNode anAlbumNode = anArtistNode.getAlbum(aSong);
		
		return anAlbumNode;
	}

	protected ArtistNode getArtist( String artistName)
	{
		int artistId = ArtistContainer.getSingleton().getLibelle(artistName).getId();
		
		return getArtist( artistId);
	}
	
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

	@Override
	public void removeMe()
	{
		// TODO Auto-generated method stub
		System.out.println( "MusicNode");		
	}

	@Override
	public void removeNode(ADefaultMutableTreeNode aNode)
	{
		// TODO Auto-generated method stub
		
	}
		
	
}
