package com.salvinien.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * @class: MyDatabase
 * 
 * This class manages access to the database, and query-ing
 * 
 * it is implemented as a singleton (Design pattern)
 * 
 * 
 * 
 * 
 */


public class MyDatabase 
{
	protected static MyDatabase  mySingleton=null;				/** contains the only instance of MyDayabase */
	protected String  databaseFileName="myMusic.db";			/** the actual file name that contains the data*/
	protected Connection theConnection=null; 					/** connection to the database*/
	protected final String connectionString = "jdbc:sqlite:";	/** beginning of the connection string, to complete it we just need to add the filename*/

	//CTOR
	//private to forbid the creation of instances but from getSingleton
	private MyDatabase() 
	{
		try 
		{
			//initialisation of JDBC driver and open the connection to the database
			Class.forName("org.sqlite.JDBC");
			theConnection = DriverManager.getConnection( connectionString+databaseFileName);
			theConnection.setAutoCommit(false);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	
	}

	
	//METHODS
	/**
	 * @method : MyDatabase getSingleton()
	 * this accessor returns the only instance of the class
	 * cf. DESIGN PATTENR singleton   
	 */
	public static MyDatabase  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new MyDatabase();
		
		return mySingleton;
	}


	/**
	 * @method : close
	 * close the connection to the database.
	 */
	public void close()
	{
		try 
		{
			theConnection.commit();
			theConnection.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	

	/**
	 * @method : executeSimpleQuery( String aQuery)
	 * execute a Query
	 * it doesn't commit 
	 * but if there is an error it rollbacks 
	 */
	public void executeSimpleQuery( String aQuery)
	{
		try 
		{
			Statement stmt = theConnection.createStatement();
	   		stmt.executeUpdate(aQuery);
			stmt.close();

		} 
		catch(SQLException ex) 
		{
			this.rollBack();
			
			ex.printStackTrace();
			System.out.print( aQuery);
		}
	}



	/**
	 * @method : commit()
	 *  commit 
	 */
	public void commit()
	{
		try 
		{
			theConnection.commit();

		} 
		catch(SQLException ex) 
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @method : rollBack()
	 *  rollBack 
	 */
	public void rollBack()
	{
		try 
		{
			theConnection.rollback();

		} 
		catch(SQLException ex) 
		{
			ex.printStackTrace();
		}
	}
	
	
	

	/**
	 * @method : ResultSet executeQuery( String aQuery)
	 *  execute a quey and returns the resulting resultSet
	 *  in case of error it doesn't rollback becasue normaly it is fora select 
	 *  it does't commit
	 */
	public ResultSet executeQuery( String aQuery)
	{
		ResultSet rs = null;
		try 
		{
			Statement stmt = theConnection.createStatement();
		    rs = stmt.executeQuery( aQuery);
			//stmt.close();

		} 
		catch(SQLException ex) 
		{
			ex.printStackTrace();
			System.out.print( aQuery);
		}
		
		
		return rs;		
	}	
	
	/**
	 * @method : ResultSet executeQueryRethroughtException( String aQuery)
	 *  execute a quey and returns the resulting resultSet
	 *  in case of error it doesn't rollback becasue normaly it is fora select 
	 *  it does't commit
	 *  this one rethrought the exception ( sometime we need it..)
	 */
	public ResultSet executeQueryRethroughtException( String aQuery) throws SQLException
	{
		Statement stmt = theConnection.createStatement();
		ResultSet rs = stmt.executeQuery( aQuery);
		
		return rs;		
	}	
	
}
