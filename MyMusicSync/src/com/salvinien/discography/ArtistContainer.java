package com.salvinien.discography;

import com.salvinien.mymusicsync.LibelleContainer;


public class ArtistContainer extends LibelleContainer
{
	//Members
	protected static ArtistContainer  mySingleton=null;

	//CTOR
	protected ArtistContainer() 
	{
		super( "Artists");
	}

	
	//ACCESSORS
	public static ArtistContainer  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new ArtistContainer();
		
		return mySingleton;
	}
}
