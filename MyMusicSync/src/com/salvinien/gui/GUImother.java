package com.salvinien.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @class: GUImother
 * 
 * This class manages a window, it used as an help to create a simple window
 * (you just have to subclass this)
 * 
 * ---This may to be changed---
 * 
 */

public class GUImother extends JPanel
{
	static final long serialVersionUID=-1;
	
	//
	String title;
	myApp mom;
	JFrame theFrame;
	
	
	//CTOR
	GUImother( myApp anApp, String aTitle)
	{
        super(new BorderLayout());
        
		mom = anApp;
		title=aTitle;
	}
	
	
	public void setTitle( String aTitle)	{ title=aTitle;}
	public String  getTitle()				{ return title;}
	
	
    /**
     * @method : void createAndShowGUI() 
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    void createAndShowGUI() 
    {
        //Create and set up the window.
    	theFrame = new JFrame(title);
        
        //set up the content pane.
        setOpaque(true); //content panes must be opaque
        theFrame.setContentPane(this);

        //Display the window.
        theFrame.pack();
        theFrame.setVisible(true);
    }


}
