package com.salvinien.playlists;

import com.salvinien.mymusicsync.LibelleContainer;

public class PlaylistNamesContainer extends LibelleContainer
{
	//Members
	protected static PlaylistNamesContainer  mySingleton=null;

	//CTOR
	protected PlaylistNamesContainer() 
	{
		super( "PlaylistNames");
	}

	
	//ACCESSORS
	public static PlaylistNamesContainer  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new PlaylistNamesContainer();
		
		return mySingleton;
	}




}
