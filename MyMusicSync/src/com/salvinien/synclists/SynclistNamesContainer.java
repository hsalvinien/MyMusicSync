package com.salvinien.synclists;

import com.salvinien.mymusicsync.LibelleContainer;


/*
 * @class: SynclistNamesContainer
 * 
 * This class manages synclists names
 * 
 * it is implemented has a singleton
 * 
 */

public class SynclistNamesContainer extends LibelleContainer
{
	//Members
	protected static SynclistNamesContainer  mySingleton=null;

	//CTOR
	protected SynclistNamesContainer() 
	{
		super( "SynclistNames"); //table name
	}

	
	//ACCESSORS
	public static SynclistNamesContainer  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new SynclistNamesContainer();
		
		return mySingleton;
	}




}
