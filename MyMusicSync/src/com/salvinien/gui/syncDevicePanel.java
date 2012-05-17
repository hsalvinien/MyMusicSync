package com.salvinien.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.salvinien.discography.SongSynchro;
import com.salvinien.discography.SongSynchroContainer;
import com.salvinien.mymusicsync.Device;
import com.salvinien.mymusicsync.DeviceContainer;
import com.salvinien.mymusicsync.DeviceSyncList;
import com.salvinien.mymusicsync.syncTask;
import com.salvinien.playlists.Playlist;
import com.salvinien.playlists.PlaylistContainer;



public class syncDevicePanel extends JPanel implements ActionListener
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1533245427593818182L;
	private final static int MAX_WIDTH 		= 600;
	public final static int JC_DEVICE 		= 1;
	public final static int B_ANALYSIS 		= 2;
	public final static int B_SYNCHRONIZE 	= 3;
	
	protected Device selectedDevice;
	protected JTable theDeviceTable;
	protected JTable theSyncTable;
	protected JPanel theDeviceListPanel ;
	protected JButton bAnalysis;
	protected JButton bSync;

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
		
		Vector<DeviceSyncList> v = selectedDevice.getDeviceSyncLis();
		
		//1) remove all previous data
		DefaultTableModel dm = (DefaultTableModel)theDeviceTable.getModel();
		dm.getDataVector().removeAllElements();
		
		for( int i = 0; i< v.size(); i++)
		{
			Vector<String> row = new Vector<String>();

			int anId = v.get(i).getPlaylistId();
			
			Playlist aPlaylist = PlaylistContainer.getSingleton().getPlaylist(anId);
			String name = aPlaylist.getName();
			row.add( name);
			
			row.add( v.get(i).getDefaultPath());
			int nb = aPlaylist.getSize();
			String s= String.valueOf(nb);
			row.add( s);
			
			dm.addRow(row);
		}
			
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
		jC.setMaximumSize(new Dimension(MAX_WIDTH, 20));
		theDeviceListPanel.add(jC, BorderLayout.NORTH);
        String aSelection = (String)jC.getItemAt(0);
        selectedDevice = DeviceContainer.getSingleton().getDevice(aSelection); 

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
		
		 DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();    
	     dtcr.setHorizontalAlignment(SwingConstants.CENTER);  

		TableColumn column = theDeviceTable.getColumnModel().getColumn(0);
		int width = 100;
		column.setPreferredWidth(width);	//fixes the column size
		column.setMaxWidth(width);
		column.setWidth(width);

		column = theDeviceTable.getColumnModel().getColumn(2);
		width = 70;
		column.setPreferredWidth(width);	//fixes the column size
		column.setMaxWidth(width);
		column.setWidth(width);
		column.setCellRenderer(dtcr);  //centering the text  

		
		JScrollPane aPanel = new JScrollPane( theDeviceTable);
		theDeviceListPanel.add(aPanel, BorderLayout.CENTER);
		Dimension d = new Dimension( MAX_WIDTH, 60);
		aPanel.setMaximumSize(d);
		aPanel.setPreferredSize(d);
		
		refreshInfoDevice(); //to set first info
		
		////////////////
		bAnalysis = new JButton("Analyse");
		bAnalysis.setActionCommand( String.valueOf(B_ANALYSIS));
		bAnalysis.addActionListener(this);
	
		theDeviceListPanel.add(bAnalysis);
		
		bSync = new JButton("Synchronize");
		bSync.setActionCommand( String.valueOf(B_SYNCHRONIZE));
		bSync.addActionListener(this);
	
		theDeviceListPanel.add(bSync);
		
		
		
		return theDeviceListPanel;
	}
	
	protected JPanel createSyncListPanel()
	{
		JPanel aPanel = new JPanel();
		
		Vector< String> Titles = new Vector<String>();
		Titles.add( "Artist"); Titles.add( "Album");Titles.add( "SongFrom");Titles.add( "ActionFrom");Titles.add( "DoNothing");Titles.add( "ActionTo");Titles.add( "SongTo");Titles.add( "ModifySyncList");

		Vector< Vector<String>> rowData = new Vector< Vector<String>>();
		Vector<String> row = new Vector<String>(); 
		row.add(" ");row.add(" ");row.add(" ");row.add(" ");row.add(" ");row.add(" ");row.add(" ");row.add(" ");	
		rowData.add(row);
		
		theSyncTable = new JTable( rowData, Titles);
		JScrollPane aScPanel = new JScrollPane( theSyncTable);
		
		aPanel.add(aScPanel);
		
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

        	case B_ANALYSIS:
        		SongSynchroContainer C = analyse();
        		displayAnalyse( C);
        	break;
        		
        	case B_SYNCHRONIZE:
        	break;
        		
        	default: //do nothing
        		System.out.print("default=>");
        		System.out.print(actionNumber);
        }	
	}


	
	public void displayAnalyse( SongSynchroContainer aContainer )
	{
		Iterator< SongSynchro> it = aContainer.iterator();
		
		
		//1) remove all previous data
		DefaultTableModel dm = (DefaultTableModel)theSyncTable.getModel();
		dm.getDataVector().removeAllElements();
		
		while( it.hasNext())
		{
			SongSynchro aSongSynchro= it.next();
			Vector<String> row =  aSongSynchro.toVstring();
			
			dm.addRow(row);
		}

		
	}
	
	public SongSynchroContainer analyse()
	{
		SongSynchroContainer aContainer = new SongSynchroContainer();

		Vector<DeviceSyncList> vSyncList = selectedDevice.getDeviceSyncLis();
		for( int i=0; i < vSyncList.size(); i++)
		{
			syncTask aTask = new syncTask( selectedDevice,  vSyncList.get(i));
		
			aTask.sync( aContainer);
		}
		
		
		return aContainer;
	}
}
