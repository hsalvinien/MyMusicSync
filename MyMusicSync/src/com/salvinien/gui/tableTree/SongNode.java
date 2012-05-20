package com.salvinien.gui.tableTree;

import java.text.SimpleDateFormat;
import java.util.Vector;

import com.salvinien.discography.Song;
import com.salvinien.utils.Converter;

public class SongNode extends ADefaultMutableTreeNode
{
	private static final long	serialVersionUID	= 1586769874979868677L;
	
	private Song theSong;
	
	public SongNode(String aSongName)	{super( aSongName);}
	public SongNode(Song aSong)	
	{	
		super( aSong.getName());
		theSong = aSong;
	}

	public String getTypeName()	{ return "Song";}
	
	public void getSongIds( Vector<Integer> v)
	{
		v.add( theSong.getId());
	}

	

	public Object getValueAt(int column)
	{
	    switch(column) 
	    {
		    case 0:	return theSong.getName();
		    case 1: return Converter.int2StringKB( theSong.getSize());
		    case 2:
		    	String shk = Long.toHexString(theSong.getHashkey());
		    	return shk;

		    default:
		    case 3:
		    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd - hh:mm:ss");
		    	String sd= formatter.format(theSong.getLastModification());
		    	return sd;
	    }

	}



}
