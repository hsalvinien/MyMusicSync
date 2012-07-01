package com.salvinien.database;

import java.sql.ResultSet;

/**
 * @class: dataModel
 * 
 * This class manages the definitions of the data model
 * it manages also:
 * - the versionning of the data model
 * - the pre-population of the database
 * - and the population for test purpose
 * 
 * it is implemented as a singleton (Design pattern)
 * 
 * 
 * 
 * 
 */

public class dataModel
{
	
	protected static dataModel  mySingleton=null;	/** the only instance of dataModel*/
	protected int version = 1;						/** version of the dataModel */
	
	//CTOR
	//private to forbid the creation of instances but from getSingleton
	private dataModel() 
	{
	}

	
	//METHODS
	/**
	 * @method : dataModel  getSingleton()
	 * this accessor returns the only instance of the class
	 * cf. DESIGN PATTENR singleton   
	 */
	public static dataModel  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new dataModel();
		
		return mySingleton;
	}

	
	/**
	 * @method : checkDatabase
	 * check the database.
	 * 1) if the database doesn't exist, it creates it
	 * 2) if the database exists but is from a previous version, then it runs upgrade patch  
	 */
	public void checkDatabase()
	{
		String query = "SELECT * FROM Version";
		try 
		{
			ResultSet rs = MyDatabase.getSingleton().executeQueryRethroughtException( query);
			int currentVersion = rs.getInt("version");
			
			updateDataModelFromVersion( currentVersion);
		}
		catch(Exception e)
		{
			//so we have to create the datamodel
			createDataModel() ;
		}
	}


	
	
	/**
	 * @method : createDataModel
	 * create the data model
	 */
	private void createDataModel() 
	{
		String aQuery;
		aQuery 	= 	"CREATE TABLE Songs (" +
				  	"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"Name VARCHAR(90), " +
					"FileName VARCHAR(256), " +
					"ArtistID INTEGER, " +
					"Hashkey INTEGER, " +
					"Size INTEGER, " +
					"LastModification DATETIME, " +
					"AlbumID INTEGER)";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

	
		aQuery 	= 	"CREATE TABLE Artists (" +
					"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"Name VARCHAR(90))";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"CREATE TABLE Albums (" +						//@TODO =>my mistake, this is not possible because two artits may have both an album with the same name !
					"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"Name VARCHAR(90))";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"CREATE TABLE SynclistNames(" +
					"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"Name VARCHAR(90))";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"CREATE TABLE SynclistValues(" +
					"SynclistID INTEGER, " +
					"SongId INTEGER )";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"CREATE TABLE Devices(" +
					"DeviceID INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"DeviceName VARCHAR(90), " +
					"DeviceType   INTEGER)";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"CREATE TABLE DeviceSyncList(" +
					"DeviceID INTEGER, " +
					"DefaultPath VARCHAR(256), " +
					"SynclistId INTEGER)";

		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"CREATE TABLE Parameters(" +
					"ParameterName VARCHAR(30), " +
					"value VARCHAR(30))";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"CREATE TABLE DevicesType(" +
					"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"Name VARCHAR(90))";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		
		aQuery 	= 	"CREATE TABLE Version (" +
				"version INTEGER)";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		
		MyDatabase.getSingleton().commit();
		
		
		populate();
		
		//for test purpose
		populateTest();
	}

	
	
	
	/**
	 * @method : populate
	 * initialize the database 
	 */
	private void populate() 
	{
		String aQuery;
		aQuery = 	"INSERT INTO SynclistNames (Name) values ( 'All')";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery = 	"INSERT INTO SynclistNames (Name) values ( 'Latest')";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);		
		aQuery 	= 	"INSERT INTO Parameters (ParameterName) VALUES ( 'root') ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO DevicesType ('Name') VALUES ( 'local hard drive') ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO DevicesType ('Name') VALUES ( 'network hard drive') ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO DevicesType ('Name') VALUES ( 'usb hard drive') ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO DevicesType ('Name') VALUES ( 'Phone') ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO DevicesType ('Name') VALUES ( 'Portable device') ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO Version ('version') VALUES ( '"+ version+"') ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		
		MyDatabase.getSingleton().commit();
	}


	
	/**
	 * @method : populateTest
	 * initialize the database with test data 
	 */
	private void populateTest() 
	{
		String aQuery;
		
			
		aQuery 	= 	"INSERT INTO SynclistNames (Name) VALUES ( 'TestList1') ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
//		aQuery 	= 	"INSERT INTO SynclistNames (Name) VALUES ( 'TestList2') ";
//		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		
		aQuery 	= 	"INSERT INTO Devices (DeviceName, DeviceType) VALUES ( 'deviceTest', 1) ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
//		aQuery 	= 	"INSERT INTO DeviceSyncList ('DeviceID', 'DefaultPath', 'SynclistId') VALUES ( 1, 'C:\\FileSystemeE\\development\\git-repos\\MyMusicSync\\MyMusicSync\\myMusicTest', 3)";
//		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

/*
		aQuery 	= 	"INSERT INTO Devices (DeviceName, DeviceType) VALUES ( 'MyWD', 3) ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO DeviceSyncList ('DeviceID', 'DefaultPath', 'SynclistId') VALUES ( 2, 'G:\\1.Multimedia\\Audio\\1.Audio-flac', 3)";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"INSERT INTO Devices (DeviceName, DeviceType) VALUES ( 'SG II', 4) ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO DeviceSyncList ('DeviceID', 'DefaultPath', 'SynclistId') VALUES ( 3, 'E:\\root', 3)";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO DeviceSyncList ('DeviceID', 'DefaultPath', 'SynclistId') VALUES ( 3, 'F:\\sd', 4)";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"INSERT INTO Devices (DeviceName, DeviceType) VALUES ( 'DX100', 5) ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO DeviceSyncList ('DeviceID', 'DefaultPath', 'SynclistId') VALUES ( 4, 'G:\\root', 3)";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO DeviceSyncList ('DeviceID', 'DefaultPath', 'SynclistId') VALUES ( 4, 'H:\\sd', 4)";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
*/



		//'C:\\FileSystemeE\\development\\workspace-loc\\MyMusicSync\\myMusicTest\\'		
		
/*		aQuery 	= 	"INSERT INTO SynclistValues (SynclistID,SongId) VALUES ( 3,1) ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO SynclistValues (SynclistID,SongId) VALUES ( 3,2) ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"INSERT INTO SynclistValues (SynclistID,SongId) VALUES ( 3,3) ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"INSERT INTO SynclistValues (SynclistID,SongId) VALUES ( 4,4) ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO SynclistValues (SynclistID,SongId) VALUES ( 4,5) ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"INSERT INTO SynclistValues (SynclistID,SongId) VALUES ( 4,6) ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
*/
		//aQuery 	= 	"INSERT INTO Parameters (ParameterName,value) VALUES ( 'root','C:\\FileSystemeE\\work\\multimedia\\Audio\\1.Audio-flac\\') ";
		aQuery 	= 	"UPDATE  Parameters SET value = 'C:\\poub\\root\\' WHERE ParameterName ='root' ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		
		MyDatabase.getSingleton().commit();
	}

	
	
	
	/**
	 * @method : updateDataModelFromVersion
	 * update the data model from its current version to the the new version  
	 */	
	private void updateDataModelFromVersion( int currentVersion)
	{
		if( currentVersion == version)  return; //the current version and the datamodel are the same so nothing to update
		
		switch( currentVersion)
		{
/*			case 1: updateDataModelfrom1to2();
			case 2: updateDataModelfrom2to3();    ///don't add the break statement in order to allow the sequence of updates
			case 3: updateDataModelfrom3to4();
			case 4: updateDataModelfrom4to5();
*/						
		}
	}

}
