package com.salvinien.playlists;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import com.salvinien.database.MyDatabase;

public class PlaylistContainer
{
	//Members
	protected HashMap< Integer, Playlist> containerId;
	protected String containerTableName;

	//Members
	protected static PlaylistContainer  mySingleton=null;

	//CTOR
	protected PlaylistContainer() 
	{
		//creates the physical containers
		containerId = new HashMap< Integer, Playlist>();
		containerTableName = "PlaylistValues";
		loadFromDB();
	}

	
	
	//ACCESSORS
	public static PlaylistContainer  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new PlaylistContainer();
		
		return mySingleton;
	}
	public Playlist getPlaylist(int anId)
	{			
		return containerId.get(anId);
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
			    int PlaylistId  = rs.getInt("PlaylistId");
			    int SongId  	= rs.getInt("SongID");
			    
			    Playlist p = containerId.get(PlaylistId);
			    if( p==null)
			    	{
			    		p= new Playlist( PlaylistId);
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
		Iterator<Playlist> it = containerId.values().iterator();
		while( it.hasNext())
		{
			Playlist p = it.next();
			
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
}
