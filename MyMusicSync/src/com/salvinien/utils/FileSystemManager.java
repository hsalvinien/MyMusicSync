package com.salvinien.utils;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * @class: FileSystemManager
 * 
 * This class manages file system operation
 * 
 */
public class FileSystemManager
{


	/**
	 * @method : public void copyFile( String fileNameSource, String fileNameTarget)
	 * 
	 * copy a file and keeps its properties 
	 */
	static public void copyFile( String fileNameSource, String fileNameTarget)
	{
		try
		{
			//1) we create the target dir
			String S = fileNameTarget;
			S = S.substring(0, S.lastIndexOf(File.separatorChar));
			Path targetDir = new File( S).toPath();
			Files.createDirectories(targetDir);
					
			//2) we create a target file if there isn't
			File fTo = new File( fileNameTarget);
			if( !fTo.exists())
			{//if target file doesn't exist, we create an empty one
				fTo.createNewFile();
			}

			Files.copy(  Paths.get(fileNameSource), Paths.get(fileNameTarget), REPLACE_EXISTING, COPY_ATTRIBUTES);
		}
		catch (IOException e) { e.printStackTrace();}

	}

	
	
	
	/**
	 * @method : public void delteFileAndCleanDirectory( String fileName)
	 * 
	 * delete a file, if directory is empty, then delete the directory etc 
	 */
	static public void delteFileAndCleanDirectory( String fileName)
	{
		//first we delete the file
		File f = new File( fileName);
		f.delete(); 

		//now we clean the directory and its parents if necessary
		File p = f.getParentFile();
		if( p== null) return;		
		
		File children[] = p.listFiles();
		if( (children==null) || (children.length==0) )
		{//so directoy is empty we have to shoot again
			FileSystemManager.delteFileAndCleanDirectory(p.getAbsolutePath());
		}
		
	}
}
