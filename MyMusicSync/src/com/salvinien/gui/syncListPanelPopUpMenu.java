package com.salvinien.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.salvinien.gui.tableTree.ADefaultMutableTreeNode;
import com.salvinien.gui.tableTree.SyncListNode;
import com.salvinien.mymusicsync.Device;
import com.salvinien.synclists.Synclist;
import com.salvinien.synclists.SynclistContainer;
import com.salvinien.synclists.SynclistNamesContainer;

public class syncListPanelPopUpMenu extends JPopupMenu implements ActionListener
{
	private static final long	serialVersionUID	= 6224936694416732035L;

	public final static int PuP_EXPAND_ALL=1;
	public final static int PuP_REMOVE_NODE=2;
	public final static int PuP_DISSOCIATE_SYNCLIST=3;
	public final static int PuP_ASSOCIATE_SYNCLIST=4;
	public final static int PuP_DELETE_SYNCLIST=5;
	
	
	protected SyncListPanel theSyncListPanel;

	public syncListPanelPopUpMenu(SyncListPanel aSyncListPanel) 
	{
		//		expand the tree
		JMenuItem menuItem = new JMenuItem("ExpandAll", KeyEvent.VK_A);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription( "Expand the tree");
		menuItem.setActionCommand(  String.valueOf(PuP_EXPAND_ALL));
		menuItem.addActionListener(this);
		this.add(menuItem);

		//Remove a node
		menuItem = new JMenuItem("Remove Selection", KeyEvent.VK_R);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription( "Remove songs from the synclist");
		menuItem.setActionCommand(  String.valueOf(PuP_REMOVE_NODE));
		menuItem.addActionListener(this);
		this.add(menuItem);

		
		//Dissociate a synclist
		menuItem = new JMenuItem("Dissociate Synclist &device", KeyEvent.VK_D);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription( "Remove the associatio synclist <-> device, but keep the synclist");
		menuItem.setActionCommand(  String.valueOf(PuP_DISSOCIATE_SYNCLIST));
		menuItem.addActionListener(this);
		this.add(menuItem);

		//Delete a node
		menuItem = new JMenuItem("Delete a Synclist", KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription( "delete the synclist");
		menuItem.setActionCommand(  String.valueOf(PuP_DELETE_SYNCLIST));
		menuItem.addActionListener(this);
		this.add(menuItem);

		//Remove a node
		menuItem = new JMenuItem("Associate a Synclist", KeyEvent.VK_A);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription( "Create a new association synclist <-> device");
		menuItem.setActionCommand(  String.valueOf(PuP_ASSOCIATE_SYNCLIST));
		menuItem.addActionListener(this);
		this.add(menuItem);
		
		
		theSyncListPanel = aSyncListPanel;
	}


	
	public void actionPerformed(ActionEvent e)
	{
    	String s1 = e.getActionCommand();
        int actionNumber = Integer.parseInt(s1);
        
        switch( actionNumber)
        {
        	case PuP_EXPAND_ALL: /*expand the tree*/
        		theSyncListPanel.expandAll();
        	 break;

        	case PuP_REMOVE_NODE: /* remove a node */
        		TreePath tp[] = theSyncListPanel.theTree.getSelectionPaths();
        		
        		theSyncListPanel.removeNodes( tp);
        		for( int i=0; i< theSyncListPanel.root.getChildCount(); i++)
        		{
        			SyncListNode aNode = (SyncListNode)theSyncListPanel.root.getChildAt(i);
        			SynclistContainer.getSingleton().save(aNode.getSynclist());
        		}
        		break;
        	 	
        	case PuP_DISSOCIATE_SYNCLIST: //remove association between device and synclist
        		
        		removeSyncList( false); //we don't delete the synclist
        		
        		((DefaultTreeModel)theSyncListPanel.theTree.getModel()).reload();

        		break;
        		
        	case PuP_DELETE_SYNCLIST: //delete synclist
        		
        		removeSyncList( true); //we  delete the synclist

        		((DefaultTreeModel)theSyncListPanel.theTree.getModel()).reload();

        		break;
        		

        	case PuP_ASSOCIATE_SYNCLIST:
        		syncListDeviceAssociation aSyncListDeviceAssociation= new syncListDeviceAssociation( theSyncListPanel);
        		aSyncListDeviceAssociation.createAndShowGUI();
        		break;
        		
        		
        	default: //do nothing
        		System.out.print("default=>");
        		System.out.print(actionNumber);
        }
	
	}
	
	
	
	protected void removeSyncList(boolean deleteSyncList)
	{
		TreePath tq[] = theSyncListPanel.theTree.getSelectionPaths();
		
		for( int i=0; i < tq.length; i++)
		{
			TreePath aTq = tq[i];
			Object to[] = aTq.getPath();
			
			if(to.length<2) continue; //if have selected the root node, we cannot know which synclist we want to remove
			
			SyncListNode aSyncListNode= (SyncListNode) to[ 1];  //1 is the synclist level 
			Device aDevice =  theSyncListPanel.theDevice;

			Synclist aSyncList = aSyncListNode.getSynclist();
			aDevice.unAssociateSyncList( aSyncList);
			aSyncListNode.removeMeCompletly();
			
			if( deleteSyncList)
			{
				SynclistNamesContainer.getSingleton().delete( aSyncList.getId());				
			}
			
		}
		
	}
	
	
}
