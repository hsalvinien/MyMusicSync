package com.salvinien.mymusicsync;


/**
 * @class: Libelle
 * 
 * This class manages a Libelle, which is a Name and an Id 
 * 
 * this is the base class for all containers like artistContainer etc etc
 * 
 */

public class Libelle
{
	//members
	protected int Id;
	protected String Name;


	//Ctors
	public Libelle( )
	{
		Id=-1;
		Name = "";
	}
	public Libelle( String aName)
	{
		Id=-1;
		Name = aName;
	}

	public Libelle( int anId, String aName)
	{
		Id= anId;
		Name = aName;
	}


	//Accessors
	public int getId() 					{ return Id;}
	public String getName() 			{ return Name;}
	public void setId(int anId) 		{ Id = anId;}
	public void setName( String aName) 	{ Name = aName;}

}
