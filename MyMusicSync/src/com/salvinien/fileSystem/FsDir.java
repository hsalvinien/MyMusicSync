package com.salvinien.fileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Vector;


/**
 * @class: FsFile
 * 
 * This class represents the Node class to manage the song file in the file system 
 * 
 */

public class FsDir  extends FsElement
{

	//members
	protected Vector<FsElement> elts =new Vector<FsElement>();

	//Ctor
	public FsDir( String aMountPoint, String aFullPath) {super(aMountPoint, aFullPath);}
	public FsDir( String aMountPoint, File aFile)	
	{ 
		super( aMountPoint, aFile);
		try
		{
			BasicFileAttributes attr = Files.readAttributes(aFile.toPath(), BasicFileAttributes.class);
			creationDate =attr.creationTime();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
	
	//Accessors
	public FsElement getElt( int i )  { return elts.elementAt(i);}
	public int size() 				  { return elts.size();}
	
	//Methods
	public void add( FsElement anElt) { elts.add(anElt);}
	public Boolean isDirectory() 	  { return true;}
	public void print()	              { System.out.println(getFullName() + " ==>  DIRECTORY:  created:" +creationDate);}


	/**
	 * @method : void loadChild()
	 * recursion to load the tree of files songs
	 */
	public void loadChild()
	{
		File d = new File( getFullName());
		File[] files = d.listFiles();
		
		if( files==null) return;
		
		for( int i =0; i<files.length; i++)
		{
			FsElement e=null;
			
			if( files[i].isDirectory())  {  e = new FsDir( mountPoint, files[i]);}
			else						 {  e = new FsFile( mountPoint, files[i]);}
			
			e.loadChild();
			elts.add(e);	
		}	
	}
		
}