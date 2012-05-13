package com.salvinien.mymusicsync;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;

import javax.swing.JPanel;

public class ScrDialogInit extends JDialog implements ActionListener 
{
  /**
	 * 
	 */
  private static final long	serialVersionUID	= 3344291444433181236L;

  protected JFileChooser fileChooser= new JFileChooser();
  
  public File getRootFile()
  {
	  File aFile = fileChooser.getCurrentDirectory();
	  return aFile;
  }
  
  public ScrDialogInit(JFrame parent) 
  {
	  
	  
    super(parent, "Init MusicSync", true);
    if (parent != null) 
    {
      Dimension parentSize = parent.getSize(); 
      Point p = parent.getLocation(); 
      setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
    }
    
    JLabel aLabel = new JLabel( "Select your Music root directory");
    JPanel messagePane1 = new JPanel();
    messagePane1.add(aLabel);
    getContentPane().add(messagePane1, BorderLayout.NORTH);
   
    fileChooser.setMultiSelectionEnabled(false);
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);


    JPanel messagePane = new JPanel();
    messagePane.add(fileChooser);

    getContentPane().add(messagePane);
    JPanel buttonPane = new JPanel();
    JButton button = new JButton("This one is the root directoy of my MUSIC !!!"); 
    buttonPane.add(button); 
    button.addActionListener(this);
    getContentPane().add(buttonPane, BorderLayout.SOUTH);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    pack(); 
    setVisible(true);
  }
  
  public void actionPerformed(ActionEvent e) 
  {
    setVisible(false); 
    dispose(); 
  }
  
  
  
}