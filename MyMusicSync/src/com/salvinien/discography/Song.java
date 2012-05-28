package com.salvinien.discography;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.salvinien.fileSystem.FsFile;
import com.salvinien.mymusicsync.Libelle;
import com.salvinien.mymusicsync.Tags;
import com.salvinien.utils.Converter;

import static java.nio.file.StandardCopyOption.*;



/*
 * @class: FileSongContainer
 * 
 * This class manages a song 
 
 * 
 */
public class Song
{
	//Members
	protected int Id;
	protected String Name;
	protected String FileName;
	protected int ArtistID;
	protected int AlbumID;
	protected int hashkey;
	protected long size;
	protected Date LastModification;
	
	
	//Ctors
	public Song( int anId, String aName, String aFileName, int anArtistID, int anAlbumID, int anHashKey, long aSize, Date aLastModification)
	{
		init(  anId, aName, aFileName, anArtistID, anAlbumID, anHashKey, aSize, aLastModification);
	}
	
	
	public Song( String aName, String aFileName, int anArtistID, int anAlbumID, int anHashKey, long aSize, Date aLastModification)
	{
		init(  -1, aName, aFileName, anArtistID, anAlbumID, anHashKey, aSize, aLastModification);
	}

	public Song( FsFile aFile)
	{
		//todo this.getName => in the real name
		init( -1, aFile.getName(), aFile.getRelativeName(), ArtistContainer.getSingleton().getUnknownId(), AlbumContainer.getSingleton().getUnknownId(), aFile.getHasKey(), aFile.getSize(), aFile.getLastModification());
	}

	
	public void init( int anId, String aName, String aFileName, int anArtistID, int anAlbumID, int anHashKey, long aSize, Date aLastModification)
	{
		Id = anId;
		Name = aName.trim();
		FileName = aFileName;
		ArtistID = anArtistID;
		AlbumID = anAlbumID;
		hashkey = anHashKey;
		LastModification = aLastModification;
		size =aSize;
	}
	
	
	
	//ACCESSORS
	public int 		getId() 		{ return Id;}
	public String 	getName() 		{ return Name;}
	public String 	getFileName() 	{ return FileName;}
	public int 		getArtistID() 	{ return ArtistID;}
	public int 		getAlbumID() 	{ return AlbumID;}
	public int 		getHashkey() 	{ return hashkey;}
	public long		getSize() 		{ return size;}
	public Date 	getLastModification()	{ return LastModification;}
	
	public void 	setId( int anId) 	{ Id =anId;}

	
	//METHODS
	
	//Methods
	/*@method : print()
	 * display a song in the console (debug purpose)
	 */
	public void print()
	{
		System.out.println( Name+ " ==>  FILE: size: " +String.valueOf(size)+"   hashkey: "+String.valueOf(hashkey)+"  Last Update:"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(LastModification));

	}
	
	

	
	/*@method : void copy( String sFrom, String sTo) throws IOException
	 * copy a song from a location (sFrom) to another location (sTo) 
	 */
	public void copy( String sFrom, String sTo) throws IOException
	{
		
		//1) we create the target dir
		String S = sTo+FileName;
		S = S.substring(0, S.lastIndexOf(File.separatorChar));
		Path targetDir = new File( S).toPath();
		Files.createDirectories(targetDir);
				
		//2) we create a target file if there isn't
		File fTo = new File( sTo+ FileName);
		if( !fTo.exists())
		{//if target file doesn't exist, we create an empty one
			fTo.createNewFile();
		}
		
		
		Files.copy( Paths.get( sFrom + FileName), Paths.get( sTo+ FileName), REPLACE_EXISTING, COPY_ATTRIBUTES);
		
	}


	
	/*@method : updateInfos( int lib, HashMap<String, String> h)
	 * uses the tag information in h, previously filled, to update the song information 
	 */
	private void updateInfos( int lib, HashMap<String, String> h)
	{
		//Artist
		String Artist = h.get(Tags.getArtist(lib));
		

		if( Artist!=null)
		{
			Artist = Artist.trim();
			Libelle l = ArtistContainer.getSingleton().getLibelle(Artist);
			if( l==null)
			{
				l = ArtistContainer.getSingleton().create(Artist);
			}
			
			ArtistID = l.getId();
		}

		//Album
		String Album = h.get(Tags.getAlbum(lib));
		if( Album!=null)
		{
			Album = Album.trim();
			Libelle l = AlbumContainer.getSingleton().getLibelle(Album);
			if( l==null)
			{
				l = AlbumContainer.getSingleton().create(Album);
			}
			
			AlbumID = l.getId();
		}
		
		//song Name /title
		String songName = h.get(Tags.getTitle(lib));
		if(songName!=null)
		{
			Name= songName.trim();
		}
			
		
	}

	
	
