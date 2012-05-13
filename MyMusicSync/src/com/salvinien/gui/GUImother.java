package com.salvinien.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUImother extends JPanel
{
	static final long serialVersionUID=-1;
	
	String title;
	
	myApp mom;
	
	GUImother( myApp anApp, String aTitle)
	{
        super(new BorderLayout());
        
		mom = anApp;
		title=aTitle;
	}
	
	
	public void setTitle( String aTitle)	{ title=aTitle;}
	public String  getTitle()				{ return title;}
	
	
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    void createAndShowGUI() 
    {
        //Create and set up the window.
        JFrame frame = new JFrame(title);
        
        //set up the content pane.
        setOpaque(true); //content panes must be opaque
        frame.setContentPane(this);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }


}