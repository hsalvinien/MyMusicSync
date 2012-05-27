package com.salvinien.mymusicsync;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;


import com.salvinien.database.MyDatabase;

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

	protected void init()
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
	
	//ACCESSORS
	public Libelle getUnknown() 			{ return unknown;}
	public int getUnknownId() 				{ return unknown.getId();}
	public Libelle getLibelle(int anId)		{ return containerId.get(anId);}
	public Libelle getLibelle(String aName)	{ return containerName.get(aName);}
	public int 	   getId(String aName)		{ return containerName.get(aName).getId();}
	public String  getName(int anId)		{ return containerId.get(anId).getName();}

	
	
	public int[] getIds()
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

	
	public String[] getLibelles()
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

}
