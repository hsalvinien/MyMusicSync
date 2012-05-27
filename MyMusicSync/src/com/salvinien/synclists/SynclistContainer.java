package com.salvinien.synclists;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import com.salvinien.database.MyDatabase;

/*
 * @class: SynclistContainer
 * 
 * This class manages synclists
 * 
 * it is implemented has a singleton
 * 
 */

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
	public Synclist getPlaylist(int anId) 	{ return containerId.get(anId);}


	
	//Methods
	
	
	/*@method : void preFill()
	 *prefill the container of Synclist with existing synclist 
	 *without adding the songs id. It is for addressing the case of playlist which haven't yet associated with songs
	 */
	private void preFill()
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
	
	/*@method : void loadFromDB()
	 * load the synclist from databse 
	 */
	private void loadFromDB()
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
	
	
	/*@method : removeSongFromAllPlaylist( int aSongId)
	 * remove a song from all playlists
	 * 
	 */
	public void removeSongFromAllPlaylist( int aSongId)
	{
		//we remove the song from the container
		Iterator<Synclist> it = containerId.values().iterator();
		while( it.hasNext())
		{
			Synclist p = it.next();
			
			p.removeSong( aSongId);
			//even if the playlist is associated with no more songs we still keep it
		}
		
		//we remove the song from the database
		String Query= " DELETE FROM "+containerTableName;	
		Query=  Query + " WHERE SongID='"+String.valueOf(aSongId)+"'";
		
		MyDatabase.getSingleton().executeSimpleQuery(Query);
		MyDatabase.getSingleton().commit();
	}
	
	
	
	
	/*@method : void save( Synclist aSyncList)
	 * save aSynlist in database
	 * 
	 */
	public void save( Synclist aSyncList)
	{
		//let's be bourrin
		
		//1) we delete the playlist-songs from table
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
