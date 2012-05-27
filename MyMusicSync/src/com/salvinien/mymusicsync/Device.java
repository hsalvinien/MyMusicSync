package com.salvinien.mymusicsync;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Vector;

import com.salvinien.database.MyDatabase;
import com.salvinien.synclists.Synclist;

public class Device
{
	protected int DeviceID;
	protected String DeviceName;
	protected int DeviceType;

	Vector<DeviceSyncList> container = new Vector<DeviceSyncList> ();
	
	
	public Device( String aDeviceName, int aDeviceType)
	{
		init( -1, aDeviceName, aDeviceType);		
	}
	public Device( int aDeviceID, String aDeviceName, int aDeviceType)
	{
		init( aDeviceID, aDeviceName, aDeviceType);
	}
	public void init( int aDeviceID, String aDeviceName, int aDeviceType)
	{
		DeviceID = aDeviceID;
		DeviceName = aDeviceName;
		DeviceType = aDeviceType;
	}



	//ACCESSORS
	public int 	  getDeviceID()		{ return DeviceID;}
	public String getDeviceName()	{ return  DeviceName;}
	public int 	  getDeviceType()	{ return  DeviceType;}

	public void setDeviceID( int anId)		{ DeviceID  =anId;}

	public Vector<DeviceSyncList> getDeviceSyncLis() { return container;}


	//Methods
	protected void loadMoreFromDB() throws ParseException, SQLException
	{
		String Query= " SELECT * FROM DeviceSyncList where DeviceID =" + String.valueOf(DeviceID);
		ResultSet rs = MyDatabase.getSingleton().executeQuery(Query);
		
		
	    //Extract data from result set
		while(rs.next())
		{
		    //Retrieve by column name
			int PlaylistId  	= rs.getInt("PlaylistId");
		    String DefaultPath  = rs.getString("DefaultPath");

		    DeviceSyncList aDeviceSyncList = new DeviceSyncList( DefaultPath, PlaylistId);
		    container.add(aDeviceSyncList);
		}

	    rs.close();	     
	}

	
	
	//remove a playlist from the deivce
	public void unAssociateSyncList( Synclist aSynclist)
	{
		//we itarate throuhgt the container
		for( int i =0; i< container.size(); i++)
		{
			//when we have the playslist we are looking up
			if( container.get(i).getPlaylistId() == aSynclist.getId())
			{
				//we remove it fron the container
				container.remove(i);
				
				// and from the database
				String Query= " DELETE FROM DeviceSyncList ";	
				Query=  Query + " WHERE DeviceID='"+String.valueOf(this.DeviceID)+"'  and PlaylistId='"+String.valueOf(aSynclist.getId())+"'";
								
				MyDatabase.getSingleton().executeSimpleQuery(Query);
				MyDatabase.getSingleton().commit();
							
				return;
			}
		}
	}

}
