package com.salvinien.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.salvinien.database.MyDatabase;


/**
 * @class: myApp
 * 
 * This class manages the main window of the App, it is called from the main (after some iniitalization) 
 * 
 * 
 */

public class myApp extends JFrame 
{
	private static final long	serialVersionUID	= -1008495897396933252L;



	//CTOR
	public myApp()
	{		
		try
		{//set the look and feel
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		//set some callbacks 
		addWindowCallbacks();
	}

	

	//Methods
	/**
	 * @method : void addWindowCallbacks()
	 * 
	 *   sets some callback which are called depending on the actions 
	 *   read the method names, it is self explainatory
	 */	
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

	
	
	
	
	
	/**
	 * @method : Container createContentPane()
	 * 
	 *   creates the window
	 *   
	 */	
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
	        
	        
	        //DEVICES ZONES
	        syncDevicePanel s= new syncDevicePanel( this);
	        contentPane.add(s, BorderLayout.EAST);
	        
	        return contentPane;
	    }

	
	
	
	/**
	 * @method : void createAndShowGUI()
	 * 
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

  
        //main display
        Container aContainer = createContentPane(); 
        this.setContentPane( aContainer);

        //Display the window.
        this.setSize(1200, 900);
        this.setVisible(true);
    }


    
	/**
	 * @method : void CLEANEXIT()
	 *
	 * method called when the  user closes the app
	 * 
	 * it allows us to close cleanly the database
	 * 
     */

    private void CLEANEXIT()
    {
    	MyDatabase.getSingleton().close();
    	
    	dispose();
        System.exit(0); 
    }
	 
}
