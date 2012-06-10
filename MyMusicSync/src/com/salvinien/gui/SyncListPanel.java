package com.salvinien.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.salvinien.discography.Song;
import com.salvinien.discography.SongContainer;
import com.salvinien.gui.tableTree.ADefaultMutableTreeNode;
import com.salvinien.gui.tableTree.SyncListNode;
import com.salvinien.gui.tableTree.TreeTransferHandler;
import com.salvinien.mymusicsync.Device;
import com.salvinien.synclists.DeviceSyncList;
import com.salvinien.synclists.Synclist;
import com.salvinien.synclists.SynclistContainer;


/**
 * @class: SyncListPanel 
 * 
 * This class manages the synclist of the selected device
 * 
 * it implements an MouseListener to catch the left-clicks to show the JPopupMenu
 * 
 */

public class SyncListPanel extends JPanel implements MouseListener
{
	private static final long	serialVersionUID	= -3121566837540214523L;

	protected JTree theTree;
	protected DefaultMutableTreeNode root;

	protected Device theDevice;
	myApp theMom;
	
	
	//CTOR
	public SyncListPanel( myApp aMom, Device aDevice)
	{
		theMom = aMom;
		
		BoxLayout l1= new BoxLayout(this, BoxLayout.PAGE_AXIS);
	    this.setLayout(l1);
		
		root = new DefaultMutableTreeNode("Root");
		theTree = new JTree( root);
		
		//to allow DND and CP from the library tree
		theTree.setDragEnabled(true);
	    theTree.setTransferHandler(new TreeTransferHandler());

		JScrollPane jp = new JScrollPane( theTree);		
		add(jp);
		
		setNewDevice(aDevice);

		theTree.addMouseListener(this);	
	}
	
	

	
	//Methods
	/**
	 * @method : void setNewDevice( Device aDevice)
	 * 
	 *   it displays the synclists fo the device
	 *     
	 */
	
	public void setNewDevice( Device aDevice)
	{
		//if the tree has already been constructed
		theDevice = aDevice;
		root.removeAllChildren();
		theTree.removeAll();
		
		//for all synclists of the device				
		Iterator<DeviceSyncList> it =aDevice.getDeviceSyncLis().iterator();
		while( it.hasNext())
		{
			//next synclist
			DeviceSyncList aDeviceSyncList=it.next();
			int aSynclisId = aDeviceSyncList.getSynclistId();
			Synclist aSyncList = SynclistContainer.getSingleton().getSynclist(aSynclisId);

			//create the synclist tree (under root)
			SyncListNode aSyncListNode = new SyncListNode( aSyncList);
			root.add( aSyncListNode);
			
			addSynclist( aSyncList, aSyncListNode);
		}
		
		((DefaultTreeModel)theTree.getModel()).reload(); //repaint the tree

	}
	
	
	/**
	 * @method : void addSynclist( Synclist aSynclist, SyncListNode aSyncListNode)
	 * 
	 *   add the elements of the synclist to the synclistnodes
	 *     
	 */
	protected void addSynclist( Synclist aSynclist, SyncListNode aSyncListNode)
	{
		Iterator<Integer> it = aSynclist.iterator();
		while( it.hasNext())
		{
			int id = it.next();
			
			Song aSong = SongContainer.getSingleton().getSong(id);
			
			aSyncListNode.getArtist(aSong);
		}
	}


	
	
	/**
	 * @method : void removeNodes( TreePath tp[])
	 * 
	 *   remove nodes fron the tree
	 *     
	 */
	public void removeNodes( TreePath tp[])
	{
		for( int i=0; i < tp.length; i++)
		{
			TreePath aTp = tp[i];
			Object to[] = aTp.getPath();
			
			ADefaultMutableTreeNode aNode = (ADefaultMutableTreeNode) to[ to.length-1]; 
			
			aNode.removeMe();
		}

		((DefaultTreeModel)theTree.getModel()).reload();
	}
    		

	/**
	 * @method : void expandAll()
	 * 
	 *   expand all nodes
	 *     
	 */
	public  void expandAll()
	{
		for( int i =0 ; i<theTree.getRowCount(); i++)
			theTree.expandRow(i);		
	}



	//the methods to be implemented of the mouselistener
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e)  {}
	public void mousePressed(MouseEvent e) {}

	/**
	 * @method : void mouseReleased(MouseEvent e)
	 * 
	 *   when the mouse button is released (left, middle or right) is released, this method is called  
	 *     
	 */
	public void mouseReleased(MouseEvent e)
	{
		if( e.isPopupTrigger())  //is-it the right button?
		{
			//yes so we show the popupmenu
			syncListPanelPopUpMenu m = new syncListPanelPopUpMenu(this);
			m.show(e.getComponent(), e.getX(), e.getY());
		}		
	}
	
}
