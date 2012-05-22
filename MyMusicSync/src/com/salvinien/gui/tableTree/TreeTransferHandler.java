package com.salvinien.gui.tableTree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.salvinien.discography.SongContainer;
import com.salvinien.synclists.Synclist;
import com.salvinien.synclists.SynclistContainer;
import com.salvinien.utils.Converter;

public class TreeTransferHandler extends TransferHandler
{
	private static final long	serialVersionUID	= -318289928111396520L;

    
    //Ctor
    public TreeTransferHandler()
    {
    	
    }
    
   
    
    public boolean canImport(TransferHandler.TransferSupport info) 
    {
        // Check for String flavor
        if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) 
        {
            return false;
        }
        return true;
   }

    protected Transferable createTransferable(JComponent c) 
    {
    	JTree t = (JTree) c;

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
    
    public boolean importData(TransferHandler.TransferSupport info) 
    {
    
      /*  if (!info.isDrop()) 
        {
            return false;
        }
*/

        

        // Get the string that is being dropped.
        Transferable t = info.getTransferable();
        String data;
        try 
        {
            data = (String)t.getTransferData(DataFlavor.stringFlavor);
        } 
        catch (Exception e) { return false; }
                                
        // Perform the actual import.
        //1) transfer the ids in a vector
        Vector<Integer> v =Converter.stringToVector(data);
        
        //2) look for the targeted synclist
        JTree aTree = (JTree)info.getComponent();
        DefaultMutableTreeNode aNode = (DefaultMutableTreeNode)( aTree.getSelectionPath().getLastPathComponent()  );
        SyncListNode theSyncListNode=null;
        if( aNode.isRoot()==true)
        {//the drop is the root
        	
        	//so if there are several synclist(s), we cannot decide which one is the real 
        	if( aNode.getChildCount() >1) return false;
        	
        	//however if there is only one ....
        	theSyncListNode = (SyncListNode) aNode.getChildAt(0); 
        }
        else
        {
        	//ok so th drop has not been done on the root
        	//so it is possible to know in which SyncList !
        	ADefaultMutableTreeNode prev = (ADefaultMutableTreeNode) aNode;
        	while( aNode.isRoot()!=true)
        	{
            	prev = (ADefaultMutableTreeNode) aNode;
        		aNode= (DefaultMutableTreeNode)aNode.getParent();
        	}
        	
        	theSyncListNode = (SyncListNode) prev;
        	
        }
        
        
        Iterator<Integer> it = v.iterator();
        Synclist aSyncList = theSyncListNode.getSynclist();
        while( it.hasNext())
        {
        	int songId = it.next();
        	
        	aSyncList.addSong(songId); //add the song to the playlist
        	theSyncListNode.getArtist( SongContainer.getSingleton().getSong(songId)); //create the node int the tree 
        }
        SynclistContainer.getSingleton().save(aSyncList);
        ((DefaultTreeModel)aTree.getModel()).reload();

        
        return true;
    }

    protected void exportDone(JComponent c, Transferable data, int action) 
    {
    	//@todo: remove the node
    }
    
}
