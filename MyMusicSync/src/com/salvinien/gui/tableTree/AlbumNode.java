package com.salvinien.gui.tableTree;

import java.util.HashMap;

import com.salvinien.discography.Song;


public class AlbumNode extends ADefaultMutableTreeNode
{
	private static final long	serialVersionUID	= 5139317989238918397L;

	protected HashMap<Integer, SongNode> container= new HashMap<Integer, SongNode>();
	
	public AlbumNode(String anAlbum)	
	{
		super( anAlbum);
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

	
}
