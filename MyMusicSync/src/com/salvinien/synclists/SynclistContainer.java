package com.salvinien.synclists;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import com.salvinien.database.MyDatabase;

public class SynclistContainer
{
	//Members
	protected HashMap< Integer, Synclist> containerId;
	protected String containerTableName;

	//Members
	protected static SynclistContainer  mySingleton=null;

	//CTOR
	protected SynclistContainer() 
	{
		//creates the physical containers
		containerId = new HashMap< Integer, Synclist>();
		containerTableName= "SynclistValues";
		loadFromDB();
	}

	
	
	//ACCESSORS
	public static SynclistContainer  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new SynclistContainer();
		
		return mySingleton;
	}
	public Synclist getPlaylist(int anId)
	{			
		return containerId.get(anId);
	}


	
	//Methods
	
	
	//prefill the container of Synclist with existing synclist 
	//without adding the songs id. It is for addressing the case of playlist chich haven't yet associated with songs
	protected void preFill()
	{
		int[] ids = SynclistNamesContainer.getSingleton().getIds();
		
		for( int i=0; i<ids.length; i++)
		{
		    Synclist p = containerId.get(ids[i]);
		    if( p==null)
		    	{
		    		p= new Synclist( ids[i]);
		    		containerId.put(ids[i], p);
		    	}
		    
			
		}
		
	}
	
	protected void loadFromDB()
	{
		preFill(); //case of playlist not yet associated with songs
		
		String Query= " SELECT * FROM "+containerTableName;
		ResultSet rs = MyDatabase.getSingleton().executeQuery(Query);
				
	    //Extract data from result set
	    try
		{
			while(rs.next())
			{
			    //Retrieve by column name
			    int PlaylistId  = rs.getInt("SynclistId");
			    int SongId  	= rs.getInt("SongID");
			    
			    Synclist p = containerId.get(PlaylistId);
			    if( p==null)
			    	{
			    		p= new Synclist( PlaylistId);
			    		containerId.put(PlaylistId, p);
			    	}
			    
			    p.addSong(SongId);
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
	
	
	public void removeSongFromAllPlaylist( int anId)
	{
		//we remove the song from the container
		Iterator<Synclist> it = containerId.values().iterator();
		while( it.hasNext())
		{
			Synclist p = it.next();
			
			p.removeSong( anId);
			if(p.getSize()==0) //if the playlist has no more items we remove it from the container
			{
				it.remove();
			}
		}
		
		//we remove the song from the database
		String Query= " DELETE FROM "+containerTableName;	
		Query=  Query + " WHERE ID='"+String.valueOf(anId)+"'";
		
		MyDatabase.getSingleton().executeSimpleQuery(Query);
		MyDatabase.getSingleton().commit();
	}
	
	
	public void save( Synclist aSyncList)
	{
		//let's be bourrin
		
		//1) we delete the songs from table
		String Query= " DELETE FROM "+containerTableName;	
		Query=  Query + " WHERE SynclistID='"+String.valueOf(aSyncList.getId())+"'";
		
		MyDatabase.getSingleton().executeSimpleQuery(Query);
	    
	    
	    //2) we reinsert all
		Iterator<Integer> it = aSyncList.iterator();
		int ID = aSyncList.getId();
		while( it.hasNext())
		{
			int i = it.next();
			
			Query = "INSERT INTO "+containerTableName+ " (SynclistID, SongId) VALUES "+"('"+  String.valueOf(ID) +"','"+  String.valueOf(i) +"')";
			
			MyDatabase.getSingleton().executeSimpleQuery(Query);
		}
			
		MyDatabase.getSingleton().commit();
	}
}
