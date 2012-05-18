package com.salvinien.discography;

import java.util.Iterator;
import java.util.Vector;

public class SongSynchroContainer
{

	Vector<SongSynchro> container =	new Vector<SongSynchro>();
	
	
	public void add( SongSynchro aSongSynchro)
	{
		container.add( aSongSynchro);
	}

	public Iterator<SongSynchro> iterator()
	{
		return container.iterator();
	}
	
	public int size() { return container.size();}
	public SongSynchro getElement( int i) 	{ return container.get(i);}
	
	
}
