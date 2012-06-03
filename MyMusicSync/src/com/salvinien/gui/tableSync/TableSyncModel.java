package com.salvinien.gui.tableSync;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

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
        // Names of the columns.		0			1				2				3				4				5				7				8
		columnNames = new String[]  {"Artist",		"Album", 		"Name", 	   	" ",			" ",   			" ",   			"Name",       "	Modify SyncList"};
        ColumnTypes = new Class<?>[]{String.class, 	String.class, 	String.class, 	ImageIcon.class,ImageIcon.class,ImageIcon.class,String.class, 	Boolean.class};
        FixeSize    = new Boolean[] {false,			false,			false,			true,			true,			true,			false,			true};  	
        ColumnSize  = new int[]  	{-1, 			-1,				-1,				20, 			20, 			20,				-1,				70};  	// column size
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

	
	public void setValueAt(Object value, int row, int col)  
    {
		if( row>= getRowCount()) return;
		if( col>= getColumnCount()) return;
    	

		SongSynchro aSongSynchro = data.getElement(row);
				
		switch( col)
		{
			case 0:	return; 
			case 1: return ;
			case 2: return ; 
			case 3: 
				aSongSynchro.isTo( true);
				aSongSynchro.IshouldDoNothing(false);				
				break;
				
			case 4: 
				aSongSynchro.IshouldDoNothing( !aSongSynchro.IshouldDoNothing());

				break;
				
			case 5: 
				aSongSynchro.isFrom(true );
				aSongSynchro.IshouldDoNothing(false);

				break;
				
			case 6: return ;
			case 7: 
				Boolean b3 = (Boolean) value;
				aSongSynchro.shouldImodifySyncList(b3);
				
			default:
				break;
		}
		fireTableCellUpdated(row, 3);
		fireTableCellUpdated(row, 4);
		fireTableCellUpdated(row, 5);
		fireTableCellUpdated(row, 7);
		
		return;
}
	public Object getValueAt(int row, int col)
	{

		if( row>= getRowCount()) return null;
		if( col>= getColumnCount()) return null;
	
		SongSynchro aSongSynchro = data.getElement(row);
		
		String path=null;
		File file=null;
		ImageIcon img=null;
		switch( col)
		{
			case 0:	return aSongSynchro.Artist(); 
			case 1: return aSongSynchro.Album();
			case 2: return aSongSynchro.NameSource(); 
			case 3: 
				if( aSongSynchro.isTo())
				{
					if( aSongSynchro.NameTarget().equals("==null==") )
					{
						if( aSongSynchro.IshouldDoNothing()) { path="img/delete-no.gif";}  
						else	{	path = "img/delete.gif";}
					}
					else
					{
						if( aSongSynchro.IshouldDoNothing()) { path="img/copytoleft-no.gif";}  
						else	{	path = "img/copytoleft.gif";}							
					}
				}
				else
				{
					path="img/blank.gif";
				}					

				
				file = new File(path);
				img = new ImageIcon(file.getPath());
				
				
				return img;

			case 4: 
				if( aSongSynchro.IshouldDoNothing()) { path="img/donothing-true.gif";}  /*case true*/
				else								 { path="img/donothing-false.gif";} //case false
				
				file = new File(path);
				img = new ImageIcon(file.getPath());				
				return img;
				
				
			case 5: 
				if( aSongSynchro.isFrom())
				{
					if( aSongSynchro.NameSource().equals("==null==") )
					{
						if( aSongSynchro.IshouldDoNothing()) { path="img/delete-no.gif";}  
						else { path = "img/delete.gif";}
					}
					else
					{
						if( aSongSynchro.IshouldDoNothing()) { path="img/copytoright-no.gif";}  
						else { path = "img/copytoright.gif";}							
					}
				}
				else
				{
					path="img/blank.gif";
				}					

					
				file = new File(path);
				img = new ImageIcon(file.getPath());				
				return img;
				
			case 6: return aSongSynchro.NameTarget();
			case 7: 
				if( aSongSynchro.IshouldDoNothing()) return false;
				return aSongSynchro.shouldImodifySyncList();
				
			default:
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
