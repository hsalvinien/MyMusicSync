package com.salvinien.discography;

import java.util.Vector;

public class SongSynchro
{
	protected Song SongRoot;   //song which is in the root (can be null)
	protected Song SongDevice;	   //song which is in the device (can be null)
	boolean doNothing;
	boolean From2To;
	
	boolean ModifySyncList;
	
	public SongSynchro(Song aSongRoot, boolean aFrom2To, boolean aDoNothing, Song aSongDevice, boolean aModiSyncList)
	{
		doNothing=aDoNothing;
		From2To=aFrom2To;
		ModifySyncList=false;
		SongRoot = aSongRoot;
		SongDevice = aSongDevice;
	}
	
	
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
