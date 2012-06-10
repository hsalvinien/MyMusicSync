package com.salvinien.mymusicsync;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;

import com.salvinien.database.MyDatabase;

/**
 * @class: Parameters
 * 
 * This class manages the differents parameters of the application
 *  
 *  it is a Singleton
 * 
 */
public class Parameters 
{
	protected String root=null;

	protected static Parameters  mySingleton=null;
	protected String containerTableName="Parameters";

	
	//CTOR
	protected Parameters() 
	{
		try
		{
			loadFromDB();
		}
		catch (SQLException e) { e.printStackTrace();}
	}
	
	
	//ACCESSORS
	public static Parameters  getSingleton()
	{
		if(mySingleton==null) mySingleton=new Parameters ();
		
		return mySingleton;
	}
	public String getRoot() { return root;}
	
	
	
	
	//METHODS
	/*@method : void loadFromDB() throws SQLException
	 * 
	 *   load the parameters ofrem database
	 *   
	 *   if there is no root, ask the user (using the modal screen), where is the mount point of the library 
	 */
	protected void loadFromDB() throws SQLException
	{
		String Query= " SELECT * FROM "+containerTableName +" WHERE ";
		Query = Query + " ParameterName ='root'";
		
		ResultSet rs = MyDatabase.getSingleton().executeQuery(Query);
		
		
	    //Extract data from result set
		while(rs.next())
		{
		    //Retrieve by column name
		    root = rs.getString("Value");
		    
		}
		
		//Clean-up environment
		rs.close();

	    //if no root, choose one
	    if (root==null) 
	    {
			ScrDialogInit dlg = new ScrDialogInit(new JFrame());

			File rootDir = dlg.getRootFile();
			
			root = rootDir.getAbsolutePath();
			root += File.separator;
			
			Query= " UPDATE "+containerTableName +" SET Value ='"+root+"' ";
			Query = Query + "WHERE ParameterName ='root'";
			
			MyDatabase.getSingleton().executeSimpleQuery(Query);
			
			MyDatabase.getSingleton().commit();
	    }    
	}

}
