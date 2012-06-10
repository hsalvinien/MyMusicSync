package com.salvinien.synclists;

import com.salvinien.database.MyDatabase;
import com.salvinien.mymusicsync.LibelleContainer;


/**
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


	/**
	 * @method : void delete( int aSyncListID)
	 * delete a synclist from the names container, from database AND from SynclisContainer
	 * 
	 * actually, to delete a synclist you can rather (if you prefer) call the delete from SynclisContainer
	 * it is exactly the same, both methods work together and insure the same service  
	 */
	public void delete( int aSyncListID)
	{
		//0) precheck
		if( this.getLibelle(aSyncListID)==null)   return;
		
		//1) from the synclist from the names container and its database table
		super.delete(aSyncListID);
		
		//2) delete the different associations
		String aQuery 	= 	"DELETE FROM DeviceSyncList WHERE SynclistId ="+aSyncListID;
		MyDatabase.getSingleton().executeSimpleQuery(aQuery);
		MyDatabase.getSingleton().commit();
		
		//3) delete from SynclistContainer
		SynclistContainer.getSingleton().delete( aSyncListID);
	}


}
