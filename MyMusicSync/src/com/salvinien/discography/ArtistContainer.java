package com.salvinien.discography;

import com.salvinien.mymusicsync.LibelleContainer;



/*
 * @class: ArtistContainer
 * 
 * This class manages Artists
 
 * it is implemented as a singleton (Design pattern)
 * 
 * 
 * 
 * 
 */

public class ArtistContainer extends LibelleContainer
{
	//Members
	protected static ArtistContainer  mySingleton=null;

	//CTOR
	//private to forbid the creation of instances but from getSingleton
	private ArtistContainer() 
	{
		super( "Artists"); //"Artists" : is the table name in the database
	}

	
	//ACCESSORS
	//accessor that gives the only instance of the  class 
	public static ArtistContainer  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new ArtistContainer();
		
		return mySingleton;
	}
}
