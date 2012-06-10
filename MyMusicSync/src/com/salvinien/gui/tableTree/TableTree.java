package com.salvinien.gui.tableTree;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;


/**
 * @class: TableTree
 * 
 * This class manage a table with a Jtree as a first column 
 * 
 */

public class TableTree extends JTable
{
	private static final long	serialVersionUID	= 1L;
	protected TableTreeCellRenderer	tree;

	//CTOR
	public TableTree( AbstractTableTreeModel TableTreeModel) 
	{
		super();

		// Create the tree. It will be used as a renderer and editor.
		tree = new TableTreeCellRenderer(TableTreeModel);

		// Install a tableModel representing the visible rows in the tree.
		TableModel tm =new TableTreeModelAdapter(TableTreeModel, tree); 
		super.setModel( tm);
		
		
		 // don't display root
	     tree.expandRow(0); 
	     tree.setRootVisible(TableTreeModel.isRootVisible());

	     tree.setDragEnabled(true);
	     tree.setTransferHandler(new TableTreeTransferHandler());
	     
	     this.setTransferHandler(new TableTreeTransferHandlerCCP());

	     //column size
	     for(int i=0; i< TableTreeModel.getColumnCount(); i++ )
	     {
	    	 
	 	    TableColumn column = this.getColumnModel().getColumn(i);
	    	 
	 		//fixes the size of some columns 
	    	 if( TableTreeModel.isColumnFixedSize(i))
	    	 {
				int width = TableTreeModel.ColumnSize(i);
				column.setPreferredWidth(width);	//fixes the column size
				column.setMaxWidth(width);
				column.setWidth(width);
	    		 
	    	 }
	    	 
	    	 
	    	 //and align the display in the column
	    	 if( i>0)
	    	 {
		    	 DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();    
			     dtcr.setHorizontalAlignment(  TableTreeModel.ColumnAlignement(i));  
				 column.setCellRenderer(dtcr);  //centering the text  
	    	 }
	     }
	     

		// Force the JTable and JTree to share their row selection models.
		tree.setSelectionModel(new DefaultTreeSelectionModel()
		{
			/***
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			// Extend the implementation of the constructor, as if:
			/** public this() */{
				setSelectionModel(listSelectionModel);
			}
		});
		// Make the tree and table row heights the same.
		tree.setRowHeight(getRowHeight());

		// Install the tree editor renderer and editor.
		setDefaultRenderer(TableTreeModel.class, tree);
		setDefaultEditor(TableTreeModel.class, new TableTreeCellEditor());

		setShowGrid(false);
		setIntercellSpacing(new Dimension(0, 0));
	}


	/**
	 * @method : int getEditingRow()
	 * Workaround for BasicTableUI anomaly. Make sure the UI never tries to
	 * paint the editor. The UI currently uses different techniques to paint the
	 * renderers and editors and overriding setBounds() below is not the right
	 * thing to do for an editor. Returning -1 for the editing row in this case,
	 * ensures the editor is never painted.
	 */
	public int getEditingRow()
	{
		return (getColumnClass(editingColumn) == TableTreeModel.class) ? -1
				: editingRow;
	}


	
	
	/**
	 * @class: TableTreeCellEditor
	 * 
	 * trivial cell editor for the tabletree  
	 * 
	 */	
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

	
	
	
	
	/**
	 * @class: TableTreeCellRenderer
	 * 
	 * trivial cell renderer for the tabletree  
	 * 
	 */	
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

}
