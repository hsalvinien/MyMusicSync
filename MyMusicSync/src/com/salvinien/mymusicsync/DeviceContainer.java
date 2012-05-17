package com.salvinien.mymusicsync;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

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

	public String[] getDevices()
	{
		
		Collection<Device> aCollection = containerFileName.values();
		String[] devices = new String[ aCollection.size()];
		
		int i =0;
		Iterator<Device> it = aCollection.iterator();
		while( it.hasNext())
		{
			devices[i] = it.next().getDeviceName();
			i++;
		}
		
		return devices;
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
		    int DeviceType  = rs.getInt("DeviceType");
			   
		    Device aDevice = new Device( id, name, DeviceType);
		    aDevice.loadMoreFromDB();

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
		String Query = " INSERT INTO "+containerTableName+" (DeviceName, DeviceType) VALUES ('";

		aString = aDevice.getDeviceName(); 
		aString = aString.replace("'", "''");
		Query = Query + aString				+ "','";
//		aString = aDevice.getDefaultPath(); 
//		aString = aString.replace("'", "''");
		Query = Query + aString				+ "','";
		Query = Query + aDevice.getDeviceType()+ "')";
				
		MyDatabase.getSingleton().executeSimpleQuery(Query);

		
		System.out.println(" @TODO ajouter les inserts des listes de synchro en base "+
		" necessitera de daire le commit plus loin pour recup de l'id du device pour pouvoir faire les inserts"+
		" en gardant la notion du point de commit/rollback");
		
		MyDatabase.getSingleton().commit();

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

		Query = Query + " DefaultPath= '" 	+ aString+ "', " ;
		Query = Query + " DeviceType= '" 	+ aDevice.getDeviceType()+ "', " ;
		Query = Query + " where DeviceID = '"	+ aDevice.getDeviceID()+"'";
		MyDatabase.getSingleton().executeSimpleQuery(Query);		

		
		System.out.println("@TODO : ajouter l'update sur les listes de synchros du device!!!"); 

		MyDatabase.getSingleton().commit();

		//remove the old song
		containerId.remove( anObj.getDeviceID());
		containerFileName.remove( anObj.getDeviceName());
		
		//add the new album
		containerId.put( aDevice.getDeviceID(), aDevice);
		containerFileName.put(aDevice.getDeviceName(), aDevice);
	}
	

}
