package com.salvinien.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;


import com.salvinien.mymusicsync.Device;
import com.salvinien.mymusicsync.DeviceSyncList;
import com.salvinien.mymusicsync.Libelle;
import com.salvinien.synclists.Synclist;
import com.salvinien.synclists.SynclistContainer;
import com.salvinien.synclists.SynclistNamesContainer;

public class syncListDeviceAssociation extends GUImother implements ActionListener
{

	private static final long	serialVersionUID	= -1181868958445375458L;
	private final int AC_CANCEL = 1;
	private final int AC_SAVE = 2;
	private final int AC_CHOSEN = 3;
	
	protected  Device  theDevice;
	protected JTextField theSyncListName;
	protected JTextField theDirectory;
	protected SyncListPanel theSyncListPanel;
	
	public syncListDeviceAssociation( SyncListPanel aSyncListPanel) 
	{
		super(aSyncListPanel.theMom, "syncListDeviceAssociation");
		
	
		theSyncListPanel = aSyncListPanel;
        theDevice = theSyncListPanel.theDevice;

		
        BoxLayout mainLayout = new BoxLayout(this, BoxLayout.LINE_AXIS);
        setLayout( mainLayout);
        Border boxForm =BorderFactory.createCompoundBorder( BorderFactory.createTitledBorder("SyncLists"), BorderFactory.createEmptyBorder(5,5,5,5)); 
        setBorder( boxForm);
                
        
        createPanel();     
                 
    }

    


	
    protected void createPanel()
    {
        BoxLayout l1= new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(l1);

        JPanel mainPanel = createMainPanel();
        add( mainPanel, BorderLayout.NORTH);
        
        
        JPanel aButtonPanel= createButtonPanel();        
        add( aButtonPanel, BorderLayout.SOUTH);
        
    }

    
    protected JPanel createMainPanel()
    {
    	JPanel aPanel =new JPanel();
        BoxLayout l1= new BoxLayout(aPanel, BoxLayout.PAGE_AXIS);
        aPanel.setLayout(l1);
    
    	JComboBox<String> jC = new JComboBox<String>( SynclistNamesContainer.getSingleton().getLibelles());
		jC.setActionCommand(String.valueOf(AC_CHOSEN));
		jC.addActionListener(this);
		//jC.setMaximumSize(new Dimension(MAX_WIDTH, 20));
		aPanel.add(jC, BorderLayout.NORTH);
		
		aPanel.add(  createSyncListName() , BorderLayout.CENTER);
    	
		aPanel.add(createSyncDirectory(), BorderLayout.SOUTH);
		
    	return aPanel;
    }
    

    protected JPanel createSyncListName()
    {
    	JPanel aPanel =new JPanel();
        BoxLayout l1= new BoxLayout(aPanel, BoxLayout.LINE_AXIS);
        aPanel.setLayout(l1);
    
        aPanel.add(new JLabel("Sync List Name : "), BorderLayout.WEST);
        
		theSyncListName = new JTextField();
		aPanel.add(theSyncListName, BorderLayout.EAST);
    	
			
    	return aPanel;
    }
    
    protected JPanel createSyncDirectory()
    {
    	JPanel aPanel =new JPanel();
        BoxLayout l1= new BoxLayout(aPanel, BoxLayout.LINE_AXIS);
        aPanel.setLayout(l1);
    
        aPanel.add(new JLabel("Directory : "), BorderLayout.WEST);
        
        theDirectory = new JTextField();
		aPanel.add(theDirectory, BorderLayout.EAST);
    	
			
    	return aPanel;
    }
    

    
    
    
    protected JPanel createButtonPanel()
    {
        JPanel panelForm = new JPanel();
        BoxLayout l1= new BoxLayout(panelForm, BoxLayout.LINE_AXIS);
        panelForm.setLayout(l1);
        
        JButton aButton = new JButton( "Save association");
        aButton.setActionCommand(String.valueOf(AC_SAVE));

        //Register a listener for the radio buttons.
        aButton.addActionListener(this);          
     	panelForm.add( aButton, BorderLayout.WEST);

     	  
        aButton = new JButton( "Cancel");
        aButton.setActionCommand( String.valueOf(AC_CANCEL));

        //Register a listener for the radio buttons.
        aButton.addActionListener(this);          
     	panelForm.add( aButton, BorderLayout.EAST);
     	  
        return panelForm;
    }

    



	
    
    public void actionPerformed(ActionEvent e) 
    {
    	  String s = e.getActionCommand();
        int command = Integer.parseInt(s);

        switch( command)
        {
        	case AC_CHOSEN:	
        		JComboBox<String> cb = (JComboBox<String>)e.getSource();
                String aSelection = (String)cb.getSelectedItem();
                theSyncListName.setText(aSelection);
        		break;

        	case AC_CANCEL:	
        		this.setVisible(false);
        		theFrame.dispose();
        		
        		break;
        		
        	case AC_SAVE:	
        		
        		String syncListName = theSyncListName.getText();
        		Libelle aLibelle = SynclistNamesContainer.getSingleton().getLibelle(syncListName);
        		if( aLibelle ==null)
        		{//so it is a new synclist, we create it
        			aLibelle = SynclistNamesContainer.getSingleton().create(syncListName);
        			Synclist p = SynclistContainer.getSingleton().getSynclist(aLibelle.getId());
        			if( p==null)
    			    	{
    			    		p= new Synclist( aLibelle.getId());
    			    		SynclistContainer.getSingleton().addSynclist(p);
    			    	}
        		}
        		
        		Synclist aSynclist= new Synclist( aLibelle.getId());
        		String aDirectory = theDirectory.getText();
        		DeviceSyncList aDeviceSyncList = new DeviceSyncList( aDirectory, aSynclist.getId());
        		
        		theDevice.addDeviceSyncList(aDeviceSyncList);
        		
        		theSyncListPanel.setNewDevice(theDevice);
        		
        		this.setVisible(false);
        		theFrame.dispose();
        		
        		break;
        		
        	default:
        }
        
        
        repaint();
    }

	
}
