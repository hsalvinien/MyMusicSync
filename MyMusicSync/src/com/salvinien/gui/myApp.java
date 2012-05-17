package com.salvinien.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.salvinien.database.MyDatabase;



public class myApp extends JFrame implements ActionListener
{
	private static final long	serialVersionUID	= -1008495897396933252L;


		//	 Actions Event
	private final static int AE_NEW_DEVICE = 1;

	public myApp()
	{		
		
		addWindowCallbacks();
	}

	

	
	private void addWindowCallbacks()
	{
		addWindowListener(new WindowListener() 
		{
            public void windowClosed(WindowEvent arg0) 
            {
                System.out.println("Window close event occur");
            }
            public void windowActivated(WindowEvent arg0) 
            {
                //call each time the window is activated
            }
            public void windowClosing(WindowEvent arg0) 
            {
            	//call each time the window is Closing"
            	CLEANEXIT();
            }
            public void windowDeactivated(WindowEvent arg0) 
            {
            	//call each time the window is Deactivated
            }
            public void windowDeiconified(WindowEvent arg0) 
            {
            	//call each time the window is Deiconified
            }
            public void windowIconified(WindowEvent arg0) 
            {
            	//call each time the window is Iconified
            }
            public void windowOpened(WindowEvent arg0) 
            {
            	//call each time the window is Opened
            }
        });
	}

	
	private JMenu createMenuFile()
	{
		JMenu menu;
		JMenuItem menuItem;
		
//		Build the Metronome menu.
		menu = new JMenu("Devices");
		menu.setMnemonic(KeyEvent.VK_P);
		menu.getAccessibleContext().setAccessibleDescription( "This menu manages Devices");

//		new DEvice
		menuItem = new JMenuItem("New Device", KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription( "Create a Device");
		menuItem.setActionCommand(  String.valueOf(AE_NEW_DEVICE));
		menuItem.addActionListener(this);
		menu.add(menuItem);

		
		return(menu);
	}

	 
	private void createMenus()
	{
		JMenuBar menuBar;
		
//			Where the GUI is created:
		JMenu menu;

//			Create the menu bar.
		menuBar = new JMenuBar();

		
//			PROFILE menu.
		menu = createMenuFile();
		menuBar.add(menu);
		
				
		//mainFrame.setJMenuBar(menuBar);
		this.setJMenuBar(menuBar);
	}
		

	
	
	
	 public Container createContentPane() 
	 {
	        //Create the content-pane-to-be.
	        JPanel contentPane = new JPanel(new BorderLayout());
	        //contentPane.setOpaque(true);
	        BoxLayout l1= new BoxLayout(contentPane, BoxLayout.LINE_AXIS);
	        contentPane.setLayout(l1);


	        //INFO ZONE
	        infoPanel infoP = new infoPanel (); 
	        contentPane.add(infoP, BorderLayout.WEST);
	        
	        //SONGS ZONE
	        jtreeRootPanel jt = new jtreeRootPanel();
	        contentPane.add(jt, BorderLayout.CENTER);
		        
	        
	        //DEVICES ZONES
	        syncDevicePanel s= new syncDevicePanel();
	        contentPane.add(s, BorderLayout.EAST);
	        
	        return contentPane;
	    }

	    public void actionPerformed(ActionEvent e) 
	    {
	    	String s1 = e.getActionCommand();
	        int actionNumber = Integer.parseInt(s1);
	        
	        switch( actionNumber)
	        {
	        	case AE_NEW_DEVICE: /*Create a new device*/
	        		//GuiMetronomeSetup aGuiMetronomeSetup= new GuiMetronomeSetup( this); 
	        		//aGuiMetronomeSetup.createAndShowGUI();
	        	 break;

	        	 	
	        	default: //do nothing
	        		System.out.print("default=>");
	        		System.out.print(actionNumber);
	        }

/*	        JMenuItem source = (JMenuItem)(e.getSource());
	        String s = "Action event detected."
	        		   + s1
	                   + newline
	                   + "    Event source: " + source.getText()
	                   + " (an instance of " + getClassName(source) + ")";
	        output.append(s + newline);
	        output.setCaretPosition(output.getDocument().getLength());
*/	        
	    }


	    // Returns just the class name -- no package info.
	    protected String getClassName(Object o) 
	    {
	        String classString = o.getClass().getName();
	        int dotIndex = classString.lastIndexOf(".");
	        return classString.substring(dotIndex+1);
	    }


	
	
	
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void createAndShowGUI() 
    {
        //Create and set up the window.
    	//mainFrame = new JFrame("My MuZic Synchronizer");
    	this.setTitle("My MuZic Synchronizer");
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create menus
        createMenus();
        
  
        //main display
        Container aContainer = createContentPane(); 
        this.setContentPane( aContainer);

        //Display the window.
        this.setSize(450, 260);
        this.setVisible(true);
    }

    
    private void CLEANEXIT()
    {
    	MyDatabase.getSingleton().close();
    	
    	dispose();
        System.exit(0); 
    }
	 
}
