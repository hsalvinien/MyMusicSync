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
import com.salvinien.mymusicsync.DeviceSyncList;
import com.salvinien.synclists.Synclist;
import com.salvinien.synclists.SynclistContainer;

public class SyncListPanel extends JPanel implements MouseListener
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3121566837540214523L;

	protected JTree theTree;
	protected DefaultMutableTreeNode root;

	protected Device theDevice;
	myApp theMom;
	
	public SyncListPanel( myApp aMom, Device aDevice)
	{
		theMom = aMom;
		
		 BoxLayout l1= new BoxLayout(this, BoxLayout.PAGE_AXIS);
	     this.setLayout(l1);
		
		
		root = new DefaultMutableTreeNode("Root");
		theTree = new JTree( root);
		
		theTree.setDragEnabled(true);
	    theTree.setTransferHandler(new TreeTransferHandler());

		JScrollPane jp = new JScrollPane( theTree);		
		add(jp);

		setNewDevice(aDevice);

		theTree.addMouseListener(this);
	
	}
	
	
	
	public void setNewDevice( Device aDevice)
	{
		//if the tree has already been constructed
		theDevice = aDevice;
		root.removeAllChildren();
		theTree.removeAll();
		
						
		Iterator<DeviceSyncList> it =aDevice.getDeviceSyncLis().iterator();
		while( it.hasNext())
		{
			DeviceSyncList aDeviceSyncList=it.next();
			int aSynclisId = aDeviceSyncList.getSynclistId();
			
			Synclist aSyncList = SynclistContainer.getSingleton().getSynclist(aSynclisId);

			SyncListNode aSyncListNode = new SyncListNode( aSyncList);
			
			root.add( aSyncListNode);
			
			addSynclist( aSyncList, aSyncListNode);
		}
		
		((DefaultTreeModel)theTree.getModel()).reload();

	}
	
	
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
    		
	
	public  void expandAll()
	{
		for( int i =0 ; i<theTree.getRowCount(); i++)
			theTree.expandRow(i);		
	}



	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e)  {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e)
	{
		if( e.isPopupTrigger())
		{
			syncListPanelPopUpMenu m = new syncListPanelPopUpMenu(this);
			m.show(e.getComponent(), e.getX(), e.getY());
		}		
	}
	
}
