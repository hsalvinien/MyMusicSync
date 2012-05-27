package com.salvinien.discography;

import java.util.HashMap;

import com.salvinien.fileSystem.FsDir;
import com.salvinien.fileSystem.FsElement;
import com.salvinien.fileSystem.FsFile;


/*
 * @class: FileSongContainer
 * 
 * This class manages a container of songs (by their filenames)
 
 * 
 */

public class FileSongContainer
{
	protected HashMap< String, Song> containerFileName;

	//CTOR
	public FileSongContainer( FsDir root) 
	{
		//creates the physical containers
		containerFileName = new HashMap< String, Song>();
		
		loadFromFS( root);//populate the container form a FsDir ( a tree of prelaoded files freom the fileSystem) 
	}


	//ACCESSORS
	public HashMap< String, Song> getContainerFileName() { return containerFileName;}

	
	
	//METHODS
	
	//Methods
	/*@method : loadFromFS( FsDir root)
	 * loads the  container from the FsDir tree, it uses recursivity, to go through the tree
	 */
	private void loadFromFS( FsDir root)
	{
		for( int i = 0; i< root.size(); i++)
		{
			FsElement e = root.getElt(i);
			if( e.isDirectory())
			{
				loadFromFS( (FsDir) e);
			}
			else
			{
				loadFromFS( (FsFile) e);				
			}
		}
	}
	
	/*@method : loadFromFS( FsFile root)
	 * loads the  container from a FSFile, that the leaf of the FsDir tree recursive exploration
	 */
	private void loadFromFS( FsFile aFile)
	{
		Song aSong = new Song ( aFile); // creates a song form a file
		
		containerFileName.put(aSong.getFileName(), aSong); //add the song to the container
	}

}
