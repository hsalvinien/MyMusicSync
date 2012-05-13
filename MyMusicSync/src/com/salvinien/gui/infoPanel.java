package com.salvinien.gui;


import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import com.salvinien.discography.ArtistContainer;
import com.salvinien.discography.SongContainer;

public class infoPanel extends JPanel
{
	private static final long	serialVersionUID	= 6187409787084974465L;

	public infoPanel()
	      {
	          
	          Border boxForm =BorderFactory.createCompoundBorder( BorderFactory.createTitledBorder("Information"), BorderFactory.createEmptyBorder(5,5,5,5)); 
	          this.setBorder( boxForm);
	          BoxLayout l1= new BoxLayout(this, BoxLayout.PAGE_AXIS);
	          this.setLayout(l1);
	           
	          
	          this.add( createSyncListPanel(), BorderLayout.NORTH);
	          this.add( createArtistPanel(), BorderLayout.CENTER);
	          this.add( createAlbumPanel(), BorderLayout.SOUTH);
	          
	      }
	
	
	
	
	protected JPanel createSyncListPanel()
	{
		JPanel aPanel = new JPanel();
		
		
		
		return aPanel;
	}
	
	protected JScrollPane createArtistPanel()
	{
		JScrollPane aPanel;
		

		Vector<String>Titles = new Vector<String>();
		Titles.add("Artist"); Titles.add("# of Albums"); Titles.add("# of Songs");			

	    String[] t = ArtistContainer.getSingleton().getLibelles();
	    Arrays.sort(t);

		Vector< Vector<String>> rowData = new Vector< Vector<String>>() ;
		for( int i=0; i< t.length; i++)
		{
			Vector<String> v =  new Vector<String>();
			v.add(t[i]);
			int artistID = ArtistContainer.getSingleton().getId(t[i]);
			
			int a = SongContainer.getSingleton().getNbOfAlbums( artistID) ;
			v.add( String.valueOf(a));
			a = SongContainer.getSingleton().getNbOfSongs( artistID);
			v.add( String.valueOf(a));
			
			rowData.add( v);
		}
		
		JTable table = new JTable( rowData, Titles);
	    
	    aPanel = new JScrollPane( table);
        
	    Border boxForm =BorderFactory.createCompoundBorder( BorderFactory.createTitledBorder("Artists"), BorderFactory.createEmptyBorder(5,5,5,5)); 
        aPanel.setBorder( boxForm);

        
		return aPanel;
	}
	protected JPanel createAlbumPanel()
	{
		JPanel aPanel = new JPanel();
		
		
		
		return aPanel;
	}

}
