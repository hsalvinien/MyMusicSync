package com.salvinien.synclists;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Vector;

import com.salvinien.discography.Song;
import com.salvinien.discography.SongContainer;
import com.salvinien.mymusicsync.Parameters;

import static java.nio.file.StandardCopyOption.*;


public class SongSynchroContainer
{

	private Vector<SongSynchro> container =	new Vector<SongSynchro>();
	
	
	
	//accessors
	public void add( SongSynchro aSongSynchro)	{container.add( aSongSynchro);}
	public Iterator<SongSynchro> iterator()		{return container.iterator();}	
	public int size() { return container.size();}
	public SongSynchro getElement( int i) 	{ return container.get(i);}
	
	
	
	
	
	public void synchronize()
	{
		Iterator<SongSynchro> it = iterator();
		while( it.hasNext())
		{
			SongSynchro s= it.next();
			
			if( s.IshouldDoNothing())  continue;
			if(s.isTo())
			{
				//case : the action goes from the device to the root
				try 						{ DeviceToRoot(s);}
				catch (IOException e)		{e.printStackTrace();}
			}
			else
			{
				RootToDevice(s);
				
			}
			
			
		}
		
	}
	
	
	
	private void DeviceToRoot(SongSynchro s) throws IOException
	{
		Song target = s.getSongRoot();
		Song source = s.getSongDevice();
		
		if( source==null)
		{
			//there is no source so it means that we have to delete the file from the librairy, container and database
			//we can use the target.getId since it is the song which is in the container it is a valid ID
			SongContainer.getSingleton().removeDeleteSong(target.getId());  
			
			//and now we have to remove the file from the synclists
			SynclistContainer.getSingleton().removeSongFromAllSynclist( target.getId());
		}
		else
		{
			//there is a source SO we have to copy the source into the target
			//! However the target may not exist
			String targetFileName=null;
			if( target != null)
			{
				// the target already exists, so we just get the complete filename
				targetFileName = target.getFileName();
			}
			else
			{
				//the target doesn't exist, so we have to recreate the complete filename from the source
				//1) get the file name
				targetFileName = source.getFileName();
				//2) remove the root of the librairy to keep the relative name
				targetFileName = targetFileName.substring(Parameters.getSingleton().getRoot().length());
				
				//3) add the the root of the device to create an absolute name
				targetFileName = s.getDeviceSyncList().getDefaultPath() + targetFileName; 
				
			}
			
			//////////may be should use song.copy
			File f= new File( source.getFileName());
			Files.copy(  Paths.get(source.getFileName()), Paths.get(targetFileName), REPLACE_EXISTING, COPY_ATTRIBUTES);

			//ajouter dans la base et dans les synclists
			
		}
		
	}
	
	private void RootToDevice(SongSynchro s)
	{
		Song source = s.getSongRoot();
		Song target = s.getSongDevice();

		if( source==null)
		{	//there is no source so we have to delete the target
			//his time, it is a file on a device that we have to delete, so nothing to remove from database/librairy or synclist
			File f = new File( s.getDeviceSyncList().getDefaultPath()+target.getFileName());
			f.delete(); 
		}
		else
		{
			//case : copy the song from root to device
			try						{ source.copy( Parameters.getSingleton().getRoot(), s.getDeviceSyncList().getDefaultPath());}
			catch (IOException e)	{ e.printStackTrace();}			
		}	
		
		
	}
	
	
}
