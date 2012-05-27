package com.salvinien.synclists;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.salvinien.discography.Song;
import com.salvinien.discography.SongContainer;

/*
 * @class: Synclist
 * 
 * This class manages a synclist
 * 
 * 
 */

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
	public int getSize() 				{ return theContainer.size();}
	public int getId() 					{ return id;}
	public String getName() 			{ return SynclistNamesContainer.getSingleton().getName(id);}
	public void addSong( int anId)		{ theContainer.put( anId, anId);}
	public Iterator<Integer> iterator()	{ return theContainer.values().iterator();}
	public void removeSong( int anId)	{ theContainer.remove(anId); }

	/*@method : boolean hasSong( int aSongId)
	 * check if a song is in the playlist
	 */
	public boolean hasSong( int aSongId)
	{
		Integer I = theContainer.get(aSongId);
		if( I==null) return false;
		
		return true;
	}
	
	/*@method : boolean hasSong( Song aSong)
	 * check if a song is in the playlist
	 */
	public boolean hasSong( Song aSong)	
	{
		//1) is the song a song in the database rq: if not, it cannot be in the playlist!!!
		int id = SongContainer.getSingleton().getSongByFileName( aSong.getFileName());
		
		return hasSong(id);
	}

	
	
	/*@method : Vector<Song> getSongNotInDevice( HashMap<String, Song> SongsInTheDevice)
	 * 
	 * SongsInTheDevice contains the song in the device
	 * we are looking for the songs that should be synchronized (so in the synclist) which are not in the device
	 * 
	 * it returns a vector of songs which are not in the device
	 */
	public Vector<Song> getSongNotInDevice( HashMap<String, Song> SongsInTheDevice)
	{
		Vector<Song> vSongInRootNotInDevice = new Vector<Song>();// create the vector that will contain the results
		
		Iterator< Integer> it= theContainer.values().iterator(); //iterator of songs of the synclist
		while( it.hasNext())
		{
			int idSong = it.next(); //so next song of the synclist
			
			Song aSong = SongContainer.getSingleton().getSong(idSong);  //get the song from the song container
			
			Song anotherSong= SongsInTheDevice.get(aSong.getFileName());	//get the song from tSongs (which are the songs in the device
			
			if( anotherSong==null)
			{//in this case the file is not in the device, so we add it to the results
				vSongInRootNotInDevice.add(aSong);
			}
			else
			{//the file is already in the device
				if( aSong.getLastModification().after(anotherSong.getLastModification()))
				{//OK the file is newer in root than in the device so we add it to the results
					vSongInRootNotInDevice.add(aSong);					
				}
			}
			
		}//end while
		
		
		return vSongInRootNotInDevice;
	}
	
	
	/*@method : void addAlbum( int albumId)
	 * add all songs of an album to the synclist
	 */
	public void addAlbum( int albumId)
	{
		//let's do it "bourrin"
		Vector<Integer> v = SongContainer.getSingleton().getSongByAlbum(albumId);
		
		addSongs( v);
		
	}



	/*@method : void addArtist( int artistId)
	 * add all songs of an artist to the synclist
	 */
	public void addArtist( int artistId)
	{
		//let's do it "bourrin"
		Vector<Integer> v = SongContainer.getSingleton().getSongByArtist(artistId);
		
		addSongs( v);
	}


	/*@method : addSongs( Vector<Integer> v)
	 * add all songs of vector to the synclist
	 */
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
