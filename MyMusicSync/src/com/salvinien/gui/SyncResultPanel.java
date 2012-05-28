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

import com.salvinien.synclists.SongSynchroContainer;


public class SyncResultPanel extends GUImother implements ActionListener
{
	private static final long	serialVersionUID	= 7462800434070553694L;

	private final int AE_QUIT=1;
	private final int AE_FINISH_SYNC=2;
	
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

		panelForm.add( createButtons());
     	  
        return panelForm;
    }

    
    protected JPanel createButtons()
    {
        JPanel panelForm = new JPanel();
        BoxLayout l1= new BoxLayout(panelForm, BoxLayout.LINE_AXIS);
        panelForm.setLayout(l1);

        
        JButton aButton = new JButton( "Finish Sync");
        aButton.setActionCommand(String.valueOf(AE_FINISH_SYNC));

        //Register a listener for the radio buttons.
        aButton.addActionListener(this);          
     	panelForm.add( aButton);

     	  
        aButton = new JButton( "Quit");
        aButton.setActionCommand(String.valueOf(AE_QUIT));

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
        	case AE_FINISH_SYNC:	
        		
        		this.setVisible(false);
        		theFrame.dispose();
        		break;
        		
        	case AE_QUIT:	
        		this.setVisible(false);
        		theFrame.dispose();		
        		break;
        		
        	default:
        }
        
        
        repaint();
    }

	
}
