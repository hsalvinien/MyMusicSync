package com.salvinien.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;

import com.salvinien.mymusicsync.Device;
import com.salvinien.mymusicsync.DeviceContainer;
import com.salvinien.mymusicsync.Libelle;
import com.salvinien.playlists.PlaylistNamesContainer;



public class syncDevicePanel extends JPanel implements ActionListener
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1533245427593818182L;
	public final static int JC_DEVICE = 1;
	
	protected Device selectedDevice;
	protected JTable theDeviceTable;
	protected JPanel theDeviceListPanel ;
	
	public syncDevicePanel()
    {
        
        Border boxForm =BorderFactory.createCompoundBorder( BorderFactory.createTitledBorder("Device"), BorderFactory.createEmptyBorder(5,5,5,5)); 
        this.setBorder( boxForm);
        BoxLayout l1= new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(l1);
         
        
        this.add( createDeviceListPanel(), BorderLayout.NORTH);
        this.add( createSyncListPanel(), BorderLayout.CENTER);
        this.add( createSynchroInfoPanel(), BorderLayout.SOUTH);
        
    }

	
	
	protected void refreshInfoDevice()
	{
		
		if( selectedDevice == null) return;
		
		theDeviceTable.setValueAt(selectedDevice.getDeviceName(), 0, 0);
		theDeviceTable.setValueAt(selectedDevice.getDefaultPath(), 0, 1);
		int id = selectedDevice.getPlaylistId();
		Libelle s = PlaylistNamesContainer.getSingleton().getLibelle(id);
		theDeviceTable.setValueAt(s.getName(), 0, 2);	
	
	}
	
	protected JPanel createDeviceListPanel()
	{
		theDeviceListPanel = new JPanel();
        BoxLayout l1= new BoxLayout(theDeviceListPanel, BoxLayout.PAGE_AXIS);
        theDeviceListPanel.setLayout(l1);
	
        //the comboBox
		JComboBox<String> jC = new JComboBox<String>( DeviceContainer.getSingleton().getDevices());
		jC.setActionCommand(String.valueOf(JC_DEVICE));
		jC.addActionListener(this);
		jC.setMaximumSize(new Dimension(800, 20));
		theDeviceListPanel.add(jC, BorderLayout.NORTH);
		
		//the table
		Vector<String>Titles = new Vector<String>();
		Titles.add("Sync List Name"); Titles.add("Target"); Titles.add("# of Songs");			

		Vector<Vector<String>>rowData = new Vector<Vector<String>>(); 

		Vector< String> aRow= new Vector< String>();
		aRow.add("");
		aRow.add("");
		aRow.add("");
			
		rowData.add(aRow);
		
		theDeviceTable = new JTable( rowData, Titles);
		JScrollPane aPanel = new JScrollPane( theDeviceTable);
		theDeviceListPanel.add(aPanel, BorderLayout.CENTER);
		
		
		return theDeviceListPanel;
	}
	
	protected JPanel createSyncListPanel()
	{
		JPanel aPanel = new JPanel();
		
		
		
		return aPanel;
	}
	protected JPanel createSynchroInfoPanel()
	{
		JPanel aPanel = new JPanel();
		
		
		
		return aPanel;
	}

	
	
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
    	String s1 = e.getActionCommand();
        int actionNumber = Integer.parseInt(s1);
        
        switch( actionNumber)
        {
        	case JC_DEVICE: /*Ok it 's the comboxbox*/
        		JComboBox<String> cb = (JComboBox<String>)e.getSource();
                String aSelection = (String)cb.getSelectedItem();
                selectedDevice = DeviceContainer.getSingleton().getDevice(aSelection); 
                refreshInfoDevice();
        	break;

        	 	
        	default: //do nothing
        		System.out.print("default=>");
        		System.out.print(actionNumber);
        }	
	}


}
