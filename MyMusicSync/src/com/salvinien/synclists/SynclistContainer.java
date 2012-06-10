package com.salvinien.synclists;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import com.salvinien.database.MyDatabase;

/**
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
	public Synclist getSynclist(int anId) 			{ return containerId.get(anId);}
	public void addSynclist(Synclist aSyncList) 	{ containerId.put(aSyncList.getId(), aSyncList);}


	
	//Methods
	
	
	/**
	 * @method : void preFill()
	 *prefill the container of Synclist with existing synclist 
	 *without adding the songs id. It is for addressing the case of Synclist which haven't yet associated with songs
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
	
	/**
	 * @method : void loadFromDB()
	 * load the synclist from databse 
	 */
	private void loadFromDB()
	{
		preFill(); //case of Synclist not yet associated with songs
		
		String Query= " SELECT * FROM "+containerTableName;
		ResultSet rs = MyDatabase.getSingleton().executeQuery(Query);
				
	    //Extract data from result set
	    try
		{
			while(rs.next())
			{
			    //Retrieve by column name
			    int SynclistId  = rs.getInt("SynclistId");
			    int SongId  	= rs.getInt("SongID");
			    
			    Synclist p = containerId.get(SynclistId);
			    if( p==null)
			    	{
			    		p= new Synclist( SynclistId);
			    		containerId.put(SynclistId, p);
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
	
	
	/**
	 * @method : removeSongFromAllSynclist( int aSongId)
	 * remove a song from all Syncylists
	 * 
	 */
	public void removeSongFromAllSynclist( int aSongId)
	{
		//we remove the song from the container
		Iterator<Synclist> it = containerId.values().iterator();
		while( it.hasNext())
		{
			Synclist p = it.next();
			
			p.removeSong( aSongId);
			//even if the Synclist is associated with no more songs we still keep it
		}
		
		//we remove the song from the database
		String Query= " DELETE FROM "+containerTableName;	
		Query=  Query + " WHERE SongID='"+String.valueOf(aSongId)+"'";
		
		MyDatabase.getSingleton().executeSimpleQuery(Query);
		MyDatabase.getSingleton().commit();
	}
	
	
	
	
	/**
	 * @method : void save( Synclist aSyncList)
	 * save aSynlist in database
	 * 
	 */
	public void save( Synclist aSyncList)
	{
		//let's be bourrin
		
		//1) we delete the Synclist-songs from table
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


	/**
	 * @method : void delete( int aSyncListID)
	 * delete a synclist from the names container, from database AND from SynclisContainer
	 * 
	 * actually, to delete a synclist you can rather (if you prefer) call the delete from SynclistNamesContainer
	 * it is exactly the same, both methods work together and insure the same service  
	 */
	public void delete( int aSyncListID)
	{
		//0) precheck
		if( this.getSynclist(aSyncListID)==null)   return;
		
		//1) we remove it from from the container and its database table
		this.containerId.remove(aSyncListID);
		String Query = "DELETE FROM "+containerTableName+ " WHERE SynclistID="+aSyncListID;
		MyDatabase.getSingleton().executeSimpleQuery(Query);
		MyDatabase.getSingleton().commit();
		
		
		//2) delete from SynclistContainer
		SynclistContainer.getSingleton().delete( aSyncListID);
	}



}
