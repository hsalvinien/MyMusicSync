package com.salvinien.gui.tableTree;

import java.util.HashMap;

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
		//if there is no artist then it we have to remove the playlist, which means we have to remove the association with the device and no more.
		if( this.getChildCount()==0)
		{
			System.out.println( " SyncLIstNode => todo : remove the association between the playlist and the device");
		}
		else
		{
			//1) we remove the songs from the synclist
			//1.a we retreive all the AlbumNodeS in an array
			ArtistNode ta[] = new ArtistNode[this.getChildCount()];
			this.children.copyInto(ta);
			
			for( int i=0; i< ta.length; i++ )
			{
				ta[i].removeMe();
			}
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
		  if( containerArtist.size()==0)
		  {
		  }
		  
	}
	
}

