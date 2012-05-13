package com.salvinien.database;

import java.sql.ResultSet;

public class dataModel
{
	
	protected static dataModel  mySingleton=null;
	protected int version = 1;
	
	//CTOR
	protected dataModel() 
	{
	}

	
	//ACCESSORS
	public static dataModel  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new dataModel();
		
		return mySingleton;
	}

	
	//METHODS
	
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


	
	
	protected void createDataModel() 
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
							//date modif 
							//key
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

	
		aQuery 	= 	"CREATE TABLE Artists (" +
					"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"Name VARCHAR(90))";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"CREATE TABLE Albums (" +
					"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"Name VARCHAR(90))";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"CREATE TABLE PlaylistNames(" +
					"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"Name VARCHAR(90))";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"CREATE TABLE PlaylistValues(" +
					"PlaylistID INTEGER, " +
					"SongId INTEGER )";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"CREATE TABLE Devices(" +
					"DeviceID INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"DeviceName VARCHAR(90), " +
					"DefaultPath   VARCHAR(256), " +
					"DeviceType   INTEGER, " +
					"PlaylistId INTEGER)";
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

	
	protected void populate() 
	{
		String aQuery;
		aQuery = 	"INSERT INTO PlaylistNames (Name) values ( 'All')";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery = 	"INSERT INTO PlaylistNames (Name) values ( 'Latest')";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);		
		aQuery 	= 	"INSERT INTO Parameters (ParameterName) VALUES ( 'root') ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO DevicesType ('Name') VALUES ( 'local hard drive') ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO DevicesType ('Name') VALUES ( 'network hard drive') ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO DevicesType ('Name') VALUES ( 'usb hard drive') ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO Version ('version') VALUES ( '"+ version+"') ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		
		MyDatabase.getSingleton().commit();
	}

	
	protected void populateTest() 
	{
		String aQuery;
		
			
		aQuery 	= 	"INSERT INTO PlaylistNames (Name) VALUES ( 'TestList') ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO Devices (DeviceName, DefaultPath, DeviceType, PlaylistId) VALUES ( 'TestDevice', 'C:\\FileSystemeE\\development\\workspace-loc\\MyMusicSync\\myMusicTest\\', 1, 3) ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"INSERT INTO PlaylistValues (PlaylistID,SongId) VALUES ( 3,1) ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);
		aQuery 	= 	"INSERT INTO PlaylistValues (PlaylistID,SongId) VALUES ( 3,2) ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		aQuery 	= 	"INSERT INTO PlaylistValues (PlaylistID,SongId) VALUES ( 3,3) ";
		MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		//aQuery 	= 	"INSERT INTO Parameters (ParameterName,value) VALUES ( 'root','C:\\FileSystemeE\\work\\multimedia\\Audio\\1.Audio-flac\\') ";
		//aQuery 	= 	"INSERT INTO Parameters (ParameterName,value) VALUES ( 'root','C:\\FileSystemeE\\work\\multimedia\\Audio\\poub\\') ";
		//MyDatabase.getSingleton().executeSimpleQuery( aQuery);

		
		MyDatabase.getSingleton().commit();
	}

	
	
	
	
	public 	void updateDataModelFromVersion( int currentVersion)
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
