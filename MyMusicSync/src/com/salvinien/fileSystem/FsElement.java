package com.salvinien.fileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.FileTime;



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
	public String getFullName() 	
	{ 
		String fullPath;
		
		fullPath = mountPoint+path;
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
	public void setFullName(String aMountPoint, String aPath) 
	{ 
		int i =0;
		mountPoint = new String(aMountPoint);
				
		
		//the name
		//if( aPath.charAt(aPath.length()-1) == File.separatorChar )
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
	
	
	//methods
	public abstract Boolean isDirectory();
	public abstract void print();
	
	//to be expressed in an other way to allow package to be fully independant
	public abstract void loadChild();

}
