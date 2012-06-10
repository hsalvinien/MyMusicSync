package com.salvinien.gui.tableTree;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

import com.salvinien.gui.tableTree.TableTree.TableTreeCellRenderer;



/**
 * @class: TableTreeTransferHandler 
 * 
 * This class manages the transfert of data when there is a Drag n Drop action 
 * this one is specific to a the tableTree 
 */

public class TableTreeTransferHandler extends TransferHandler
{
	private static final long	serialVersionUID	= -318289928111396520L;

    
    //Ctor
    public TableTreeTransferHandler()
    {
    	
    }
    
   
    
  
	/**
	 * @method : Transferable createTransferable(JComponent c)
	 * this method has to be overridden, 
	 * 
	 * it extracts data from the componnent and encapsulates it in a String
	 * 
	 */ 
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
    
    
	/**
	 * @method : void exportToClipboard(JComponent c, Clipboard clip,int action)
	 * this method has to be overridden, 
	 * 
	 * putthe info in a clipboard
	 * 
	 */ 
    public void exportToClipboard(JComponent c, Clipboard clip,int action)
    {    	//@TODO =>NOT TESTED AND ....NOT WORKING
    	Transferable t = createTransferable(c);
    	clip.setContents(t, (ClipboardOwner) t);
    }
    
    
    
    
    
	/**
	 * @method : int getSourceActions(JComponent c)
	 * this method has to be overridden, 
	 * 
	 * honestly I don't really know why we have to do this (actually  I didn't take the time)
	 * I guess it is something like it is a copy or a move ( deleting or not the source)
	 * 
	 */ 
    public int getSourceActions(JComponent c) 
    {
        return TransferHandler.COPY;
    }



    
	/**
	 * @method : boolean canImport(TransferHandler.TransferSupport info)
	 * this method has to be overridden, 
	 * 
	 * it tells if the import can be done or not
	 * 
	 * actually for us, we don't want to import anything in the TableTree so returns false
	 * 
	 */ 
    public boolean canImport(TransferHandler.TransferSupport info) 
    {
    	return false;
    }

    /**
     * @method : boolean importData(TransferHandler.TransferSupport info)
	 * this method has to be overridden, 
	 * 
	 * it imports data, but actually we don't want to import anything in the TableTree, so we do nothing and return false
	 * 
	 */ 
    public boolean importData(TransferHandler.TransferSupport info) 
    {
            return false;
    }

	/**
	 * @method : void exportDone(JComponent c, Transferable data, int action)
	 * this method has to be overridden, 
	 * 
	 * it is called after the export has been done
	 * 
	 * it can be used for any post action 
	 * 
	 */ 
    protected void exportDone(JComponent c, Transferable data, int action) 
    {
     
    }
    
}
