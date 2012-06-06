package com.salvinien.gui;


import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import com.salvinien.discography.SongContainer;
import com.salvinien.gui.tableTree.AbstractTableTreeModel;
import com.salvinien.gui.tableTree.MusicNode;
import com.salvinien.gui.tableTree.SongContainerModel;
import com.salvinien.gui.tableTree.TableTree;


/*
 * @class: infoPanel  
 * 
 * This class manages the panel (left side of the main frame), which is the root library tree
 * 
 * the real intelligence of this part is in the tabel (com.salvinien.gui.tableTree)
 * 
 */

public class infoPanel extends JPanel
{
	private static final long	serialVersionUID	= 6187409787084974465L;

	//CTOR
	public infoPanel()
	      {
	          
	          Border boxForm =BorderFactory.createCompoundBorder( BorderFactory.createTitledBorder("Multimedia Lib"), BorderFactory.createEmptyBorder(5,5,5,5)); 
	          this.setBorder( boxForm);
	          BoxLayout l1= new BoxLayout(this, BoxLayout.PAGE_AXIS);
	          this.setLayout(l1);
	           
		  	  MusicNode root = new MusicNode(SongContainer.getSingleton());
				
			  AbstractTableTreeModel TTm = new SongContainerModel( root);
			  TableTree treeTable = new TableTree(TTm);	          
	          JScrollPane JS = new JScrollPane(treeTable );
	          
	          
	          this.add( JS, BorderLayout.NORTH);
	          
	      }
	
}
