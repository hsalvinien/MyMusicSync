package com.salvinien.gui.tableSync;

import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import com.salvinien.gui.tableTree.TableTreeModel;
import com.salvinien.synclists.SongSynchro;
import com.salvinien.synclists.SongSynchroContainer;

public class TableSyncModel extends  AbstractTableModel
{
	private static final long	serialVersionUID	= 1L;
	private String[] columnNames;		//column names
	private Class<?>[]  ColumnTypes;	// Types of the columns.
	private Boolean[]  FixeSize;  	// is the 
	private int[]  	ColumnSize;  	// is the 
	private int[] 	ColumnAlignement; //= ;
	private Boolean[]  isEditable;  	// is the 
	private final static int LEFT = SwingConstants.LEFT;
	private final static int CENTER = SwingConstants.CENTER;
	private final static int RIGHT= SwingConstants.RIGHT;
    
	private SongSynchroContainer data;


	
	//Ctor
	public TableSyncModel(SongSynchroContainer aContainer)
	{
		setData( aContainer);
		
		init();
	}

	private void init()
	{
        // Names of the columns.
		columnNames = new String[]  {"Artist",		"Album", 		"Name", 	   	"ActionFrom",	"DoNothing",   	"ActionTo",   "	Name",       "	Modify SyncList"};
        ColumnTypes = new Class<?>[]{String.class, 	String.class, 	String.class, 	String.class, 	Boolean.class, 	String.class, 	String.class, 	String.class};
        FixeSize    = new Boolean[] {false,			false,			false,			true,			true,			true,			false,			true};  	
        ColumnSize  = new int[]  	{-1, 			-1,				-1,				70, 			70, 			70,				-1,				70};  	// column size
        ColumnAlignement = new int[]{ RIGHT, 		RIGHT, 			RIGHT, 			CENTER, 		CENTER, 		CENTER, 		LEFT, 			CENTER}; //= SwingConstants.CENTER;
        isEditable	= new Boolean[]	{false,			false,			false,			true,			true,			true,			false,			true};
	}
	
	
	
	//Accessors 
	public int getColumnCount()				{ return columnNames.length;}
	public int getRowCount()				{ return data.size();}
	public String getColumnName(int col) 	{ return columnNames[col];}
    public Class<?> getColumnClass(int col) { return ColumnTypes[col];}
    public boolean isColumnFixedSize( int i){ return FixeSize[i];}
    public int ColumnSize( int i)			{ return ColumnSize[i];}
    public int ColumnAlignement( int i)		{ return ColumnAlignement[i];}
        
    //Note that the data/cell address is constant,no matter where the cell appears onscreen.
	public boolean isCellEditable(int row, int col) { return isEditable[ col];}

	
    public void setValueAt(Object aValue, Object node, int column) {}
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

	
	public void setData(SongSynchroContainer aContainer)
	{
		data = aContainer;
		
		if(data==null)
		{
			data = new SongSynchroContainer();
		}
	}

}
