package com.salvinien.mymusicsync;

public class DeviceSyncList
{
	protected String DefaultPath;
	protected int SynclistId;

	//CTOR
	public DeviceSyncList( String aDefaultPath, int aSynclistId)
	{
		DefaultPath = aDefaultPath;
		SynclistId = aSynclistId;
	}
	
	//ACCESSORS
	public String getDefaultPath()  { return DefaultPath;}
	public int getSynclistId()		{ return SynclistId;}
	
}
