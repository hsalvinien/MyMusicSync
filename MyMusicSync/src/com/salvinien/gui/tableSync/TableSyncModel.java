package com.salvinien.gui.tableSync;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import com.salvinien.synclists.SongSynchro;
import com.salvinien.synclists.SongSynchroContainer;



/*
 * @class: TableSyncModel
 * 
 * overides the default Model to take in charge a TableSync
 * 
 * for memory: a model take in charge the relationship between the table and the data
 * 
 */

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

	public SongSynchroContainer getData()	{ return data;}
	//set the container, if it is null, create an empty one
	public void setData(SongSynchroContainer aContainer)
	{
		data = aContainer;
		
		if(data==null)
		{
			data = new SongSynchroContainer();
		}
	}
	
	
	//METHODS
	/*
	 * @method: init()
	 * 
	 * Initializes header/size/ etc etc 
	 * 
	 */
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
	
	
	/*@method : void setValueAt(Object value, int row, int col)
	 * 
	 * set a value in a cell  
	 * 
	 */
	public void setValueAt(Object value, int row, int col)  
    {
		//precheck
		if( row>= getRowCount()) return;
		if( col>= getColumnCount()) return;
    	

		//retreive the row 
		SongSynchro aSongSynchro = data.getElement(row);
				
		//depending on the column, we set the right attribute
		switch( col)
		{
			case 0:	return; //we do nothing, since the value cannot be edited  
			case 1: return; //we do nothing, since the value cannot be edited
			case 2: return; //we do nothing, since the value cannot be edited
			
			case 3: 	//case of the flag copy from target to source
				aSongSynchro.isTo( true);
				aSongSynchro.IshouldDoNothing(false); //as we have selected an action, we cannot "do nothing"	

				//as one of the previous set may impact several columns we update them
				fireTableCellUpdated(row, 3);
				fireTableCellUpdated(row, 4);
				fireTableCellUpdated(row, 5);

				break;
				
			case 4: 	//case of the flag do nothing
				aSongSynchro.IshouldDoNothing( !aSongSynchro.IshouldDoNothing()); 

				//as one of the previous set may impact several columns we update them
				fireTableCellUpdated(row, 3);
				fireTableCellUpdated(row, 4);
				fireTableCellUpdated(row, 5);

				break;
				
			case 5: //case of the flag copy from source to target
				aSongSynchro.isFrom(true );
				aSongSynchro.IshouldDoNothing(false); //as we have selected an action, we cannot "do nothing"

				//as one of the previous set may impact several columns we update them
				fireTableCellUpdated(row, 3);
				fireTableCellUpdated(row, 4);
				fireTableCellUpdated(row, 5);

				break;
				
			case 6: return ; //we do nothing, since the value cannot be edited

			case 7:	//case of the modify sync list @todo : this will probably change 
				Boolean b3 = (Boolean) value;
				aSongSynchro.shouldImodifySyncList(b3);
				
				fireTableCellUpdated(row, 7);//tell the table, a change has happened
				break;
				
			default:
				break;
		}
		
		
		return;
    }
	
	

	/*@method : Object getValueAt(int row, int col)
	 * 
	 * get a value from a cell  
	 * 
	 */
	public Object getValueAt(int row, int col)
	{

		//precheck
		if( row>= getRowCount()) return null;
		if( col>= getColumnCount()) return null;
	
		//row
		SongSynchro aSongSynchro = data.getElement(row);
		
		
		
		//col
		String path=null;
		File file=null;
		ImageIcon img=null;
		switch( col)
		{
			case 0:	return aSongSynchro.Artist(); 
			case 1: return aSongSynchro.Album();
			case 2: return aSongSynchro.NameSource(); 
			case 3:		//returns the righ icon
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

			case 4: //returns the righ icon
				if( aSongSynchro.IshouldDoNothing()) { path="img/donothing-true.gif";}  /*case true*/
				else								 { path="img/donothing-false.gif";} //case false
				
				file = new File(path);
				img = new ImageIcon(file.getPath());				
				return img;
				
				
			case 5: //returns the righ icon
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

	

}
