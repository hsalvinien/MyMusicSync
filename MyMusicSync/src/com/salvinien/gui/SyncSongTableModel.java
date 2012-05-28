package com.salvinien.gui;

import javax.swing.table.AbstractTableModel;

import com.salvinien.synclists.SongSynchro;
import com.salvinien.synclists.SongSynchroContainer;

public class SyncSongTableModel extends AbstractTableModel 
{
	 /**
	 * 
	 */
	private static final long	serialVersionUID	= 8115485209571127650L;

	private String[] columnNames = { "Artist", "Album", "Name", "ActionFrom","DoNothing","ActionTo","Name","ModifySyncList"};

	private SongSynchroContainer data;

	
	public SyncSongTableModel(SongSynchroContainer aContainer)
	{
		setData( aContainer);
	}

	
	public void setData(SongSynchroContainer aContainer)
	{
		data = aContainer;
		
		if(data==null)
		{
			data = new SongSynchroContainer();
		}
	}

	
	
	
	
	public int getColumnCount() 			{ return columnNames.length;}
	public String getColumnName(int col) 	{ return columnNames[col];}
	
	public int getRowCount()				{ return data.size();}
	
	public boolean isCellEditable(int row, int col) 
	{
	      //Note that the data/cell address is constant,
	      //no matter where the cell appears onscreen.
		switch( col)
		{
			case 3: 
			case 4: 
			case 5: 
			case 7:
				return true;
			
			default: return false;
		}
	}

	public Object getValueAt(int row, int col)
	{

		if( row>= getRowCount()) return null;
		if( col>= getColumnCount()) return null;
	
		SongSynchro aSongSynchro = data.getElement(row);
		
		switch( col)
		{
			case 0:	return aSongSynchro.Artist(); 
			case 1: return aSongSynchro.Album();
			case 2: return aSongSynchro.NameSource(); 
			case 3: return " =>";
			case 4: return "O";
			case 5: return " =>";
			case 6: return aSongSynchro.NameTarget();
			case 7: 
				break;
		}
		
		return null;
	}

}
