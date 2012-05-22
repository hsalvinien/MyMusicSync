package com.salvinien.synclists;

import com.salvinien.mymusicsync.LibelleContainer;

public class SynclistNamesContainer extends LibelleContainer
{
	//Members
	protected static SynclistNamesContainer  mySingleton=null;

	//CTOR
	protected SynclistNamesContainer() 
	{
		super( "SynclistNames");
	}

	
	//ACCESSORS
	public static SynclistNamesContainer  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new SynclistNamesContainer();
		
		return mySingleton;
	}




}
