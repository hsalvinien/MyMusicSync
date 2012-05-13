package com.salvinien.mymusicsync;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

import com.salvinien.database.MyDatabase;

public class DeviceContainer
{

	//Members
	protected HashMap< Integer, Device> containerId;
	protected HashMap< String, Device> containerFileName;
	protected String containerTableName;

	//Members
	protected static DeviceContainer  mySingleton=null;

	//CTOR
	protected DeviceContainer() 
	{
		//creates the physical containers
		containerId = new HashMap< Integer, Device>();
		containerFileName = new HashMap< String, Device>();
		containerTableName = "Devices";
		init();
	}

	protected void init()
	{
		//load data from database
		try
		{
			loadFromDB();
		}
		catch (ParseException | SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	//ACCESSORS
	public static DeviceContainer  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new DeviceContainer();
		
		return mySingleton;
	}

	public Device getDevice(String aName)
	{	
		return containerFileName.get(aName);
	}


	
	
	//Methods
	protected void loadFromDB() throws ParseException, SQLException
	{
		String Query= " SELECT * FROM "+containerTableName;
		ResultSet rs = MyDatabase.getSingleton().executeQuery(Query);
		
		
	    //Extract data from result set
		while(rs.next())
		{
		    //Retrieve by column name
			int id  = rs.getInt("DeviceID");
		    String name = rs.getString("DeviceName");
		    String defaultPath  = rs.getString("DefaultPath");
		    int DeviceType  = rs.getInt("DeviceType");
		    int PlaylistId  = rs.getInt("PlaylistId");
			   
		    Device aDevice = new Device( id, name, defaultPath, DeviceType, PlaylistId);
		    containerId.put(id, aDevice);
		    containerFileName.put( name, aDevice);
		}
	    //Clean-up environment
	    rs.close();	     
	}
	

	
	public Device create( Device aDevice)
	{
		if( aDevice.getDeviceID() != -1) return aDevice; //if it already exists... then...
		
		//insert the album in the Database
		String aString;
		String Query = " INSERT INTO "+containerTableName+" (DeviceName, DefaultPath, DeviceType, PlaylistId) VALUES ('";

		aString = aDevice.getDeviceName(); 
		aString = aString.replace("'", "''");
		Query = Query + aString				+ "','";
		aString = aDevice.getDefaultPath(); 
		aString = aString.replace("'", "''");
		Query = Query + aString				+ "','";
		Query = Query + aDevice.getDeviceType()+ "','";
		Query = Query + aDevice.getPlaylistId()+ "')";
				
		MyDatabase.getSingleton().executeSimpleQuery(Query);

		MyDatabase.getSingleton().commit();

//		Song aSong = new Song( aSongName, aFileName, anArtistId, anAlbumId, anHashkey, aSize, aLastModification);
		//retreive the id
		aString = aDevice.getDeviceName(); 
		aString = aString.replace("'", "''");
		Query= " SELECT ID FROM "+containerTableName+" where DeviceName = '" + aString +"'";
		
		ResultSet rs = MyDatabase.getSingleton().executeQuery(Query);
		int id=0;
		
	    //Extract data from result set
	    try
		{
			while(rs.next())
			{
			    //Retrieve by column name
			    id  = rs.getInt("Id");
			    aDevice.setDeviceID(id);
			    containerId.put(id, aDevice);
			    containerFileName.put( aDevice.getDeviceName(), aDevice);
			}
		    //Clean-up environment
		    rs.close();
		}
		catch (SQLException e)	{	e.printStackTrace();}
		
		return aDevice;
	}

	
	public void update( Device aDevice)
	{
		String aString;

		//1) is the album already in the container?
		Device anObj = containerId.get(aDevice.getDeviceID());
		if( anObj == null)  return;  //no ... so we cannot update
		
		//update the album in the Database
		String Query = " UPDATE "+containerTableName+" set ";
		aString = aDevice.getDeviceName(); 
		aString = aString.replace("'", "''");
		Query = Query + " DeviceName = '" 		+ aString+ "', " ;

		aString = aDevice.getDefaultPath(); 
		aString = aString.replace("'", "''");
		Query = Query + " DefaultPath= '" 	+ aString+ "', " ;
		Query = Query + " DeviceType= '" 	+ aDevice.getDeviceType()+ "', " ;
		Query = Query + " PlaylistId= '" 	+ aDevice.getPlaylistId()+ "', " ;
		Query = Query + " where DeviceID = '"	+ aDevice.getDeviceID()+"'";
		MyDatabase.getSingleton().executeSimpleQuery(Query);		
		
		MyDatabase.getSingleton().commit();

		//remove the old song
		containerId.remove( anObj.getDeviceID());
		containerFileName.remove( anObj.getDeviceName());
		
		//add the new album
		containerId.put( aDevice.getDeviceID(), aDevice);
		containerFileName.put(aDevice.getDeviceName(), aDevice);
	}
	

}
