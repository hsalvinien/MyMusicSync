package com.salvinien.mymusicsync;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.salvinien.discography.FileSongContainer;
import com.salvinien.discography.Song;
import com.salvinien.discography.SongContainer;
import com.salvinien.fileSystem.FsDir;
import com.salvinien.playlists.Playlist;

public class syncTask
{
	protected Device theDevice;
	protected Playlist thePlaylist;
	
	//Ctors
	public syncTask( Device aDevice, Playlist aPlaylist)
	{
		theDevice = aDevice;
		thePlaylist = aPlaylist; 
	}
	
	
	//Accessors
	
	//Methods
	public void sync() 
	{
		//1) loads the song from the device file system
		HashMap<String, Song> deviceSongs = loadSongFromDevice();

		
		
		//2) are they Songs in the device which are not in the playslist?
		songInDeviceNotInPlaylist(deviceSongs);
	
	
	
	
	
		//3) are they songs in the playlist which are not in the devices or songs which newer in the root than in the device 
		songInPlaylistNotInDevice( deviceSongs); 

		
		
		//4) are they songs which are newer in the device than in the root, if it is the case we should update the root 
		songNewerInDeviceThanInPlaylist( deviceSongs) ;
	
	}



	protected HashMap<String, Song> loadSongFromDevice() 
	{
		FsDir rDevice = new FsDir( theDevice.getDefaultPath(),"");
		rDevice.loadChild();
			//hashmap of songs which are on the device
		HashMap<String, Song> deviceSongs = new FileSongContainer( rDevice).getContainerFileName();

		return deviceSongs;
	}



	protected void songInDeviceNotInPlaylist( HashMap<String, Song> deviceSongs) 
	{
		//are they Songs in the device which are not in the playslist two choices:
				//=> we remove them from the device
				//or we add them to the playlist and THEN copy them into root
		Vector<Song> vSongInDeviceNotInRoot = new Vector<Song>();
		Iterator<Song> it = deviceSongs.values().iterator();
		while( it.hasNext())
		{
			Song aSong = it.next();
			if( thePlaylist.hasSong(aSong) == false)
					vSongInDeviceNotInRoot.add(aSong);
		}
			//ok what do we do?
			//@TODO 
		System.out.println( "Nombre de Songs dans le device et pas dans la playlist:" +String.valueOf(vSongInDeviceNotInRoot.size()) );	
	}



	protected void songInPlaylistNotInDevice( HashMap<String, Song> deviceSongs) 
	{
		//are they songs in the playlist which are not in the devices or songs which newer in the root than in the device 
		//if yes we copy them from the root to the devices
		Vector<Song> vSongInRootNotInDevice = thePlaylist.getSongNotInDevice( deviceSongs);
		Iterator<Song> it = vSongInRootNotInDevice.iterator();
		while( it.hasNext())
		{
			Song aSong  = it.next();
			deviceSongs.put(aSong.getFileName(), aSong);			
			//COPY THE SONG from root to device
			try
			{
				aSong.copy(Parameters.getSingleton().getRoot(), theDevice.getDefaultPath());
			}
			catch (IOException e) { e.printStackTrace();}
		}
		
		
		System.out.println( "Nombre de Songs dans la playlist et pas dans le device (ou plus recente):" +String.valueOf(vSongInRootNotInDevice.size()) );
	}


	protected void songNewerInDeviceThanInPlaylist( HashMap<String, Song> deviceSongs) 
	{
		 
		//are they songs which are newer in the device than in the root, if it is the case we should update the root 
		//BUT some checks have to be done (hashkey/size) and we need user conifrmation)
		Vector<Song> vSongNewerInDevice = new Vector<Song>();
		Iterator<Song> it = deviceSongs.values().iterator();
		while( it.hasNext())
		{
			Song aSong = it.next();
			Song aSong1 = SongContainer.getSingleton().getSong( aSong.getFileName());
			if( aSong1 !=null)  
			{
				
				long l = Math.abs(aSong.getLastModification().getTime()-aSong1.getLastModification().getTime()); //sqlite date tronque a la seconde...
				if( l>1000)
				//if( aSong.getLastModification().after( aSong1.getLastModification()))
				{
					vSongNewerInDevice.add(aSong);
				}
			}
			else
			{
				//the song is in the device but note in the playlis
				//this case will no happen more when  songInDeviceNotInPlaylist is completed
				System.out.println( "the song is in the device but note in the playlis, this case will no happen more when  songInDeviceNotInPlaylist is completed : "+aSong.getFileName() );
			}
		}
		//ok what do we do?
		//@TODO
		System.out.println( "Nombre de Songs dans le device plus recente que dans le root:" +String.valueOf(vSongNewerInDevice.size()) );
	
	}


}
