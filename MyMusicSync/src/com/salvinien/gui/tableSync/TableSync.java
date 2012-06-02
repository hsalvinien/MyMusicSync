package com.salvinien.gui.tableSync;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;


public class TableSync extends JTable
{
	private static final long	serialVersionUID	= -7096672333942522731L;


	public TableSync( TableSyncModel aTableModel) 
	{
		super(aTableModel);
		
		

	     //column size
	     for(int i=0; i< aTableModel.getColumnCount(); i++ )
	     {
	    	 
	 	    TableColumn column = this.getColumnModel().getColumn(i);
	    	 
	 		//fixes the size of some columns 
	    	 if( aTableModel.isColumnFixedSize(i))
	    	 {
				int width = aTableModel.ColumnSize(i);
				column.setPreferredWidth(width);	//fixes the column size
				column.setMaxWidth(width);
				column.setWidth(width);
	    		 
	    	 }
	    	 
	    	 
	    	 //and align the display in the column
	        //DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();    
			//dtcr.setHorizontalAlignment(  aTableModel.ColumnAlignement(i));  
	    	 //column.setCellRenderer(dtcr);  //centering the text  
	     }
	     


		// Install the tree editor renderer and editor.
//		setDefaultRenderer(TableTreeModel.class, tree);
//		setDefaultEditor(TableTreeModel.class, new TableTreeCellEditor());

		setShowGrid(false);
		setIntercellSpacing(new Dimension(0, 0));
	}

	/*
	 * Workaround for BasicTableUI anomaly. Make sure the UI never tries to
	 * paint the editor. The UI currently uses different techniques to paint the
	 * renderers and editors and overriding setBounds() below is not the right
	 * thing to do for an editor. Returning -1 for the editing row in this case,
	 * ensures the editor is never painted.
	 */
//	public int getEditingRow()
//	{
//		return (getColumnClass(editingColumn) == TableTreeModel.class) ? -1
//				: editingRow;
//	}


	
	
/*	
	public class TableTreeCellEditor extends AbstractCellEditor implements TableCellEditor
	{
		private static final long	serialVersionUID	= 1L;

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int r, int c)
		{
			return tree;
		}

		public Object getCellEditorValue()
		{
			// TODO Auto-generated method stub
			return null;
		}
	}

	*/
	
	
	/*
	public class TableTreeCellRenderer extends JTree implements TableCellRenderer
	{

		private static final long	serialVersionUID	= -413060051091608973L;
		protected int	visibleRow;

		public TableTreeCellRenderer(TreeModel model) { super(model);}

		public void setBounds(int x, int y, int w, int h)
		{
			super.setBounds(x, 0, w, TableTree.this.getHeight());
		}

		public void paint(Graphics g)
		{
			g.translate(0, -visibleRow * getRowHeight());
			super.paint(g);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			if (isSelected)
				setBackground(table.getSelectionBackground());
			else setBackground(table.getBackground());

			visibleRow = row;
			return this;
		}
	}
*/
}
