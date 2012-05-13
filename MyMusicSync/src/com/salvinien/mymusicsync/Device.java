package com.salvinien.mymusicsync;

public class Device
{
	protected int DeviceID;
	protected String DeviceName;
	protected String DefaultPath;
	protected int DeviceType;
	protected int PlaylistId;

	public Device( String aDeviceName, String aDefaultPath, int aDeviceType, int aPlaylistId)
	{
		init( -1, aDeviceName, aDefaultPath, aDeviceType, aPlaylistId);		
	}
	public Device( int aDeviceID, String aDeviceName, String aDefaultPath, int aDeviceType, int aPlaylistId)
	{
		init( aDeviceID, aDeviceName, aDefaultPath, aDeviceType, aPlaylistId);
	}
	public void init( int aDeviceID, String aDeviceName, String aDefaultPath, int aDeviceType, int aPlaylistId)
	{
		DeviceID = aDeviceID;
		DeviceName = aDeviceName;
		DefaultPath = aDefaultPath;
		DeviceType = aDeviceType;
		PlaylistId = aPlaylistId;
	}



	//ACCESSORS
	public int 	  getDeviceID()		{ return DeviceID;}
	public String getDeviceName()	{ return  DeviceName;}
	public String getDefaultPath()	{ return  DefaultPath;}
	public int 	  getDeviceType()	{ return  DeviceType;}
	public int 	  getPlaylistId()	{ return  PlaylistId;}

	public void setDeviceID( int anId)		{ DeviceID  =anId;}
}
