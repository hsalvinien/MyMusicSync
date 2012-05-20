package com.salvinien.gui.tableTree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

import com.salvinien.gui.tableTree.TableTree.TableTreeCellRenderer;


public class TableTreeTransferHandler extends TransferHandler
{
	private static final long	serialVersionUID	= -318289928111396520L;

    private int[] indices = null;
    
    //Ctor
    public TableTreeTransferHandler()
    {
    	
    }
    
   
    
  
    protected Transferable createTransferable(JComponent c) 
    {
    	TableTreeCellRenderer t = (TableTreeCellRenderer) c;

    	
    	TreePath TP[] = t.getSelectionPaths();
    	Vector<Integer> v= new Vector<Integer>();
    	for( int i=0; i<TP.length; i++)
    	{
    		ADefaultMutableTreeNode aNode = (ADefaultMutableTreeNode) TP[i].getLastPathComponent();
    		aNode.getSongIds(v);
    	}
    	
    	String S= v.toString();    	
    	
        return new StringSelection(S);
    }
    
    public int getSourceActions(JComponent c) 
    {
        return TransferHandler.COPY;
    }


    //we don't want to import anything in the TableTree
    public boolean canImport(TransferHandler.TransferSupport info) 
    {
    	return false;
    }
    public boolean importData(TransferHandler.TransferSupport info) 
    {
            return false;
    }

    protected void exportDone(JComponent c, Transferable data, int action) 
    {
     
    }
    
}
