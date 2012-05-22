package com.salvinien.mymusicsync;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;


import com.salvinien.database.MyDatabase;
import com.salvinien.database.dataModel;
import com.salvinien.discography.AlbumContainer;
import com.salvinien.discography.ArtistContainer;
import com.salvinien.discography.FileSongContainer;
import com.salvinien.discography.Song;
import com.salvinien.discography.SongContainer;
import com.salvinien.fileSystem.FsDir;
import com.salvinien.synclists.SynclistContainer;
import com.salvinien.synclists.SynclistNamesContainer;
import com.salvinien.gui.*;


public class startPoint 
{

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, SQLException 
	{
	
	
		
/*
	      System.out.println("File system roots returned by   FileSystemView.getFileSystemView():");
	      FileSystemView fsv = FileSystemView.getFileSystemView();
	      File[] roots = fsv.getRoots();
	      for (int i = 0; i < roots.length; i++)
	      {
	        System.out.println("Root: " + roots[i]);
	      }

	      System.out.println("Home directory: " + fsv.getHomeDirectory());

	      System.out.println("File system roots returned by File.listRoots():");

	      File[] f = File.listRoots();
	      for (int i = 0; i < f.length; i++)
	      {
	        System.out.println("Drive: " + f[i]);
	        System.out.println("Display name: " + fsv.getSystemDisplayName(f[i]));
	        System.out.println("Is drive: " + fsv.isDrive(f[i]));
	        System.out.println("Is floppy: " + fsv.isFloppyDrive(f[i]));
	        System.out.println("Readable: " + f[i].canRead());
	        System.out.println("Writable: " + f[i].canWrite());
	      }
*/
		
		
		
		//1) connection to database
		//1.1) open database. If database doesn't exists, create it
		MyDatabase.getSingleton();
		
		//1.2) if database has just been created then init the database with the needed tables
		dataModel.getSingleton().checkDatabase();
		
		//2) load parameters from database
		//2.1) Music root Dir
		Parameters.getSingleton();
		
		//2.2) Load all albums, artists, playlistnames
		AlbumContainer.getSingleton();
		ArtistContainer.getSingleton();
		SynclistNamesContainer.getSingleton();
		SynclistContainer.getSingleton();
		DeviceContainer.getSingleton();
		
		//3) load the file list from database => fld
		SongContainer.getSingleton();
		
		//4) load the file list from the hd  => flhd		
		FsDir r = new FsDir( Parameters.getSingleton().getRoot(),"");
		r.loadChild();
		FileSongContainer vSongs2 = new FileSongContainer( r);
		HashMap<String, Song> vSongs = vSongs2.getContainerFileName();
		
		System.out.println("Nb Elt : " + String.valueOf(vSongs.size()));
		
		//5) compare fld & flhd
		//5.1) the files which are in the database and are no more in the filesystem are deleted from Database
		int nbRemovedSongs = SongContainer.getSingleton().getRemovedSongs( vSongs);
		System.out.println("Nb Elt : " + String.valueOf(vSongs.size())+"  removed songs : " + nbRemovedSongs);
		
		//5.2) the files which are in the fs and not in the database are added to database and removed from vSongs
		int nbNewSongs = SongContainer.getSingleton().getNewSongs( vSongs);
		System.out.println("Nb Elt : " + String.valueOf(vSongs.size())+"  new songs : " + nbNewSongs);
		
		//5.3) the files which are both in database and FS, do they have to be updated?
		int nbUpdatedSongs = SongContainer.getSingleton().getUpdatedSongs( vSongs);
		System.out.println("Nb Elt : " + String.valueOf(vSongs.size())+"  updated songs : " + nbUpdatedSongs);
		
		
		
		//6) load the managed device list
		
		//7) display
		// Create an instance of the test application
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
            	myApp app= new myApp();
                app.createAndShowGUI();
            }
        });
		
	}

}