	/*@method : updateInformationFromFile(String aMountPoint) throws IOException
	 * retrieve information about songs directly from the file. it uses the right tag reading method depending on the file type   
	 */

	public void updateInformationFromFile(String aMountPoint) throws IOException
	{
		//extract Artist/album/song name from file
		//create if necessary artist and album
		
		HashMap<String, String> h = new HashMap<String, String>();
		File f= new File( aMountPoint+getFileName());
		RandomAccessFile  is = new RandomAccessFile ( f, "r");
		byte[] head = new byte[4];
		int lib=0;
		
		if( is.read(head) <4) 
		{
			is.close();
			return; //the file is too small !
		}
	
		if( head[0]==-1  && head[1]==-40) // JPG case
		{
			is.close();
			return;  
		}
 			
		if( head[0]=='#'  && head[1]=='E'  && head[2]=='X'  && head[3]=='T'  ) // Synclist case
		{
			is.close();
			return;  
		}
 			
		
		if( head[0]=='f'  && head[1]=='L'  && head[2]=='a'  && head[3]=='C'  )//FLAC
		{
			lib= Tags.FLAC;
			updateInformationFromFlacFile( is, h); 
		}
		else if( head[0]=='I'  && head[1]=='D'  && head[2]=='3') //ID3
		{
		    switch(head[3])
		    {
			  case 3: 
				  lib = Tags.ID3v23;
				  updateInformationFromID3v2xFile( is, h, lib);
				break;
				
			  case 2: 
				  lib = Tags.ID3v22;
				  updateInformationFromID3v2xFile( is, h, lib);
				break;
			  case 4: 
				  lib = Tags.ID3v24;
				  updateInformationFromID3v2xFile( is, h, lib);
				break;
					  
			  default: System.out.println("ID3v2 tags => not imolemented v:"+head[3]+"  =>"+FileName);
		    }
		}
		else
		{

			if( is.length()<= 128 ) 
			{
				String s= new String( head);
				System.out.println(" File : "+getFileName()+" ==>"+s  );
				
				is.close();
				return;
			}
			
			head = new byte[3];
			is.seek( is.length()-128);
			is.read(head);
			lib = Tags.ID3v1;
			if( head[0]=='T'  && head[1]=='A'  && head[2]=='G') //ID3v1
			{
				updateInformationFromID3v1File( is, h, lib);
			}
			else
			{
				String s= new String( head);
				System.out.println(" File : "+getFileName()+" ==>"+s  );
			}
		}
		
		is.close();
		
		updateInfos(lib, h);
	}


	
	/*@method : updateInformationFromID3v1File( RandomAccessFile is, HashMap<String, String> h, int lib) throws IOException
	 * retrieve  the song information from the file, case of a mp3 file (ID3v1)
	 * 
	 * HashMap<String, String> h  will contain the tags (key) with their values (value)
	 * 
	 * for mp3 with ID3v1
	 * 
	 * Songname   is stored between byte 3-32
	 * Artist	33-62
	 * Album    63-92
	 * Year     93-96
	 * Comment  97-126
	 * Genre    127
	 */
	private void updateInformationFromID3v1File( RandomAccessFile is, HashMap<String, String> h, int lib) throws IOException
	{
		byte [] b;
		String S;
		is.seek( is.length()-128);

		is.skipBytes(3);
		b = new byte[ 30]; //Songname   30          3-32
		is.read( b);
		S = Converter.byteToString(b).trim() ;
		if( S.length()!=0)
			h.put( Tags.getTitle(lib), S ); 
		
		
		b = new byte[ 30]; //Artist     30         33-62
		is.read( b);
		S = Converter.byteToString(b).trim() ;
		if( S.length()!=0)
			h.put( Tags.getArtist(lib), S );
		
	    b = new byte[ 30]; //Album      30         63-92
		is.read( b);
		S = Converter.byteToString(b).trim() ;
		if( S.length()!=0)
			h.put( Tags.getAlbum(lib), S );

	    //Year       4          93-96
	     //Comment    30         97-126
	     //Genre      1           127
	}

	
	/*@method : updateInformationFromID3v2File( RandomAccessFile is, HashMap<String, String> h, int lib) throws IOException
	 * retrieve  the song information from the file, case of a mp3 file (ID3v2)
	 * 
	 * HashMap<String, String> h  will contain the tags (key) with their values (value)
	 * 
	 * for mp3 with ID3v2, the description has changed, it is now like Tag value 
	 * so we start by reading the tag then the value
	 */
	private void updateInformationFromID3v2xFile( RandomAccessFile is, HashMap<String, String> h, int lib) throws IOException
	{
		byte[] header= new byte[6];
		byte[] headerSize= new byte[4];
		byte b[] = null;
		byte anInt[] =new byte[Tags.intLen(lib)];
		byte[] aTag= new byte[ Tags.tagLen(lib)];
		String Key=null;
		String Value=null;
		
		//first header end
		is.seek( 0); //go to file start
		is.read(header); ///read the 10 bytes header
		is.read(headerSize); ///read the 10 bytes header
		int lsize = Converter.byteArrayToIntMBS(headerSize);
		lsize +=10; //the 10first  bytes are not included , so we have to add them 
		
		boolean again = true;
		while( again)
		{
			//read the next TagName:
			is.read(aTag);
			
			//stop condition
			if( is.getFilePointer() >lsize)
				break;
			if( aTag[0]<=0) 
				break;
			
			Key = Converter.byteToString(aTag);
					
			//read the size of the value
			is.read( anInt);
			int len = Converter.byteArrayToIntMBS(anInt);
			//skip the 2byte flag
			is.skipBytes(Tags.offsetTagValue(lib));

			
			//Stop condition ( @TODO => CHECK if it is the right condition)
			//if len == 0 it means there is nothing more to do
			if( len ==0)  break;

			is.skipBytes(1); //one byte flag about string codage which is redundant so we ignore it

			//then we read the value
			b= new byte[ len-1];  //-1 because of the one-byte flag 
			is.read(b);
			Value = Converter.byteToString(b).trim();
			
			h.put(Key, Value);
		}

	}
	

	
	/*@method : updateInformationFromFlacFile( RandomAccessFile is, HashMap<String, String> h, int lib) throws IOException
	 * retrieve  the song information from the file, case of flac file 
	 * 
	 * HashMap<String, String> h  will contain the tags (key) with their values (value)
	 * 
	 * a flac tag  is a string "tagname = tagvalue"
	 */
	private void updateInformationFromFlacFile( RandomAccessFile is, HashMap<String, String> h) throws IOException
	{
		byte b[] = null;
		byte anInt[] =new byte[4];
		int nb;
		
		
		//first header
		is.skipBytes(42);
		
		//version of libflac
		is.read(anInt);
		nb = Converter.byteArrayToInt(anInt);
		b= new byte[ nb];
		is.read(b);
		String refLib = Converter.byteToString(b).trim();
		h.put("RefLib", refLib);
		
		//number of items
		is.read(anInt);
		int nbItems = Converter.byteArrayToInt(anInt);
		for( int i =0; i< nbItems;i++)
		{
			is.read(anInt);
			nb = Converter.byteArrayToInt(anInt);
			b= new byte[ nb];
			is.read(b, 0, nb);
			String S = Converter.byteToString(b).trim();
			
			String Key   = S.substring(0, S.indexOf('=')).toUpperCase().trim();
			String Value = S.substring(S.indexOf('=')+1).trim();
			
			h.put(Key, Value);
		}

	}
	
	
	
	
	
	/*@method : boolean updateInformationFromSong( Song aSongFromFile)
	 * check if the file is older or newer. If aSongFile is newer than this, then we upadte infos.
	 * 
	 * @todo : check that there isn't a bias due to the Math.abs ..
	 */
	public boolean updateInformationFromSong( Song aSongFromFile)
	{
		boolean songHasBeenModified=false;
		
		//
		long l = LastModification.getTime() - aSongFromFile.LastModification.getTime();
		l = Math.abs(l);
		
		if(l> 1000 )
		{
			songHasBeenModified=true;
			init(  Id, aSongFromFile.Name, aSongFromFile.FileName, aSongFromFile.ArtistID, aSongFromFile.AlbumID, aSongFromFile.hashkey, aSongFromFile.size, aSongFromFile.LastModification);
		}
		
		
		return songHasBeenModified;
	}
}
