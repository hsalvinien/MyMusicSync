package com.salvinien.mymusicsync;

public class Tags
{
	
	public static final int FLAC =1;
	public static final int ID3v22 =2;
	public static final int ID3v23 =3;
	public static final int ID3v24 =4;
	public static final int ID3v1 =5;
	
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
	
	
	
	static public String get( int lib)
	{
		switch (lib)
		{
			case ID3v1: 	return "";
			case ID3v22: 	return "";
			case ID3v23:	return "";
			case ID3v24:	return "";
				
			case FLAC:
			default:	return "";
			
		}
	}

}
