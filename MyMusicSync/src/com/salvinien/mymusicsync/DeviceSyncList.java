package com.salvinien.mymusicsync;

import java.io.File;

public class DeviceSyncList
{
	protected String DefaultPath;
	protected int SynclistId;

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
