package com.salvinien.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import com.salvinien.gui.tableSync.TableSync;
import com.salvinien.gui.tableSync.TableSyncModel;
import com.salvinien.synclists.SongSynchroContainer;

/*
 * @class: SyncResultPanel  
 * 
 * This class manages the result of a synchronization, and shows the last tasks where the application cannot decide
 * and ask the user what to do
 * 
 *   the actions are:
 *   	-> do nothing
 *   	-> copy from root to device
 *   	-> copy from device to root
 * 
 * a click on the column header set the flag for the whole columns
 * 
 * 
 *  it implements an ActionListener to catch the clicks on the buttons
 *  ! the clicks on the table is managed by the table
 *  
 *  it extends a GuiMother (which helps to create a simple window)
 *  
 */

public class SyncResultPanel extends GUImother implements ActionListener
{
	private static final long	serialVersionUID	= 7462800434070553694L;

	//some constants, it is used to know which item has been clicked
	// actually each one is associated to an item, and we retreive the info in the 	public void actionPerformed(ActionEvent e)
	private final int AE_QUIT=1;
	private final int AE_FINISH_SYNC=2;
	
	protected JTable theSyncTable; 
	
	
	//CTOR
	SyncResultPanel(myApp anApp, SongSynchroContainer aContainer) 
	{
		super(anApp, "SyncResults");
        
        BoxLayout mainLayout = new BoxLayout(this, BoxLayout.LINE_AXIS);
        setLayout( mainLayout);
                
        
        JPanel box = createPanel( aContainer);     
        
        add(box); 
    }

    


	//Methods
	/*@method : void createPanel()
	 * 
	 *   create all fields, and buttons
	 *    
	 *   it is done into two (sub)panels in order to put fields at the right place
	 *     
	 */
    protected JPanel createPanel( SongSynchroContainer aContainer)
    {
        JPanel panelForm = new JPanel();
        Border boxForm =BorderFactory.createCompoundBorder( BorderFactory.createTitledBorder("Controls"), BorderFactory.createEmptyBorder(5,5,5,5)); 
        panelForm.setBorder( boxForm);
        BoxLayout l1= new BoxLayout(panelForm, BoxLayout.PAGE_AXIS);
        panelForm.setLayout(l1);

        
		//create the table
		TableSyncModel tsm = new TableSyncModel( aContainer); 
		theSyncTable = new TableSync( tsm);

				//into a jscroll pane 
		JScrollPane aScPanel = new JScrollPane( theSyncTable);
		
		panelForm.add(aScPanel);

		
		//create the buttons
		panelForm.add( createButtons());
     	  
        return panelForm;
    }

    
	/*@method : void createButtons()
	 * 
	 *   create the buttons
	 *    
	 *     
	 */
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


	
	//Methods
	/*@method : void actionPerformed(ActionEvent e)
	 * 
	 *   this method is called each time the user click on one of the buttons
	 */
    public void actionPerformed(ActionEvent e) 
    {
    	  String s = e.getActionCommand();
        int command = Integer.parseInt(s);

        switch( command)
        {
        	case AE_FINISH_SYNC:	
        		
        		TableSyncModel tm = (TableSyncModel)theSyncTable.getModel();
        		SongSynchroContainer theContainer =tm.getData();
        		
        		theContainer.synchronize(); //do the synchronization 
        		
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
