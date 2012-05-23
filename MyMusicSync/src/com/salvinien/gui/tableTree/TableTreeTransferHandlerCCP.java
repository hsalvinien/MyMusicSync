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

public class TableTreeTransferHandlerCCP  extends TransferHandler
{
	private static final long	serialVersionUID	= 5235609475030137511L;

	//Ctor
    public TableTreeTransferHandlerCCP()
    {
    	
    }
       
    
  
    protected Transferable createTransferable(JComponent c) 
    {
 
    	TableTree TT = (TableTree) c;
    	TableTreeCellRenderer t = TT.tree;
    	
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
    
    
    public void exportToClipboard(JComponent c, Clipboard clip,int action)
    {
    	//@TODO =>NOT TESTED AND ....NOT WORKING
    	Transferable t = createTransferable(c);
    	clip.setContents(t, (ClipboardOwner) t);
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