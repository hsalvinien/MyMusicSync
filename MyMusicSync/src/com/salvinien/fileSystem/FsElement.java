package com.salvinien.fileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.FileTime;


/**
 * @class: FsElement
 * 
 * This class is the base class to manage the song file in the file system 
 * it gives the basic accessors and methods, and gives some methods to be implemented by daughters 
 * 
 */

public abstract class FsElement 
{
	protected String name;
	protected String path;
	protected String mountPoint;
	protected FileTime creationDate;

	//CTOR
	public	FsElement( String aMountPoint, String aFullPathName)	{ setFullName( aMountPoint, aMountPoint+aFullPathName);}
	public	FsElement( String aMountPoint, File aFile)	
	{ 
		String aFullPathName=null;
		
		try {aFullPathName = aFile.getCanonicalPath();} 
		catch (IOException e) {	e.printStackTrace();}

		if( aFile.isDirectory()) 
		{
			aFullPathName = aFullPathName+File.separatorChar;
		}
		
		setFullName( aMountPoint, aFullPathName);
	}

	
	//ACCESSORS
	public String getName() 		{ return name;}
	public String getPath() 		{ return path;}
	public String getMountPoint() 	{ return mountPoint;}
	
	
	/**
	 * @method : String getFullName() 
	 * rebuild the full name with the name, mount point and path 
	 */
	public String getFullName() 	
	{ 
		String fullPath;
		
		fullPath = mountPoint+getRelativeName() ;
		
		return fullPath;
	}

	/**
	 * @method : String getRelativeName() 
	 * rebuild the name with the name and path (so not the mount point) 
	 */
	public String getRelativeName() 	
	{ 
		String fullPath;
		
		fullPath = path;
		if( path.length()>0 )
		{
			if( path.charAt(path.length()-1) != File.separatorChar )
			{
				fullPath+=File.separatorChar;
			}
		}
		
		fullPath+=name;
		
		return fullPath;
	}
	

	
	
	
	public void setName( String aName) 				{ name = new String(aName);}
	public void setPath( String aPath) 				{ path = new String(aPath);}
	public void setMountPoint( String aMountPOint) 	{ mountPoint = new String(aMountPOint);}

	
	/**
	 * @method : void setFullName(String aMountPoint, String aPath)
	 * splits the mount point, path and file name (if it is file) 
	 */
	public void setFullName(String aMountPoint, String aPath) 
	{ 
		int i =0;
		mountPoint = new String(aMountPoint);
				
		
		//the name
		char aChar =  aPath.charAt(aPath.length()-1);
		if( (aChar == File.separatorChar ) || (aChar == '/' ) ) 
		{
			//then it is a Directory 
				//so
			name ="";
			path = aPath;
		}
		else
		{
			//then it is a file
			i = aPath.lastIndexOf(File.separator);
			name = aPath.substring(i+1);

			if( i<1) { path = "";}
			else 	 { path = aPath.substring(0, i+1);}
		}
		
		
		
		//the path 
		i = path.indexOf(aMountPoint);
		if( i>-1 )
		{
			path = path.substring(i+aMountPoint.length());
		}
		
	}
	
	
	
	//methods, those methods have to be implemented in the daughters
	public abstract Boolean isDirectory();   //return true if it is a directoty or false if a file
	public abstract void print();			//print the elment (for debug purpose)
	public abstract void loadChild();		//load the remaining tree

}
