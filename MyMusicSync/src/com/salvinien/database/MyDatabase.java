package com.salvinien.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MyDatabase 
{
	protected static MyDatabase  mySingleton=null;
	protected String  databaseFileName="myMusic.db";
	protected Connection theConnection=null; 
	protected final String connectionString = "jdbc:sqlite:";

	//CTOR
	protected MyDatabase() 
	{
		try 
		{
			Class.forName("org.sqlite.JDBC");
			theConnection = DriverManager.getConnection( connectionString+databaseFileName);
			theConnection.setAutoCommit(false);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	
	}

	
	//ACCESSORS
	public static MyDatabase  getSingleton()
	{	
		if(mySingleton==null) mySingleton=new MyDatabase();
		
		return mySingleton;
	}


	
	
	//METHODS
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



	public boolean isDatabasenew()
	{
		String query = "SELECT * FROM Parameters;";
		try 
		{
			Statement stmt = theConnection.createStatement();
			stmt.executeQuery(query);
		}
		catch(Exception e)
		{
			return true;
		}
		return false;
	}


	
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
	
	public ResultSet executeQueryRethroughtException( String aQuery) throws SQLException
	{
		Statement stmt = theConnection.createStatement();
		ResultSet rs = stmt.executeQuery( aQuery);
		
		return rs;		
	}	
	
}
