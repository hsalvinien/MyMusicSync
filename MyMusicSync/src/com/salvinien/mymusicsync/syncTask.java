package com.salvinien.mymusicsync;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.salvinien.discography.FileSongContainer;
import com.salvinien.discography.Song;
import com.salvinien.discography.SongContainer;
import com.salvinien.discography.SongSynchro;
import com.salvinien.discography.SongSynchroContainer;
import com.salvinien.fileSystem.FsDir;
import com.salvinien.synclists.Synclist;
import com.salvinien.synclists.SynclistContainer;

public class syncTask
{
	protected Synclist thePlaylist;
	protected DeviceSyncList theDeviceSyncList;
	
	//Ctors
	public syncTask( Device aDevice, DeviceSyncList aDeviceSyncList)
	{
		theDeviceSyncList = aDeviceSyncList;

		thePlaylist =  SynclistContainer.getSingleton().getPlaylist( aDeviceSyncList.getPlaylistId());
	}
	
	
	//Accessors
	
	//Methods
	public SongSynchroContainer sync(SongSynchroContainer aContainer) 
	{
		
		//1) loads the song from the device file system
		HashMap<String, Song> deviceSongs = loadSongFromDevice();

		//2) are they Songs in the device which are not in the synclist?
		songInDeviceNotInSyncList(deviceSongs, aContainer);
	
		//3) are they songs in the playlist which are not in the devices or songs which newer in the root than in the device 
		songInPlaylistNotInDevice( deviceSongs, aContainer); 

		//4) are they songs which are newer in the device than in the root, if it is the case we should update the root 
		songNewerInDeviceThanInPlaylist( deviceSongs, aContainer) ;
		
		return aContainer;
	}



	protected HashMap<String, Song> loadSongFromDevice() 
	{
		FsDir rDevice = new FsDir( theDeviceSyncList.getDefaultPath(),"");
		rDevice.loadChild();
			//hashmap of songs which are on the device
		HashMap<String, Song> deviceSongs = new FileSongContainer( rDevice).getContainerFileName();

		return deviceSongs;
	}



	protected void songInDeviceNotInSyncList( HashMap<String, Song> deviceSongs,  SongSynchroContainer aContainer)	
	{
		//are they Songs in the device which are not in the playslist two choices:
				//=> we remove them from the device
				//or we add them to the playlist and THEN copy them into root, well just check that it is not already in the root...

		Iterator<Song> it = deviceSongs.values().iterator();
		while( it.hasNext())
		{
			Song aSong = it.next();
			if( thePlaylist.hasSong(aSong) == false)
			{
				//so we have found a song in the device which is not in the syncList
				SongSynchro aSongSynchro = new SongSynchro(null, false, false, aSong, true);
				aContainer.add( aSongSynchro);
			}
		}
	}



	protected void songInPlaylistNotInDevice( HashMap<String, Song> deviceSongs,  SongSynchroContainer aContainer) 
	{
		//are they songs in the playlist which are not in the devices or songs which are newer in the root than in the device 
		//if yes we copy them from the root to the devices
		Vector<Song> vSongInRootNotInDevice = thePlaylist.getSongNotInDevice( deviceSongs);
		Iterator<Song> it = vSongInRootNotInDevice.iterator();
		while( it.hasNext())
		{
			Song aSong  = it.next();
			deviceSongs.put(aSong.getFileName(), aSong); //useless but cleaner
																
			SongSynchro aSongSynchro = new SongSynchro(aSong, true  , false, null, false); //this false
			aContainer.add( aSongSynchro);
		}
		
	}


	protected void songNewerInDeviceThanInPlaylist( HashMap<String, Song> deviceSongs,  SongSynchroContainer aContainer) 
	{
		 
		//are they songs which are newer in the device than in the root, if it is the case we should update the root 
		//BUT some checks have to be done (hashkey/size) and we need user conifrmation)
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
					SongSynchro aSongSynchro = new SongSynchro(aSong1, false, false, aSong, true);
					aContainer.add( aSongSynchro);
				}
			}
			else
			{
				//the song is in the device but note in the playlis
				//this case will no happen more when  songInDeviceNotInPlaylist is completed
				System.out.println( "the song is in the device but note in the playlis, this case will no happen more when  songInDeviceNotInPlaylist is completed : "+aSong.getFileName() );
			}
		}
	}


}
