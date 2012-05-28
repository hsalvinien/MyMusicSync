package com.salvinien.synclists;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.salvinien.discography.FileSongContainer;
import com.salvinien.discography.Song;
import com.salvinien.discography.SongContainer;
import com.salvinien.fileSystem.FsDir;
import com.salvinien.mymusicsync.Device;
import com.salvinien.mymusicsync.DeviceSyncList;

/*
 * @class: syncTask
 * 
 * This class manages a the synchronization between the root lib uisng a synclist
 * 
 * 
 */

public class syncTask
{
	protected Synclist theSynclist;
	protected DeviceSyncList theDeviceSyncList;
	
	//Ctors
	public syncTask( Device aDevice, DeviceSyncList aDeviceSyncList)
	{
		theDeviceSyncList = aDeviceSyncList;

		theSynclist =  SynclistContainer.getSingleton().getSynclist( aDeviceSyncList.getSynclistId());
	}
	
	
	//Accessors
	
	//Methods
	/*@method : SongSynchroContainer sync(SongSynchroContainer aContainer)
	 * generate the synchro to be done
	 * 
	 * actually it adds the new sync to do to a previously filled container (in cas of several synclit for a single device) 
	 */
	public SongSynchroContainer sync(SongSynchroContainer aContainer) 
	{
		
		//1) loads the song from the device file system
		HashMap<String, Song> deviceSongs = loadSongFromDevice();

		//2) are they Songs in the device which are not in the synclist?
		songInDeviceNotInSyncList(deviceSongs, aContainer);
	
		//3) are they songs in the Synclist which are not in the devices or songs which newer in the root than in the device 
		songInSynclistNotInDevice( deviceSongs, aContainer); 

		//4) are they songs which are newer in the device than in the root, if it is the case we should update the root 
		songNewerInDeviceThanInSynclist( deviceSongs, aContainer) ;
		
		return aContainer;
	}



	/*@method : HashMap<String, Song> loadSongFromDevice()
	 * load songs from the device into an hashmap
	 */
	protected HashMap<String, Song> loadSongFromDevice() 
	{
		//1) load the files in a FsDir/FsFile tree
		FsDir rDevice = new FsDir( theDeviceSyncList.getDefaultPath(),"");
		rDevice.loadChild();
		
		//2) transform the tree in hashmap
			//hashmap of songs which are on the device
		HashMap<String, Song> deviceSongs = new FileSongContainer( rDevice).getContainerFileName();

		
		return deviceSongs;
	}


	/*@method : void songInDeviceNotInSyncList( HashMap<String, Song> deviceSongs,  SongSynchroContainer aContainer)
	 * list the songs on the device which are not in the synclist and add them in a SongSynchroContainer, in order to ask the user what he wants
	 * us to do either remove from the device or add it to the synclist and maybe add to the root lib. 
	 */
	protected void songInDeviceNotInSyncList( HashMap<String, Song> deviceSongs,  SongSynchroContainer aContainer)	
	{
		//are they Songs in the device which are not in the Syncslist two choices:
				//=> we remove them from the device
				//or we add them to the Synclist and THEN copy them into root, well just check that it is not already in the root...

		Iterator<Song> it = deviceSongs.values().iterator();
		while( it.hasNext())
		{
			Song aSong = it.next();
			if( theSynclist.hasSong(aSong) == false)
			{
				//so we have found a song in the device which is not in the syncList
				SongSynchro aSongSynchro = new SongSynchro(null, false, false, aSong, true);
				aContainer.add( aSongSynchro);
			}
		}
	}




	/*@method : songInSynclistNotInDevice( HashMap<String, Song> deviceSongs,  SongSynchroContainer aContainer)
	 * list the songs in the synclist which are not in the device and add them in a SongSynchroContainer
	 * in order to ask the user what he wants us to do :
	 * either remove from the it from the synclust and mey be from the root lib
	 * or copy it to the device 
	 */
	protected void songInSynclistNotInDevice( HashMap<String, Song> deviceSongs,  SongSynchroContainer aContainer) 
	{
		//are they songs in the Synclist which are not in the devices or songs which are newer in the root than in the device 
		//if yes we copy them from the root to the devices
		Vector<Song> vSongInRootNotInDevice = theSynclist.getSongNotInDevice( deviceSongs);
		Iterator<Song> it = vSongInRootNotInDevice.iterator();
		while( it.hasNext())
		{
			Song aSong  = it.next();
			deviceSongs.put(aSong.getFileName(), aSong); //useless but cleaner
																
			SongSynchro aSongSynchro = new SongSynchro(aSong, true  , false, null, false); //this false
			aContainer.add( aSongSynchro);
		}
		
	}


	/*@method : songNewerInDeviceThanInSynclist( HashMap<String, Song> deviceSongs,  SongSynchroContainer aContainer)
	 * list the songs newer in the device than in the root lib
	 * in order to ask the user what he wants us to do :
	 */
	protected void songNewerInDeviceThanInSynclist( HashMap<String, Song> deviceSongs,  SongSynchroContainer aContainer) 
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
				//the song is in the device but not in the Synclis
				//this case will no more  happen when  songInDeviceNotInSynclist is completed
				System.out.println( "the song is in the device but note in the Synclist, this case will no happen more when  songInDeviceNotInSynclist is completed : "+aSong.getFileName() );
			}
		}
	}


}
