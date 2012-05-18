package com.salvinien.gui.tableTree;

public class AlbumNode extends ADefaultMutableTreeNode
{
	private static final long	serialVersionUID	= 5139317989238918397L;

	public AlbumNode(String anAlbum)	{super( anAlbum);}

	public Object getValueAt(int column)
	{
	    switch(column) 
	    {
		    case 0:	return this.getUserObject();
		    case 1: return String.valueOf(this.getChildCount())+" Songs";
		    case 2: return " ";

		    default:
		    case 3: return " ";
	    }

	}



}
