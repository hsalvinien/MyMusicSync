package com.salvinien.synclists;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.salvinien.discography.Song;
import com.salvinien.discography.SongContainer;

public class Synclist
{
	//members
	protected int id; //id of the list
	protected HashMap<Integer, Integer> theContainer; //song ids 
	
	//CTOR
	public Synclist( int anId)  
	{ 
		id = anId;
		theContainer = new HashMap<Integer, Integer>();
	}
	
	//ACCESSORS
	public int getSize() { return theContainer.size();}
	public int getId() { return id;}
	public String getName() 
	{
		return SynclistNamesContainer.getSingleton().getName(id);
	}
	
	public void addSong( int anId)
	{
		theContainer.put( anId, anId);
	}
	public Iterator<Integer> iterator()
	{
		return theContainer.values().iterator();
	}
	
	public void removeSong( int anId)
	{
		theContainer.remove(anId);
	}
	
	public boolean hasSong( int aSongId)
	{
		Integer I = theContainer.get(aSongId);
		if( I==null) return false;
		
		return true;
	}
	
	public boolean hasSong( Song aSong)	
	{
		//1) is the song a song in the database rq: if not, it cannot be in the playlist!!!
		int id = SongContainer.getSingleton().getSongByFileName( aSong.getFileName());
		
		return hasSong(id);
	}

	
	
	public Vector<Song> getSongNotInDevice( HashMap<String, Song> tSongs)
	{
		Vector<Song> vSongInRootNotInDevice = new Vector<Song>();
		
		Iterator< Integer> it= theContainer.values().iterator();
		while( it.hasNext())
		{
			int idSong = it.next();
			
			Song aSong = SongContainer.getSingleton().getSong(idSong);
			
			Song anotherSong= tSongs.get(aSong.getFileName());
			
			if( anotherSong==null)
			{//in this case the file is not in the device
				vSongInRootNotInDevice.add(aSong);
			}
			else
			{//the file is already in the device
				if( aSong.getLastModification().after(anotherSong.getLastModification()))
				{//OK the file is newer in root than in the device
					vSongInRootNotInDevice.add(aSong);					
				}
			}
			
		}//end while
		
		
		return vSongInRootNotInDevice;
	}
	
	
	public void addAlbum( int albumId)
	{
		//let's do it "bourrin"
		Vector<Integer> v = SongContainer.getSingleton().getSongByAlbum(albumId);
		
		addSongs( v);
		
	}



	public void addArtist( int artistId)
	{
		//let's do it "bourrin"
		Vector<Integer> v = SongContainer.getSingleton().getSongByArtist(artistId);
		
		addSongs( v);
	}


	public void addSongs( Vector<Integer> v)
	{
		for( int i=0; i< v.size(); i++)
		{
			int songId = v.get(i);
			
			if( !this.hasSong(songId))
			{
				this.addSong(songId);
			}
		}
		
	}
	
}
