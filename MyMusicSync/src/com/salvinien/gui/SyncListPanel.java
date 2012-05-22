package com.salvinien.gui;

import java.awt.Dimension;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.salvinien.discography.Song;
import com.salvinien.discography.SongContainer;
import com.salvinien.gui.tableTree.SyncListNode;
import com.salvinien.gui.tableTree.TableTreeTransferHandler;
import com.salvinien.gui.tableTree.TreeTransferHandler;
import com.salvinien.mymusicsync.Device;
import com.salvinien.mymusicsync.DeviceSyncList;
import com.salvinien.synclists.Synclist;
import com.salvinien.synclists.SynclistContainer;

public class SyncListPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3121566837540214523L;

	protected JTree theTree;
	protected DefaultMutableTreeNode root;
	
	public SyncListPanel( Device aDevice)
	{
		root = new DefaultMutableTreeNode("Root");
		theTree = new JTree( root);
		
		theTree.setDragEnabled(true);
	    theTree.setTransferHandler(new TreeTransferHandler());

		
		JScrollPane jp = new JScrollPane( theTree);		
		add(jp);

		setNewDevice(aDevice);
	}
	
	
	
	public void setNewDevice( Device aDevice)
	{
		//if the tree has already been constructed
		root.removeAllChildren();
		theTree.removeAll();
		
						
		Iterator<DeviceSyncList> it =aDevice.getDeviceSyncLis().iterator();
		while( it.hasNext())
		{
			DeviceSyncList aDeviceSyncList=it.next();
			int aPlaylisId = aDeviceSyncList.getPlaylistId();
			
			Synclist aPlayList = SynclistContainer.getSingleton().getPlaylist(aPlaylisId);

			SyncListNode aSyncListNode = new SyncListNode( aPlayList);
			
			root.add( aSyncListNode);
			
			addPlaylist( aPlayList, aSyncListNode);
		}
		
		
		((DefaultTreeModel)theTree.getModel()).reload();
	}
	
	
	protected void addPlaylist( Synclist aPlaylist, SyncListNode aSyncListNode)
	{
		Iterator<Integer> it = aPlaylist.iterator();
		while( it.hasNext())
		{
			int id = it.next();
			
			Song aSong = SongContainer.getSingleton().getSong(id);
			
			aSyncListNode.getArtist(aSong);
		}
	}

}
