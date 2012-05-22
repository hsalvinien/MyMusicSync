package com.salvinien.discography;

import java.io.IOException;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.salvinien.database.MyDatabase;
import com.salvinien.mymusicsync.Parameters;
import com.salvinien.synclists.SynclistContainer;


public class SongContainer
{
	//Members
	protected HashMap< Integer, Song> containerId;
	protected HashMap< String, Song> containerFileName;
	protected String containerTableName;
	protected static SongContainer  mySingleton=null;

	//CTOR
	protected SongContainer() 
	{
		//creates the physical containers
		containerId = new HashMap< Integer, Song>();
		containerFileName = new HashMap< String, Song>();
		containerTableName = "Songs";
		init();
	}

	protected void init()
	{
		//load data from database
		try
		{
			loadFromDB();
		}
		catch (ParseException | SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	//ACCESSORS
	public static SongContainer  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new SongContainer();
		
		return mySingleton;
	}

	public Song getSong( int anId)
	{			
		return containerId.get(anId);
	}

	public Song getSong( String aFileName)
	{			
		return containerId.get(getSongByFileName( aFileName));
	}

	public int getSongByFileName( String aFileName)
	{
		Song aSong = containerFileName.get(aFileName);
		if(aSong == null) return -1;
		
		return aSong.getId();
	}
	
	public Iterator<Song> getSongIteratorID() { return containerId.values().iterator();}
	
	
	//Methods
	
	public int getNbOfAlbums( int artistID) 
	{
		int nb=0;
		
	
		try
		{
			nb = getNbOfAlbumsFromDB( artistID);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return nb;
	}
	public int getNbOfSongs( int artistID) 
	{
		int nb=0;
		
	
		try
		{
			nb = getNbOfSongsFromDB( artistID);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return nb;
	}
	
	protected int getNbOfAlbumsFromDB( int artistID) throws SQLException
	{
		int nb=0;
		
		String Query= " SELECT count(distinct AlbumID) FROM "+containerTableName +" where AlbumID <> 1 and ArtistID = "+ String.valueOf(artistID);
		ResultSet rs = MyDatabase.getSingleton().executeQuery(Query);
		
		
	    //Extract data from result set
		while(rs.next())
		{
		    //Retrieve by column name
		    nb= rs.getInt(1);
		}
	    //Clean-up environment
	    rs.close();	     
		
	    return nb;
	}

	protected int getNbOfSongsFromDB( int artistID) throws SQLException
	{
		int nb=0;
		
		String Query= " SELECT count(*) FROM "+containerTableName +" where ArtistID = "+ String.valueOf(artistID);
		ResultSet rs = MyDatabase.getSingleton().executeQuery(Query);
		
		
	    //Extract data from result set
		while(rs.next())
		{
		    //Retrieve by column name
		    nb= rs.getInt(1);
		}
	    //Clean-up environment
	    rs.close();	     
		
	    return nb;
	}

	
	
	protected void loadFromDB() throws ParseException, SQLException
	{
		String Query= " SELECT * FROM "+containerTableName;
		ResultSet rs = MyDatabase.getSingleton().executeQuery(Query);
		
		
	    //Extract data from result set
		while(rs.next())
		{
		    //Retrieve by column name
		    int id  = rs.getInt("Id");
		    String name = rs.getString("Name");
		    String fileName = rs.getString("FileName");
		    int idArtist  = rs.getInt("ArtistID");
		    int idAlbum  = rs.getInt("AlbumID");
		    int hashkey  = rs.getInt("Hashkey");
		    long size  = rs.getLong("size");
		    String S1= rs.getString("LastModification");
		    Date LastModification = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" ).parse(S1);
			   
		    Song aSong = new Song( id, name, fileName, idArtist, idAlbum, hashkey, size, LastModification);
		    containerId.put(id, aSong);
		    containerFileName.put(fileName, aSong);
		}
	    //Clean-up environment
	    rs.close();	     
	}
	

	public Song create( Song aSong)
	{
		if( aSong.getId() != -1) return aSong; //if it already exists... then...
		
		//insert the album in the Database
		String aString;
		String Query = " INSERT INTO "+containerTableName+" (Name, FileName, ArtistID, Hashkey, Size, LastModification, AlbumID) VALUES ('";

		aString = aSong.getName(); 
		aString = aString.replace("'", "''");
		Query = Query + aString				+ "','";
		aString = aSong.getFileName(); 
		aString = aString.replace("'", "''");
		Query = Query + aString				+ "','";
		Query = Query + aSong.getArtistID()	+ "','";
		Query = Query + aSong.getHashkey()	+ "','";
		Query = Query + aSong.getSize()		+ "','";
		Query = Query + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(aSong.getLastModification()) + "','";
		Query = Query + aSong.getAlbumID()	+ "')";
				
		MyDatabase.getSingleton().executeSimpleQuery(Query);

		MyDatabase.getSingleton().commit();

//		Song aSong = new Song( aSongName, aFileName, anArtistId, anAlbumId, anHashkey, aSize, aLastModification);
		//retreive the id
		aString = aSong.getFileName(); 
		aString = aString.replace("'", "''");
		Query= " SELECT ID FROM "+containerTableName+" where FileName = '" + aString +"'";
		
		ResultSet rs = MyDatabase.getSingleton().executeQuery(Query);
		int id=0;
		
	    //Extract data from result set
	    try
		{
			while(rs.next())
			{
			    //Retrieve by column name
			    id  = rs.getInt("Id");
			    aSong.setId(id);
			    containerId.put(id, aSong);
			    containerFileName.put( aSong.getFileName(), aSong);
			}
		    //Clean-up environment
		    rs.close();
		}
		catch (SQLException e)	{	e.printStackTrace();}
		
		return aSong;
	}

	
	public void update( Song aSong)
	{
		String aString;

		//1) is the album already in the container?
		Song anObj = containerId.get(aSong.getId());
		if( anObj == null)  return;  //no ... so we cannot update
		
		//update the album in the Database
		String Query = " UPDATE "+containerTableName+" set ";
		aString = aSong.getName(); 
		aString = aString.replace("'", "''");
		Query = Query + " Name= '" 		+ aString+ "', " ;

		aString = aSong.getFileName(); 
		aString = aString.replace("'", "''");
		Query = Query + " FileName= '" 	+ aString+ "', " ;
		Query = Query + " ArtistID= '" 	+ aSong.getArtistID()+ "', " ;
		Query = Query + " Hashkey= '" 	+ aSong.getHashkey()+ "', " ;
		Query = Query + " Size= '" 		+ aSong.getSize()+ "', " ;
		Query = Query + " AlbumID= '" 	+ aSong.getAlbumID()+ "', " ;
		Query = Query + " LastModification= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(aSong.getLastModification())+ "' " ;
		Query = Query + " where Id = '"	+ aSong.getId()+"'";
		MyDatabase.getSingleton().executeSimpleQuery(Query);
		
		MyDatabase.getSingleton().commit();

		//remove the old song
		containerId.remove( anObj.getId());
		containerFileName.remove( anObj.getFileName());
		
		//add the new album
		containerId.put( aSong.getId(), aSong);
		containerFileName.put(aSong.getFileName(), aSong);
	}

	
	public int getNewSongs( HashMap<String, Song>  vSongs)
	{
		int nbOfNewSongs=0;
		Iterator<Song> it = vSongs.values().iterator();
		Song aSong=null;
		
		while(it.hasNext())
		{
			aSong = it.next();
			
			Song aSong1 = containerFileName.get( aSong.getFileName());
			if(aSong1 !=null)
			{
				//so the song is already in databasee 
			}
			else
			{
				//so the song is not yet in database
				try
				{
					aSong.updateInformationFromFile(Parameters.getSingleton().getRoot());
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.create(aSong);
				it.remove();
				nbOfNewSongs++;
			}

		}
		
		return nbOfNewSongs;
	}
	
	
	public int getUpdatedSongs( HashMap<String, Song>  vSongs)
	{
		int nbOfSongs=0;
		Iterator<Song> it = vSongs.values().iterator();
		Song aSong=null;
		
		while(it.hasNext())
		{
			aSong = it.next();
			
			Song aSong1 = containerFileName.get( aSong.getFileName());
			if(aSong1 !=null)
			{
				//so the song is already in databasee 
				if( aSong1.updateInformationFromSong( aSong))
				{
					//yes we have to update the database
					this.update(aSong1);
					nbOfSongs++;
				}
				
				it.remove();
			}

		}
		
		return nbOfSongs;
	}

	
	public int getRemovedSongs(  HashMap<String, Song>  vSongs)
	{
		int nbOfSongs=0;

		Iterator<Song> it = containerId.values().iterator();
		
		while( it.hasNext())
		{
			Song aSong = it.next();
			
			Song s = vSongs.get(aSong.getFileName());
			if( s==null)
			{//Ok it means we have a song in our database that is no more in the system
				//so we remove it from the database->songs AND database->Playlists
				//@TODO => we could check that is not just file move in using the hashkey and data and size and name
		
				//1) we remove the song from the containers
				it.remove(); //from the id containers
				containerFileName.remove(aSong.getFileName()); //from the filename containers
				
				//2) we remove the song from the playlists
				SynclistContainer.getSingleton().removeSongFromAllPlaylist(aSong.getId());
				
				//3) from the database
				String Query= " DELETE FROM "+containerTableName;	
				Query=  Query + " WHERE ID='"+String.valueOf(aSong.getId())+"'";
				
				MyDatabase.getSingleton().executeSimpleQuery(Query);
				MyDatabase.getSingleton().commit();
				
				
				nbOfSongs++;
			}
		}
		
		
		return nbOfSongs;
	}

	
	
	
	public Vector<Integer> getSongByAlbum( int albumId)
	{
		Vector<Integer> v = new Vector<Integer>();

		Iterator<Song> it = containerId.values().iterator();
		
		while( it.hasNext())
		{
			Song aSong = it.next();
			
			if( aSong.getAlbumID()==albumId) v.add(aSong.getId());
		}
		
		return v;
	}


	public Vector<Integer> getSongByArtist( int artistId)
	{
		Vector<Integer> v = new Vector<Integer>();

		Iterator<Song> it = containerId.values().iterator();
		
		while( it.hasNext())
		{
			Song aSong = it.next();
			
			if( aSong.getArtistID()==artistId) v.add(aSong.getId());
		}
		
		return v;
	}

	
}
