package com.salvinien.fileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Date;

import com.salvinien.discography.AlbumContainer;
import com.salvinien.discography.ArtistContainer;
import com.salvinien.discography.Song;


/**
 * @class: FsFile
 * 
 * This class is the leaf class to manage the song file in the file system 
 * 
 */
public class FsFile extends FsElement
{
	Song song;
	protected long size;
	protected int hashkey;
	protected Date lastModification;
	
	
	//Ctor
	public FsFile( String aMountPoint, String aFullPath) {super(aMountPoint, aFullPath);}
	public FsFile( String aMountPoint, File aFile)	
	{ 
		super( aMountPoint, aFile);
		try
		{ 
			//retrieve information fromn the files
			BasicFileAttributes attr = Files.readAttributes(aFile.toPath(), BasicFileAttributes.class);
			size = attr.size();
			creationDate =attr.creationTime();
			hashkey = attr.hashCode();
			long javaTime = attr.lastModifiedTime().toMillis();
			lastModification= new Date(javaTime);
			 
			//todo this.getName => in the real name
			song = new Song( this.getName(), getRelativeName(), ArtistContainer.getSingleton().getUnknownId(), AlbumContainer.getSingleton().getUnknownId(), hashkey, size, lastModification);
			
		}
		catch (IOException e)	{ e.printStackTrace();}

	}

	//Accessors
	public long getSize() 				{ return size;}
	public int getHasKey() 				{ return hashkey;}
	public Date getLastModification() 	{ return lastModification;}
	public void loadChild()		 		{}  //do nting since it the leaf in the recursion
	public Boolean isDirectory() 		{ return false;}
	public void print()			 		{ song.print();}
	public Song getSong()		 		{ return song;}
}
