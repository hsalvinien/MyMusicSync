package com.salvinien.synclists;

import java.io.File;

/*
 * @class: DeviceSyncList
 * 
 * This class manages a DeviceSyncList
 * 
 * A device Synclist, represents the relationships between a Device and SyncList 
 * 
 */
public class DeviceSyncList
{
	protected String DefaultPath;  	//this is the mount point
	protected int SynclistId;		//id 

	//CTOR
	public DeviceSyncList( String aDefaultPath, int aSynclistId)
	{
		DefaultPath = aDefaultPath;
		
		//1) first we check that the last char is realy the file separator, if not we add it
		char a= DefaultPath.charAt( DefaultPath.length()-1);
		if( a!=File.separatorChar)
		{
			DefaultPath+=File.separatorChar;
		}
		
		//2) we check that if the path has a form like x:\\ewewewe  alors the first letter has to be capitalized!
		a = DefaultPath.charAt(1);
		if(a ==':' )
		{
			a = DefaultPath.charAt(0);
			a = Character.toUpperCase(a);
			DefaultPath = a + DefaultPath.substring(1);
		}
		
		SynclistId = aSynclistId;
	}
	
	//ACCESSORS
	public String getDefaultPath()  { return DefaultPath;}
	public int getSynclistId()		{ return SynclistId;}
	
}
