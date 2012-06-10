package com.salvinien.gui.tableTree;


import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * @class: AbstractTableTreeModel 
 * 
 * Base class for tabletree
 * 
 * 
 */

public abstract class AbstractTableTreeModel implements TableTreeModel 
{
	
    
    protected String[]  Titles;  		// Names of the columns.
    protected Class<?>[]  ColumnTypes;	// Types of the columns.
    protected Boolean[]  FixeSize;  	// which column has a fixed size or not
    protected int[]  	ColumnSize;  	// what size for which columns
    protected int[] 	ColumnAlignement; // alignments
	
    protected DefaultMutableTreeNode theRoot;     
    protected EventListenerList listenerList = new EventListenerList();
  
    
    //CTOR
    public AbstractTableTreeModel(DefaultMutableTreeNode aRoot) { theRoot = aRoot;}

    //
    // Default implmentations for methods in the TreeModel interface. 
    //

    //ACCESSORS
    public Object getRoot() {return theRoot;}
    public boolean isLeaf(Object node) { return getChildCount(node) == 0;}
    public void valueForPathChanged(TreePath path, Object newValue) {}
    public void addTreeModelListener(TreeModelListener l) { listenerList.add(TreeModelListener.class, l);}
    public void removeTreeModelListener(TreeModelListener l) { listenerList.remove(TreeModelListener.class, l);}

   /*** By default, make the column with the Tree in it the only editable one. 
    *  Making this column editable causes the JTable to forward mouse 
    *  and keyboard events in the Tree column to the underlying JTree. 
    */ 
    public boolean isCellEditable(Object node, int column) { return getColumnClass(column) == TableTreeModel.class;    }
    public void setValueAt(Object aValue, Object node, int column) {}
    public Object getChild(Object aNode, int i)	{ return ((DefaultMutableTreeNode) aNode).getChildAt(i);}
	public int getChildCount(Object aNode)		{ return ((DefaultMutableTreeNode) aNode).getChildCount();}
	public int getColumnCount() 				{ return Titles.length;}
    public String getColumnName(int column) 	{ return Titles[column];}
    public Class<?> getColumnClass(int column) 	{ return ColumnTypes[column];}
    public boolean isColumnFixedSize( int i) 	{ return ( FixeSize== null)? false:FixeSize[i];}
    public int ColumnSize( int i)				{ return ( ColumnSize== null)? -1: ColumnSize[i];}
    public int ColumnAlignement( int i)			{ return ( ColumnAlignement== null) ? SwingConstants.LEFT : ColumnAlignement[i];}
     
    public abstract boolean isRootVisible();

    
    /**
     * @method : int getIndexOfChild(Object parent, Object child)
     * 
     * returns the index a child in a parent
     * 
     */
    public int getIndexOfChild(Object parent, Object child) 
    {
        for (int i = 0; i < getChildCount(parent); i++) 
        {
		    if (getChild(parent, i).equals(child)) 
		    { 
		        return i; 
		    }
        }
	return -1; 
    }


    /**
     * @method : void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices,Object[] children)
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices,Object[] children) 
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==TreeModelListener.class) 
            {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);

                ((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
            }          
        }
    }

    /**
     * @method : void fireTreeNodesInserted(Object source, Object[] path,  int[] childIndices, Object[] children)
     * 
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesInserted(Object source, Object[] path,  int[] childIndices, Object[] children) 
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==TreeModelListener.class) 
            {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                
                ((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
            }          
        }
    }

    /**
     * @method : void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices,Object[] children)
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices,Object[] children) 
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==TreeModelListener.class) 
            {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                ((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);
            }          
        }
    }

    /**
     * @method : void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children)
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children) 
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==TreeModelListener.class) 
            {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, 
                                           childIndices, children);
                ((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
            }          
        }
    }

}

