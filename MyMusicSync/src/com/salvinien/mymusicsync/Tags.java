package com.salvinien.mymusicsync;


/*
 * @class: Tags
 * 
 * This class manages tag names by type of tag lib 
 * 
 * generally you give the lib, you ask the type of info it returns the the Tag name
 * 
 */

public class Tags
{
	
	//Members: the libs
	public static final int FLAC =1;		//flag tags
	public static final int ID3v22 =2;		//mp3 tags, v2.2
	public static final int ID3v23 =3;		//mp3 tags, v2.3
	public static final int ID3v24 =4;		//mp3 tags, v2.4
	public static final int ID3v1 =5;		//mp3 tags, v1
	
	
	//METHODS
	/*@method : public String getArtist( int lib)
	 * 
	 *   returns the tag artist 
	 */
	static public String getArtist( int lib)
	{
		switch (lib)
		{
			case ID3v1: 	return "TP1";
			case ID3v22: 	return "TP1";
			case ID3v23:	return "TPE1";
			case ID3v24:	return "TPE1";
				
			case FLAC:
			default:	return "ARTIST";
			
		}
	}

	
	/*@method : public String getAlbum( int lib)
	 * 
	 *   returns the tag Album
	 */
	static public String getAlbum( int lib)
	{
		switch (lib)
		{
			case ID3v1: 	return "TAL";
			case ID3v22: 	return "TAL";
			case ID3v23:	return "TALB";
			case ID3v24:	return "TALB";
				
			case FLAC:
			default:	return "ALBUM";
			
		}
	}


	
	/*@method : public String getTitle( int lib)
	 * 
	 *   returns the tag title (of the song)
	 */
	static public String getTitle( int lib)
	{
		switch (lib)
		{
			case ID3v1: 	return "TT2";
			case ID3v22: 	return "TT2";
			case ID3v23:	return "TIT2";
			case ID3v24:	return "TIT2";
				
			case FLAC:
			default:	return "TITLE";
			
		}
	}


	
	/*@method : int tagLen( int lib)
	 * 
	 *   returns the length of the tag
	 */
	static public int tagLen( int lib)
	{
		switch (lib)
		{
			case ID3v1: 	return 3;
			case ID3v22: 	return 3;
			case ID3v23:	return 4;
			case ID3v24:	return 4;
				
			case FLAC:
			default:	return 4;
			
		}
	}

	/*@method : int intLen( int lib)
	 * 
	 *   returns the length of the int that encode the size of the string to be read 
	 */
	static public int intLen( int lib)
	{
		switch (lib)
		{
			case ID3v1: 	return 3;
			case ID3v22: 	return 3;
			case ID3v23:	return 4;
			case ID3v24:	return 4;
				
			case FLAC:
			default:	return 4;
			
		}
	}
	

	/*@method : int offsetTagValue( int lib)
	 * 
	 *   returns the offset (if any) before the information start  in the header
	 */
	static public int offsetTagValue( int lib)
	{
		switch (lib)
		{
			case ID3v1: 	return 0;
			case ID3v22: 	return 0;
			case ID3v23:	return 2;
			case ID3v24:	return 2;
				
			case FLAC:
			default:	return 0;
			
		}
	}
	
	
}
