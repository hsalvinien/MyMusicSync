package com.salvinien.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.salvinien.discography.AlbumContainer;
import com.salvinien.discography.ArtistContainer;
import com.salvinien.discography.Song;
import com.salvinien.discography.SongContainer;




public class jtreeRootPanel
{
	class nodeAlbum
	{
		DefaultMutableTreeNode node;
		
		nodeAlbum( String aString)
		{
			node = new DefaultMutableTreeNode( aString);
		}
	}
	class nodeArtist
	{
		DefaultMutableTreeNode node; 
		HashMap< Integer, nodeAlbum> containerAlbum;
		
		nodeArtist( String aString)
		{
			node = new DefaultMutableTreeNode( aString);
			containerAlbum = new HashMap< Integer, nodeAlbum>();
		}
		
		
		nodeAlbum getAlbum( Song aSong)
		{
			nodeAlbum aNodeAlbum = containerAlbum.get(aSong.getAlbumID());
			
			if( aNodeAlbum==null)
			{
				aNodeAlbum = new nodeAlbum( AlbumContainer.getSingleton().getName(aSong.getAlbumID()) );
				containerAlbum.put( aSong.getAlbumID(), aNodeAlbum);
				node.add( aNodeAlbum.node);
			}
			
			return aNodeAlbum;
		}
		
	}
	
	
	/////////////////////////////////////////////////////////////////////////////
	private HashMap< Integer, nodeArtist> containerArtist;
 	private  DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
	
	
	public jtreeRootPanel()
	{

	    containerArtist = new HashMap< Integer, nodeArtist>();
	}
	
	  
	      
	protected DefaultMutableTreeNode getAlbum( Song aSong)
	{
		
		nodeArtist aNodeArtist = getArtist( aSong.getArtistID());
		
		nodeAlbum aNodeAlbum = aNodeArtist.getAlbum(aSong);
		
		return aNodeAlbum.node;
	}

	protected nodeArtist getArtist( int artistId)
	{
		nodeArtist  aNodeArtist = containerArtist.get(artistId);
		
		if( aNodeArtist == null)
		{
			aNodeArtist = new nodeArtist( ArtistContainer.getSingleton().getName(artistId));
			
			containerArtist.put(artistId, aNodeArtist);
			root.add( aNodeArtist.node);
		}
		
		return aNodeArtist;
	}

	protected nodeArtist getArtist( String artistName)
	{
		int artistId = ArtistContainer.getSingleton().getLibelle(artistName).getId();
		nodeArtist  aNodeArtist = containerArtist.get(artistId);
		
		if( aNodeArtist == null)
		{
			aNodeArtist = new nodeArtist( ArtistContainer.getSingleton().getName(artistId));
			
			containerArtist.put(artistId, aNodeArtist);
			root.add( aNodeArtist.node);
		}
		
		return aNodeArtist;
	}

	  	
      public JPanel createPanel()
      {
          JPanel panelForm = new JPanel();
          
          Border boxForm =BorderFactory.createCompoundBorder( BorderFactory.createTitledBorder("My music"), BorderFactory.createEmptyBorder(5,5,5,5)); 
          panelForm.setBorder( boxForm);
          BoxLayout l1= new BoxLayout(panelForm, BoxLayout.PAGE_AXIS);
          panelForm.setLayout(l1);
           
       	  
	      JTree tree2 = new JTree(root);
	     
	      
	      //pre-fill of the artists ordered by name
	      String[] t = ArtistContainer.getSingleton().getLibelles();
	      Arrays.sort(t);
	      for( int i=0; i< t.length; i++)
	      {
	    	  getArtist( t[i]);
	      }
	      
	      Iterator<Song>  it =   SongContainer.getSingleton().getSongIteratorID();
	      while( it.hasNext())
	      {
	    	  Song aSong = it.next();
	    	  DefaultMutableTreeNode child = new DefaultMutableTreeNode(aSong.getName());

	    	  DefaultMutableTreeNode  aNode = getAlbum( aSong);
	    	  
	    	  aNode .add( child);
	      }
	      
	      
	      tree2.expandRow(0); 
	      tree2.setRootVisible(false);
	  	
	      
	      DefaultTreeCellRenderer renderer2 = new DefaultTreeCellRenderer();
	      renderer2.setOpenIcon(null);
	      renderer2.setClosedIcon(null);
	      renderer2.setLeafIcon(null);
	      tree2.setCellRenderer(renderer2);
	      JScrollPane pane2 = new JScrollPane(tree2);
	      panelForm.add(pane2);
       	  
       	  
       	  
          return panelForm;
      }

}
