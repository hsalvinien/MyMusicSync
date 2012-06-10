package com.salvinien.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.salvinien.gui.tableTree.SyncListNode;
import com.salvinien.mymusicsync.Device;
import com.salvinien.synclists.Synclist;
import com.salvinien.synclists.SynclistContainer;
import com.salvinien.synclists.SynclistNamesContainer;


/**
 * @class: syncListPanelPopUpMenu  
 * 
 * This class manages the popupmenu which is shown when the user left-click on the synclists of a device.
 * 
 * the following functionnalities have been implemented
 * => Expand all : to show all nodes of the synclists
 * => Remove Selection : allow to delete nodes (the ones selected)
 * => Dissociate Synclist &device : to remove a synclist from a device. It doesn't delete the synclist from the database since it can be used by another device
 * => Delete a Synclist	: delete a synclist from the database
 * => Associate a Synclist :  allow to associate a synclist to the device, a "virgin" synclist can be created on the fly as well
 * 
 * 
 * it extends the JPopMenu (to be a JPopMenu ;-) ) and implements an ActionListener, the ActionListener intercepts events which are sent when the user click on 
 * an item of the popupmenu 
 *  
 */

public class syncListPanelPopUpMenu extends JPopupMenu implements ActionListener
{
	//auto generated
	private static final long	serialVersionUID	= 6224936694416732035L;

	//some constants, it is used to know which item has been clicked
	// actually each one is associated to an item, and we retreive the info in the 	public void actionPerformed(ActionEvent e)
	public final static int PuP_EXPAND_ALL=1;
	public final static int PuP_REMOVE_NODE=2;
	public final static int PuP_DISSOCIATE_SYNCLIST=3;
	public final static int PuP_ASSOCIATE_SYNCLIST=4;
	public final static int PuP_DELETE_SYNCLIST=5;
	
	//we need this reference to access to data 
	protected SyncListPanel theSyncListPanel;

	
	//CTOR
	public syncListPanelPopUpMenu(SyncListPanel aSyncListPanel) 
	{
		//		expand the tree
		JMenuItem menuItem = new JMenuItem("Expand All", KeyEvent.VK_A);
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


	//Methods
	/**
	 * @method : void actionPerformed(ActionEvent e)
	 * 
	 *   this method is called each time the user click on an item of the menu
	 */
	public void actionPerformed(ActionEvent e)
	{
    	String s1 = e.getActionCommand();
        int actionNumber = Integer.parseInt(s1);	//we retrieve the imfo telling which item has been clicked, this value has been set using menuItem.setActionCommand()
        
        switch( actionNumber)
        {
        	case PuP_EXPAND_ALL: /**expand the tree*/
        		theSyncListPanel.expandAll();
        	 break;

        	case PuP_REMOVE_NODE: /** remove a node */
        		TreePath tp[] = theSyncListPanel.theTree.getSelectionPaths();
        		
        		theSyncListPanel.removeNodes( tp);
        		for( int i=0; i< theSyncListPanel.root.getChildCount(); i++)
        		{
        			SyncListNode aNode = (SyncListNode)theSyncListPanel.root.getChildAt(i);
        			SynclistContainer.getSingleton().save(aNode.getSynclist());
        		}
        		break;
        	 	
        	case PuP_DISSOCIATE_SYNCLIST: //remove association between device and synclist
        		
        		removeSyncList( false); //we don't delete the synclist, the "false" means to NOT delete physically the synclist
        		
        		((DefaultTreeModel)theSyncListPanel.theTree.getModel()).reload();

        		break;
        		
        	case PuP_DELETE_SYNCLIST: //delete synclist
        		
        		removeSyncList( true); //we  delete the synclist, the "true" means to delete physically the synclist

        		((DefaultTreeModel)theSyncListPanel.theTree.getModel()).reload();

        		break;
        		

        	case PuP_ASSOCIATE_SYNCLIST: //create a window to allow the user to associate a synclist to a device
        		syncListDeviceAssociation aSyncListDeviceAssociation= new syncListDeviceAssociation( theSyncListPanel);
        		aSyncListDeviceAssociation.createAndShowGUI();
        		break;
        		
        		
        	default: //do nothing
        		System.out.print("default=>");
        		System.out.print(actionNumber);
        }
	
	}
	
	
	
	/**
	 * @method : void removeSyncList(boolean deleteSyncList)
	 * 
	 *   remove a synclist from a device, if deleteSyncList==true then it deletes the synclist physicall.
	 */
	protected void removeSyncList(boolean deleteSyncList)
	{
		//we retreive the paths of the selections (to know which synclists must be dissociate  from the device 
		TreePath tq[] = theSyncListPanel.theTree.getSelectionPaths();
		
		
		// for all selections:
		for( int i=0; i < tq.length; i++)
		{
			
			TreePath aTq = tq[i];			
			Object to[] = aTq.getPath(); //we rertieves all the nodes from the slected nodes to the root
			
			if(to.length<2) continue; //if have selected the root node, we cannot know which synclist we want to remove, so we do nothing
			
			SyncListNode aSyncListNode= (SyncListNode) to[ 1];  //1 is the synclist level , which is the one interesting
			Device aDevice =  theSyncListPanel.theDevice;

			Synclist aSyncList = aSyncListNode.getSynclist();
			aDevice.unAssociateSyncList( aSyncList);
			aSyncListNode.removeMeCompletly();
			
			if( deleteSyncList) //if true we remove it fron database as well
			{
				SynclistNamesContainer.getSingleton().delete( aSyncList.getId());				
			}
			
		}
		
	}
	
	
}
