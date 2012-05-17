package com.salvinien.mymusicsync;

public class DeviceSyncList
{
	protected String DefaultPath;
	protected int PlaylistId;

	//CTOR
	public DeviceSyncList( String aDefaultPath, int aPlaylistId)
	{
		DefaultPath = aDefaultPath;
		PlaylistId = aPlaylistId;
	}
	
	//ACCESSORS
	public String getDefaultPath()  { return DefaultPath;}
	public int getPlaylistId()		{ return PlaylistId;}
	
}
