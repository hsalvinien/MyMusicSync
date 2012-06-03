package com.salvinien.gui.tableSync;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

public class ColumnListener implements MouseListener
{
	
    protected JTable table;

    public ColumnListener(JTable t)
    {
      table = t;
    }

    public void mouseClicked(MouseEvent e) 
    {
      TableColumnModel colModel = table.getColumnModel();
      int column = colModel.getColumnIndexAtX(e.getX());

      
      //we are intested only in column 3/4/5
      if( (column <3) || ( column>5) )  return;
      
      //for each row of the column we set a new value
      for( int i=0; i <table.getRowCount(); i++ )
      {
    	  table.getModel().setValueAt(new Boolean(true), i, column);
      }
      
      
    }




	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

}
