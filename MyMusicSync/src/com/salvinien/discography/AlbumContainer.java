package com.salvinien.discography;

import com.salvinien.mymusicsync.LibelleContainer;



public class AlbumContainer extends LibelleContainer
{
	//Members
	protected static AlbumContainer  mySingleton=null;

	//CTOR
	protected AlbumContainer() 
	{
		super( "Albums");
	}

	
	//ACCESSORS
	public static AlbumContainer  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new AlbumContainer();		
		return mySingleton;
	}

}
