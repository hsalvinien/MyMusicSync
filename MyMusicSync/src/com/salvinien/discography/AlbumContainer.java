package com.salvinien.discography;

import com.salvinien.mymusicsync.LibelleContainer;

/*
 * @class: AlbumContainer
 * 
 * This class manages Artists
 
 * it is implemented as a singleton (Design pattern)
 * 
 * 
 * @todo : there is, here, probably a misconception => because two artists may have created an album with the same name...
 * 
 */
public class AlbumContainer extends LibelleContainer
{
	//Members
	protected static AlbumContainer  mySingleton=null;

	//CTOR
	//private to forbid the creation of instances but from getSingleton
	private AlbumContainer() 
	{
		super( "Albums"); //"Albums" : is the table name in the database  
	}

	
	//ACCESSORS
	//accessor that gives the only instance of the  class 
	public static AlbumContainer  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new AlbumContainer();		
		return mySingleton;
	}

}
