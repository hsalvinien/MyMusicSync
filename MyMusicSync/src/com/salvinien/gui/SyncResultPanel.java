package com.salvinien.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import com.salvinien.discography.SongSynchroContainer;


public class SyncResultPanel extends GUImother implements ActionListener
{
	private static final long	serialVersionUID	= 7462800434070553694L;

	protected JTable theSyncTable; 
	
	
	SyncResultPanel(myApp anApp, SongSynchroContainer aContainer) 
	{
		super(anApp, "SyncResults");
        
        BoxLayout mainLayout = new BoxLayout(this, BoxLayout.LINE_AXIS);
        setLayout( mainLayout);
                
        
        JPanel box = createPanel( aContainer);     
        
        add(box); 
    }

    


	
    protected JPanel createPanel( SongSynchroContainer aContainer)
    {
        JPanel panelForm = new JPanel();
        Border boxForm =BorderFactory.createCompoundBorder( BorderFactory.createTitledBorder("Controls"), BorderFactory.createEmptyBorder(5,5,5,5)); 
        panelForm.setBorder( boxForm);
        BoxLayout l1= new BoxLayout(panelForm, BoxLayout.PAGE_AXIS);
        panelForm.setLayout(l1);

        
        
		
		Vector< String> Titles = new Vector<String>();
		Titles.add( "Artist"); Titles.add( "Album");Titles.add( "SongFrom");Titles.add( "ActionFrom");Titles.add( "DoNothing");Titles.add( "ActionTo");Titles.add( "SongTo");Titles.add( "ModifySyncList");

/*		Vector< Vector<String>> rowData = new Vector< Vector<String>>();
		Vector<String> row = new Vector<String>(); 			
		rowData.add(row);
		
		theSyncTable = new JTable( rowData, Titles);
*/		
/*		SyncSongTableModel dm = (SyncSongTableModel) theSyncTable.getModel();
		dm.setData(aContainer);
		dm.fireTableDataChanged();
*/		
		SyncSongTableModel md = new SyncSongTableModel(aContainer);  
		theSyncTable = new JTable( md);

		
		JScrollPane aScPanel = new JScrollPane( theSyncTable);
		
		panelForm.add(aScPanel);
		

        
        
        
        
        
        
        JButton aButton = new JButton( "Finish Sync");
        aButton.setActionCommand("100");

        //Register a listener for the radio buttons.
        aButton.addActionListener(this);          
     	  panelForm.add( aButton);

     	  
        aButton = new JButton( "Quit");
        aButton.setActionCommand("101");

        //Register a listener for the radio buttons.
        aButton.addActionListener(this);          
     	  panelForm.add( aButton);
     	  
        return panelForm;
    }

    



	
    
    public void actionPerformed(ActionEvent e) 
    {
    	  String s = e.getActionCommand();
        int command = Integer.parseInt(s);

        switch( command)
        {
        	case 100:	
        		
        		break;
        		
        	case 101:	
        		
        		break;
        		
        	default:
        }
        
        
        repaint();
    }

	
}
