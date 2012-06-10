package com.salvinien.mymusicsync;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Vector;

import com.salvinien.database.MyDatabase;
import com.salvinien.synclists.DeviceSyncList;
import com.salvinien.synclists.Synclist;



/**
 * @class: Device
 * 
 * This class manages a Device 
 * 
 * 
 */
public class Device
{
	protected int DeviceID;				//device id
	protected String DeviceName;		//device name, the one which is shown in the interface
	protected int DeviceType;			//device type, not used until now, but will be soon (ext hd, usb hd, network etc etc)

	Vector<DeviceSyncList> container = new Vector<DeviceSyncList> ();		//a Device may have several synclists (cf my SGII, has two usdb hosts)
																			//this allow to have several mount points for a single device 
	
	
	//different CTORs
	public Device( String aDeviceName, int aDeviceType)
	{
		init( -1, aDeviceName, aDeviceType);		
	}
	public Device( int aDeviceID, String aDeviceName, int aDeviceType)
	{
		init( aDeviceID, aDeviceName, aDeviceType);
	}
	
	

	//ACCESSORS
	public int 	  getDeviceID()		{ return DeviceID;}
	public String getDeviceName()	{ return  DeviceName;}
	public int 	  getDeviceType()	{ return  DeviceType;}
	public void setDeviceID( int anId)		{ DeviceID  =anId;}
	public Vector<DeviceSyncList> getDeviceSyncLis() { return container;}


	//Methods
	/**
	 * @method : void init( int aDeviceID, String aDeviceName, int aDeviceType)
	 * 
	 *   just init the members 
	 */
	private void init( int aDeviceID, String aDeviceName, int aDeviceType)
	{
		DeviceID = aDeviceID;
		DeviceName = aDeviceName;
		DeviceType = aDeviceType;
	}


	/**
	 * @method : void loadMoreFromDB() throws ParseException, SQLException
	 * 
	 *   loads the synclists from the database 
	 */
	protected void loadMoreFromDB() throws ParseException, SQLException
	{
		String Query= " SELECT * FROM DeviceSyncList where DeviceID =" + String.valueOf(DeviceID);
		ResultSet rs = MyDatabase.getSingleton().executeQuery(Query);
		
		
	    //Extract data from result set
		while(rs.next())
		{
		    //Retrieve by column name
			int SynclistId  	= rs.getInt("SynclistId");
		    String DefaultPath  = rs.getString("DefaultPath");

		    DeviceSyncList aDeviceSyncList = new DeviceSyncList( DefaultPath, SynclistId);
		    container.add(aDeviceSyncList);
		}

	    rs.close();	     
	}

	
	/**
	 * @method : void unAssociateSyncList( Synclist aSynclist)
	 * 
	 *   remove a Synclist from the device 
	 */	
	public void unAssociateSyncList( Synclist aSynclist)
	{
		//we iterate throught the container
		for( int i =0; i< container.size(); i++)
		{
			//when we have the Syncslist we are looking up
			if( container.get(i).getSynclistId() == aSynclist.getId())
			{
				//we remove it fron the container
				container.remove(i);
				
				// and from the database
				String Query= " DELETE FROM DeviceSyncList ";	
				Query=  Query + " WHERE DeviceID='"+String.valueOf(this.DeviceID)+"'  and SynclistId='"+String.valueOf(aSynclist.getId())+"'";
								
				MyDatabase.getSingleton().executeSimpleQuery(Query);
				MyDatabase.getSingleton().commit();
							
				return;
			}
		}
	}

	
	/**
	 * @method : addDeviceSyncList( DeviceSyncList aDeviceSyncList)
	 * 
	 *   add a syncList to a device and save it to database 
	 */	
	public void addDeviceSyncList( DeviceSyncList aDeviceSyncList)
	{
		//1) add the device to the container
		container.add(aDeviceSyncList);
		
		//2) save it to database
		String query = " Insert into DeviceSyncList ('DeviceID' , 'DefaultPath', 'SynclistId') VALUES ('";
		query += String.valueOf(this.getDeviceID())+"' , '"+aDeviceSyncList.getDefaultPath()+"', '"+String.valueOf(aDeviceSyncList.getSynclistId())+"') ";
			
		MyDatabase.getSingleton().executeSimpleQuery(query);
		MyDatabase.getSingleton().commit();
	}
}
