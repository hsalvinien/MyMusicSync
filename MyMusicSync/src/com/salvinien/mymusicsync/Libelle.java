package com.salvinien.mymusicsync;

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
