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

/*
 * @class: SongContainer
 * 
 * This class manages SOngs
 
 * it is implemented as a singleton (Design pattern)
 * 
 * 
 *
 */
public class SongContainer
{
	//Members
	protected HashMap< Integer, Song> containerId;			//this container references songs by their id
	protected HashMap< String, Song> containerFileName;		//this container references songs by their FILE NAME
	protected String containerTableName;					//this is the table name in the database
	protected static SongContainer  mySingleton=null;

	//CTOR
	//private to forbid the creation of instances but from getSingleton
	private SongContainer() 
	{
		//creates the physical containers
		containerId = new HashMap< Integer, Song>();
		containerFileName = new HashMap< String, Song>();
		containerTableName = "Songs";
		init();
	}

	
	//ACCESSORS
	//accessor that gives the only instance of the  class 
	public static SongContainer  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new SongContainer();
		
		return mySingleton;
	}

	public Iterator<Song> getSongIteratorID() { return containerId.values().iterator();}				//returns an iterator of song (on containerId)
	public Song getSong( int anId) 			{ return containerId.get(anId);}  							//returns a song given its id	(null if not found)
	public Song getSong( String aFileName)	{ return containerId.get(getSongByFileName( aFileName));}	//returns a song given its filename	(null if nor found)
	public int getSongByFileName( String aFileName)														//returns the id of a song gibe its filename (-1 if not found)
	{
		Song aSong = containerFileName.get(aFileName);
		if(aSong == null) return -1;
		
		return aSong.getId();
	}
	
	
	
	//Methods
	/*@method : init
	 * inits the container, ie loads data fron the database
	 */
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

	
	
	//Methods
	/*@method : int getNbOfAlbums( int artistID)
	 * returns the number of albums given an artist id, this is done by requesting the database
	 * in case of any problem, it returns -1
	 */
	public int getNbOfAlbums( int artistID) 
	{
		int nb=0;
		try  						{ nb = getNbOfAlbumsFromDB( artistID);}
		catch (SQLException e)		{ nb=-1;}
				
		return nb;
	}

	/*@method : int getNbOfAlbumsFromDB( int artistID)
	 * returns the number of albums given an artist id, this is done by requesting the database
	 * 
	 * this methods is called by getNbOfAlbums, and  is not visible outside from the class (private
	 */
	private int getNbOfAlbumsFromDB( int artistID) throws SQLException
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

	
	
	/*@method : int getNbOfSongs( int artistID)
	 * returns the number of songs given an artist id, this is done by requesting the database
	 * in case of any problem, it returns -1
	 */
	public int getNbOfSongs( int artistID) 
	{
		int nb=0;
		try						{ nb = getNbOfSongsFromDB( artistID);}
		catch (SQLException e)	{ nb = -1;}
		
		return nb;
	}
	
	
	/*@method : int getNbOfSongsFromDB( int artistID)
	 * returns the number of dongss given an artist id, this is done by requesting the database
	 * 
	 * this methods is called by getNbOfSongs, and  is not visible outside from the class (private
	 */
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

	
	
	
	/*@method : loadFromDB
	 * loads data from database
	 * 
	 * this methods is called by Init, and  is not visible outside from the class (private
	 */
	private void loadFromDB() throws ParseException, SQLException
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
			   
		    //create an instance of snog
		    Song aSong = new Song( id, name, fileName, idArtist, idAlbum, hashkey, size, LastModification);
		    
		    //add the songs to both containers
		    containerId.put(id, aSong);
		    containerFileName.put(fileName, aSong);
		}
	    //Clean-up environment
	    rs.close();	     
	}
	

	/*@method : Song create( Song aSong)
	 * create a song in the database, it returns the song with its new id
	 * 
	 */
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

	
	/*@method : update( Song aSong)
	 * update a song in the database AND in both containers
	 * 
	 */
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
		
		//add the new song
		containerId.put( aSong.getId(), aSong);
		containerFileName.put(aSong.getFileName(), aSong);
	}

	
	
	
	/*@method : int addNewSongs( HashMap<String, Song>  vSongs)
	 * add new songs in the container (and the database), the potential songs are coming from an hashmap of songs
	 * 
	 * returns the number of songs which have been added to the song container form the Hasmap received in parameter
	 * the songs which were new are removed form vSong
	 */
	public int addNewSongs( HashMap<String, Song>  vSongs)
	{
		int nbOfNewSongs=0;
		Iterator<Song> it = vSongs.values().iterator(); //get an iterator from the Hashmap of potentiel new songs
		Song aSong=null;
		
		//we iterate through vSong
		while(it.hasNext())
		{
			aSong = it.next(); //get the next song
			
			Song aSong1 = containerFileName.get( aSong.getFileName()); //is the song already in the container (if yes we do nothing)
			if(aSong1 ==null)
			{
				//so the song is not yet in database
				try
				{ // we retrieve information from the file directly
					aSong.updateInformationFromFile(Parameters.getSingleton().getRoot());
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				this.create(aSong); //we create the song in Database and add it to both container (id, filename)
				
				it.remove(); // we remove it form vSong
				
				nbOfNewSongs++; //+1 in the number of new songs
			}

		}//end while
		
		return nbOfNewSongs;
	}
	
	
	
	
	/*@method : int updateSongs( HashMap<String, Song>  vSongs)
	 * update songs in the container (and the database), the potential newer songs are coming from an hashmap of songs
	 * 
	 * returns the number of songs which have been updated in the song container form the Hashmap received in parameter
	 * the songs which were newer are removed form vSong, actually it's if the songs already exists in the songs container
	 * then it is removed from vSong (even if it is not newer)
	 */
	public int updateSongs( HashMap<String, Song>  vSongs)
	{
		int nbOfSongs=0;
		Iterator<Song> it = vSongs.values().iterator();//get an iterator from the Hashmap of potentiel newer songs
		Song aSong=null;
		
		//we iterate through vSong
		while(it.hasNext())
		{
			aSong = it.next();//get the next song
			
			Song aSong1 = containerFileName.get( aSong.getFileName());//is the song already in the container (if no we do nothing)
			if(aSong1 !=null)
			{
				//so the song is already in database
				if( aSong1.updateInformationFromSong( aSong)) //update the song information, is there really somethong updated? 
				{
					//yes so we have to update the database
					this.update(aSong1);
					nbOfSongs++;
				}
				
				it.remove(); //remove the song from the vSongs
			}

		}
		
		return nbOfSongs;
	}

	
	
	
	
	
	/*@method : int removeDeletedSongs( HashMap<String, Song>  vSongs)
	 * remove songs which are in the song container (in the database), and not in vSongs
	 * 
	 * returns the number of songs which have been removed
	 */
	public int removeDeletedSongs(  HashMap<String, Song>  vSongs)
	{
		int nbOfSongs=0;

		Iterator<Song> it = containerId.values().iterator();//we are going to iterate through the song container
		while( it.hasNext())
		{
			Song aSong = it.next();  //get next song
			
			Song s = vSongs.get(aSong.getFileName());  //is the song in vSong
			if( s==null)
			{	//Ok it means we have a song in our database that is no more in the system
				//so we remove it from the database->songs AND database->Synclists
				//@TODO => we could check that is not just file move in using the hashkey and data and size and name
		
				//1) we remove the song from the containers
				it.remove(); //from the id containers
				containerFileName.remove(aSong.getFileName()); //from the filename containers
				
				//2) we remove the song from the Synclists
				SynclistContainer.getSingleton().removeSongFromAllSynclist(aSong.getId());
				
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

	
	
	
	/*@method : Vector<Integer> getSongByAlbum( int albumId)
	 * returns a vector of song which belongs to an album 
	 * 
	 * notice: may be we coiuld be faster in makign directly a query in database rather then iterate through the whole song container
	 */
	public Vector<Integer> getSongByAlbum( int albumId)
	{
		Vector<Integer> v = new Vector<Integer>(); //create a vector

		Iterator<Song> it = containerId.values().iterator(); //get an Iterator from the song container 
		
		while( it.hasNext())
		{
			Song aSong = it.next();//next song
			
			if( aSong.getAlbumID()==albumId) v.add(aSong.getId()); //if the song is from the album, we add the id in the vector
		}
		
		return v;
	}


	/*@method : Vector<Integer> getSongByArtist( int artistId)
	 * returns a vector of song which belongs to an artists 
	 * notice: may be we could be faster in makign directly a query in database rather then iterate through the whole song container
	 */
	public Vector<Integer> getSongByArtist( int artistId)
	{
		Vector<Integer> v = new Vector<Integer>();//create a vector

		Iterator<Song> it = containerId.values().iterator(); //get an Iterator from the song container
		while( it.hasNext())
		{
			Song aSong = it.next();//next song
			
			if( aSong.getArtistID()==artistId) v.add(aSong.getId()); //if the song is from the artist, we add the id in the vector
		}
		
		return v;
	}

	
}
