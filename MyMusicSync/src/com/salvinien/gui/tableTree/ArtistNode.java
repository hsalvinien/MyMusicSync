package com.salvinien.gui.tableTree;

import java.util.HashMap;


import com.salvinien.discography.AlbumContainer;
import com.salvinien.discography.ArtistContainer;
import com.salvinien.discography.Song;

public class ArtistNode extends ADefaultMutableTreeNode
{
	private static final long	serialVersionUID	= 899997495513106967L;

	
	protected HashMap<Integer, AlbumNode> containerAlbum;
	protected int theArtistId;
	
	
	public ArtistNode(int artistId)	
	{
		super( ArtistContainer.getSingleton().getName(artistId));
		containerAlbum = new HashMap<Integer, AlbumNode> ();
		theArtistId = artistId;
	}
	

	public String getTypeName()	{ return "Artist";}

	public AlbumNode getAlbum(Song aSong)
	{
		int albumId = aSong.getAlbumID();
		AlbumNode anAlbumNode =containerAlbum.get(albumId);
		if( anAlbumNode==null)
		{
			anAlbumNode = new AlbumNode( albumId);
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


	public void removeMe()
	{
		//1) we remove the songs from the synclist
		//1.a we retreive all the AlbumNodeS in an array
		AlbumNode ta[] = new AlbumNode[this.getChildCount()];
		this.children.copyInto(ta);
		
		for( int i=0; i< ta.length; i++ )
		{
			ta[i].removeMe();
		}
		
	
		//2) we remove the AlbumNode from the ArtistNode 
		//no need to do anyThing since the last ta[i].removeMe(); will implicitly call the removeNode of the ArtistNode.. 
	}


	public void removeNode(ADefaultMutableTreeNode aNode)
	{
		  int id= ((AlbumNode) aNode).theAlbumId;
		  //remove the node from the container
		  containerAlbum.remove((Integer) id);
		  //remove the node from the node
		  this.remove(aNode);
		  
		  //now if there is no more album for the Artist, it has to be removed
		  if( containerAlbum.size()==0)
		  {
			ADefaultMutableTreeNode mum = (ADefaultMutableTreeNode) this.getParent();
			mum.removeNode( this);
		  }
		  
	}

	
}

