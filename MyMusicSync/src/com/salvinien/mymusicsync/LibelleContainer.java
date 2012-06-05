package com.salvinien.mymusicsync;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;


import com.salvinien.database.MyDatabase;


/*
 * @class: LibelleContainer
 * 
 * This class manages a container of  Libelle,  
 * 
 * this is the mother  class for all containers like artistContainer etc etc
 * 
 */

public class LibelleContainer
{
	//Members
	protected HashMap< Integer, Libelle> containerId;
	protected HashMap< String, Libelle> containerName;
	protected String containerTableName;
	protected Libelle unknown;

	//CTOR
	protected LibelleContainer( String aTableName) 
	{
		//creates the physical containers
		containerId = new HashMap< Integer, Libelle>();
		containerName = new HashMap< String, Libelle>();
		containerTableName = aTableName;
		unknown=null;
		init();
	}

	
	//ACCESSORS
	public Libelle getUnknown() 			{ return unknown;}
	public int getUnknownId() 				{ return unknown.getId();}
	public Libelle getLibelle(int anId)		{ return containerId.get(anId);}
	public Libelle getLibelle(String aName)	{ return containerName.get(aName);}
	public int 	   getId(String aName)		{ return containerName.get(aName).getId();}
	public String  getName(int anId)		{ return containerId.get(anId).getName();}
	
	public int[] getIds()			// returns an array of ids	
	{
		int[] v = new int[ containerId.values().size()];
		Iterator<Libelle> it = containerId.values().iterator();
		
		int i=0;
		while(it.hasNext())
		{
			v[i] = it.next().getId();
			i++;
		}
		
		return v;
	}

	
	public String[] getLibelles()	// returns an array of libelles
	{
		String[] v = new String[ containerId.values().size()];
		Iterator<Libelle> it = containerId.values().iterator();
		
		int i=0;
		while(it.hasNext())
		{
			v[i] = it.next().getName();
			i++;
		}
		
		return v;
	}

	
	//Methods
	/*@method : void init( int aDeviceID, String aDeviceName, int aDeviceType)
	 * 
	 *   just init the container 
	 */
	private void init()
	{
		//load data from database
		loadFromDB();
		
		//load the specif "Unknown" element
		unknown = containerName.get("Unknown");
		if( unknown ==null)
		{
			unknown = this.create("Unknown");
		}		
	}
	

	/*@method : void loadFromDB()
	 * 
	 *   loads data from database to the container 
	 */
	protected void loadFromDB()
	{
		String Query= " SELECT * FROM "+containerTableName;
		
		ResultSet rs = MyDatabase.getSingleton().executeQuery(Query);
		
		
	    //Extract data from result set
	    try
		{
			while(rs.next())
			{
			    //Retrieve by column name
			    int id  = rs.getInt("Id");
			    String name = rs.getString("Name");
			    Libelle obj = new Libelle( id, name);
			    containerId.put(id, obj);
			    containerName.put(name, obj);
			}
		    //Clean-up environment
		    rs.close();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	}
	
	
	/*@method : Libelle create( String aName)
	 * 
	 *   create a Libelle from the name (actully create it in database, gives an id etc etc 
	 */
	public Libelle create( String aName)
	{
		//1) is the album already in the container?
		Libelle obj = containerName.get(aName);
		if( obj != null)  return obj;

		//insert the album in the Database
		String aString = aName; 
		aString = aString.replace("'", "''");
		String Query = " INSERT INTO "+containerTableName+" (Name) VALUES ('" + aString + "')";
		MyDatabase.getSingleton().executeSimpleQuery(Query);

		MyDatabase.getSingleton().commit();

		
		//retreive the id
		Query= " SELECT * FROM "+containerTableName+" where Name = '" + aString +"'";
		
		ResultSet rs = MyDatabase.getSingleton().executeQuery(Query);
		int id=0;
	    //Extract data from result set
	    try
		{
			while(rs.next())
			{
			    //Retrieve by column name
			    id  = rs.getInt("Id");
			    obj = new Libelle( id, aName);
			    containerId.put(id, obj);
			    containerName.put(aName, obj);
			}
		    //Clean-up environment
		    rs.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return obj;
	}

	
	/*@method : void update( Libelle anObj)
	 * update a libelle in database 
	 */
	public void update( Libelle anObj)
	{
		//1) is the album already in the container?
		Libelle obj = containerId.get(anObj.getId());
		if( obj == null)  return;  //no ... so we cannot update
		
		//2) are trying to update the 'Unknown' ?
		if( anObj.getId() == unknown.getId())  return; //yes...so we cannot update

		//update the album in the Database
		String Query = " UPDATE "+containerTableName+" set Name= '" + anObj.getName()+ "' where Id = "+ anObj.getId();
		MyDatabase.getSingleton().executeSimpleQuery(Query);
		
		MyDatabase.getSingleton().commit();

		//remove the old album
		containerId.remove( obj.getId());
		containerName.remove( obj.getName());

		//add the new album
		containerId.put( anObj.getId(), anObj);
		containerName.put( anObj.getName(), anObj);
	}

	
	/*@method : void delete( int anID)
	 * delete a libelle from the container, from database 
	 */
	public void delete( int anID)
	{
		//1) first we remove it from the hashmaps
		Libelle aLibelle = containerId.get(anID);
		containerName.remove(aLibelle.getName());
		containerId.remove(anID);

		//2) we remove it from the database
		String Query = " DELETE FROM "+containerTableName+  " WHERE Id = "+ anID;
		MyDatabase.getSingleton().executeSimpleQuery(Query);		
		MyDatabase.getSingleton().commit();
		
		
	}

	
}
