package com.salvinien.discography;

import java.util.HashMap;

import com.salvinien.fileSystem.FsDir;
import com.salvinien.fileSystem.FsElement;
import com.salvinien.fileSystem.FsFile;

public class FileSongContainer
{
	protected HashMap< String, Song> containerFileName;

	//CTOR
	public FileSongContainer( FsDir root) 
	{
		//creates the physical containers
		containerFileName = new HashMap< String, Song>();
		
		loadFromFS( root);
	}


	//ACCESSORS
	public HashMap< String, Song> getContainerFileName() { return containerFileName;}

	
	
	//METHODS
	protected void loadFromFS( FsDir root)
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
	
	public void loadFromFS( FsFile aFile)
	{
		Song aSong = new Song ( aFile);
		
		containerFileName.put(aSong.getFileName(), aSong);
	}

}
