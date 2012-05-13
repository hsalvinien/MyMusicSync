package com.salvinien.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.Border;



public class myApp implements ActionListener, ItemListener
{
	 //technical members
	protected JFrame mainFrame; 
	 //temp
	 JTextArea output;
	 JScrollPane scrollPane;
	 String newline = "\n";

		//	 Actions Event
	private final static int AE_NEW_DEVICE = 1;

	public myApp()
	{		
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
		
				
		mainFrame.setJMenuBar(menuBar);
	}
		

	
	
	
	 public Container createContentPane() 
	 {
	        //Create the content-pane-to-be.
	        JPanel contentPane = new JPanel(new BorderLayout());
	        //contentPane.setOpaque(true);
	        BoxLayout l1= new BoxLayout(contentPane, BoxLayout.PAGE_AXIS);
	        contentPane.setLayout(l1);

	        
	        jtreeRootPanel jt = new jtreeRootPanel();
	        contentPane.add(jt.createPanel(), BorderLayout.NORTH);
		        
	        
	        //Create a scrolled text area.
	        output = new JTextArea(5, 30);
	        output.setEditable(false);
	        scrollPane = new JScrollPane(output);

	        //Add the text area to the content pane.
	        contentPane.add(scrollPane, BorderLayout.CENTER);
	        
	        
	        

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

	        JMenuItem source = (JMenuItem)(e.getSource());
	        String s = "Action event detected."
	        		   + s1
	                   + newline
	                   + "    Event source: " + source.getText()
	                   + " (an instance of " + getClassName(source) + ")";
	        output.append(s + newline);
	        output.setCaretPosition(output.getDocument().getLength());
	        
	    }

	    public void itemStateChanged(ItemEvent e) 
	    {
	    		    	
	        JMenuItem source = (JMenuItem)(e.getSource());
	        String s = "Item event detected."
	                   + newline
	                   + "    Event source: " + source.getText()
	                   + " (an instance of " + getClassName(source) + ")"
	                   + newline
	                   + "    New state: "
	                   + ((e.getStateChange() == ItemEvent.SELECTED) ?
	                     "selected":"unselected");
	        output.append(s + newline);
	        output.setCaretPosition(output.getDocument().getLength());
	 
	    
/*	        if( source.getText().equals(useMetronome))
	        {
	        	boolean flag =(e.getStateChange() == ItemEvent.SELECTED) ? true:false;
	        	theBand.getMetronome().setOnOff( flag);
	        }
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
    	mainFrame = new JFrame("My MuZic Synchronizer");
    	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create menus
        createMenus();
        
  
        mainFrame.setContentPane(createContentPane());

        //Display the window.
        mainFrame.setSize(450, 260);
        mainFrame.setVisible(true);

    }

	 
}
