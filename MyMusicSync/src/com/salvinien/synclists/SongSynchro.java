package com.salvinien.synclists;

import java.util.Vector;
/*
 * @class: SongSynchro
 * 
 * This class manages the song synchro between the root and the device  
 
 * 
 */

import com.salvinien.discography.AlbumContainer;
import com.salvinien.discography.ArtistContainer;
import com.salvinien.discography.Song;

public class SongSynchro
{
	protected Song SongRoot;   	//song which is in the root (can be null)
	protected Song SongDevice;  //song which is in the device (can be null)
	boolean doNothing;
	boolean From2To;
	DeviceSyncList theDeviceSyncList;
	
	boolean ModifySyncList;
	
	//Ctor
	public SongSynchro(Song aSongRoot, boolean aFrom2To, boolean aDoNothing, Song aSongDevice, boolean aModiSyncList, DeviceSyncList aDeviceSyncList)
	{
		doNothing=aDoNothing;
		From2To=aFrom2To;
		ModifySyncList=false;
		SongRoot = aSongRoot;
		SongDevice = aSongDevice;
		
		theDeviceSyncList= aDeviceSyncList;
		
	}

	public boolean IshouldDoNothing()			{ return  doNothing;}
	public void  IshouldDoNothing( boolean b)	{ doNothing=b;}
	public boolean isFrom()						{ return  From2To;}
	public void isFrom(boolean b)				{ From2To=b;}
	public boolean isTo()						{ return  !From2To;}
	public void    isTo(boolean b)				{ From2To =!b;}
	public boolean shouldImodifySyncList()		{ return  ModifySyncList;}
	public void shouldImodifySyncList( boolean b)	{ ModifySyncList=b;}
	public Song getSongDevice() 				{ return SongDevice;}
	public Song getSongRoot() 					{ return SongRoot;}
	public DeviceSyncList getDeviceSyncList() 	{ return theDeviceSyncList;}
	
	//Methods
	/*@method : String Artist()
	 * returns the artist, the song in the root has priority 
	 */
	public String Artist()
	{
		String Artist;
		if( SongRoot!=null) 
		{
			Artist = ArtistContainer.getSingleton().getName( SongRoot.getArtistID());
		}
		else
		{
			Artist = ArtistContainer.getSingleton().getName( SongDevice.getArtistID());			
		}
		
		return Artist;
	}
	

	//Methods
	/*@method : String Album()
	 * returns the album, the song in the root has priority 
	 */
	public String Album()
	{
		String Album;
		if( SongRoot!=null) 
		{
			Album = AlbumContainer.getSingleton().getName( SongRoot.getAlbumID());
		}
		else
		{
			Album = AlbumContainer.getSingleton().getName( SongDevice.getAlbumID());			
		}

		return Album;
	}
	
	
	//Methods
	/*@method : String NameSource()
	 * returns the song name fromn the source, if null, it returns a default value 
	 */
	public String NameSource()
	{
		String NameSource;
		if( SongRoot!=null) 
		{
			NameSource = SongRoot.getName();
		}
		else
		{
			NameSource = "==null==";			
		}
		
		return NameSource;
	}

	//Methods
	/*@method : String NameTarget()
	 * returns the song name fromn the target, if null, it returns a default value 
	 */
	public String NameTarget()
	{
		String NameTarget;
		if( SongDevice!=null) 
		{
			NameTarget = SongDevice.getName();
		}
		else
		{
			NameTarget = "==null==";			
		}

		return NameTarget;
	}

	
	
	
	//Methods
	/*@method : Vector<String> toVstring()
	 * returns a vector of strings, these are the values we want to display in a row  
	 */

	public Vector<String> toVstring()
	{
		Vector<String> v = new Vector<String>();
		
		
		v.add(Artist());
		
		v.add(Album());
		
		v.add(NameSource());
		
		
		String sDoNothing;
		String sActionFrom;
		String sActionTo;
		if( doNothing)
		{
			sDoNothing = "do Nothing";
			sActionFrom =" ";
			sActionTo =" ";
		}
		else
		{
			sDoNothing = " ";
			if( From2To)
			{
				sActionFrom =" ";
				sActionTo ="=>";
			}
			else
			{
				sActionFrom ="<=";
				sActionTo ="  ";
			}
		}
		v.add(sActionFrom);
		v.add(sDoNothing);
		v.add(sActionTo);

		
		
		
		v.add(NameTarget());

		
		if(ModifySyncList)
		{
			v.add("Y");
		}
		else
		{
			v.add("N");
		}
		
		
		return v;
	}
	
}
