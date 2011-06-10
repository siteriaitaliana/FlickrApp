package com.flickrapp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class AuthExample {
	
	protected Flickr f;
    protected RequestContext requestContext;
    protected String frob;
    protected String token = null;
    protected Properties properties = null;
    protected AuthInterface  authInterface;
    protected Auth auth;
    protected JFrame frameint;
    protected JFrame frameest;
    protected String web_url;
    protected JPanel webBrowserPanel;
    protected JWebBrowser webBrowser;
    protected Properties property;
    protected JButton authenticatebutt;
    protected JPanel ButtonsPanel;
    protected JPanel SetIdPanel;
    protected JPanel PhotoPanel;
    protected JButton search_set;
    protected JTextField set_id;
    
	private void startestUI() {
		frameest = new JFrame("FlickrAuthentication");
		frameest.setLayout(new BorderLayout());
        frameest.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameest.setSize(900, 900);
        frameest.setLocationByPlatform(true);
        frameest.setVisible(true);
	}
    
    private void startWebbrowser(String web_url) {
	    webBrowserPanel = new JPanel(new BorderLayout());
	    webBrowser = new JWebBrowser();
	    webBrowser.navigate(web_url);
	    webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
	    frameest.add(webBrowserPanel, BorderLayout.CENTER);
	  }
    
    public void initGUI() {
	    UIUtils.setPreferredLookAndFeel();
	    NativeInterface.open();
	    SwingUtilities.invokeLater(new Runnable() {
	      public void run() {
	        frameint = new JFrame("FlickrApp");
	        frameint.setLayout(new BorderLayout());
	        frameint.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frameint.setSize(1200, 950);
	        frameint.setLocationByPlatform(true);
	        frameint.setVisible(true);
	        authenticatebutt = new JButton("Authenticate");
	        authenticatebutt.setSize(300, 300);
	        frameint.add(authenticatebutt, BorderLayout.CENTER);
	        
	        authenticatebutt.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
        	        try {
        				flickrAuthenticate();
        			} catch (ParserConfigurationException ex) {
        				// TODO Auto-generated catch block
        				ex.printStackTrace();
        			}
        			catch (IOException ex) {
        				ex.printStackTrace();
        			} catch (SAXException ex) {
        				ex.printStackTrace();
        			} catch (FlickrException ex) {
        				ex.printStackTrace();
        			}
                }
            });
	        
	      }
	    });
	    NativeInterface.runEventPump();
	  }

    public void flickrAuthenticate() throws ParserConfigurationException, IOException, SAXException, FlickrException {
        f = new Flickr("5b80dfcc5d7235f0b5438aa5ca16d8dd", "ea56d12dbcf95b89", new REST());
        Flickr.debugStream = false;
        requestContext = RequestContext.getRequestContext();
        authInterface = f.getAuthInterface();
        frob = authInterface.getFrob();
        URL url = f.getAuthInterface().buildAuthenticationUrl(Permission.DELETE, frob);
        web_url = url.toString();

    	startestUI();
    	
        JButton authenticate = new JButton("1. Starts authenticate");
        JButton done = new JButton("2. Authentication done");
        ButtonsPanel = new JPanel();
        ButtonsPanel.add(authenticate);
        ButtonsPanel.add(done);
        frameest.add(ButtonsPanel, BorderLayout.PAGE_START);

        authenticate.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	startWebbrowser(web_url);
            }
        });
        
        done.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	try {
					try_auth();						
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
    }

	private void try_auth() throws IOException, SAXException {
	    try 
	    {
	        Auth auth = authInterface.getToken(frob);
	        token = auth.getToken();
	        System.out.println("Authentication success");
	        // This token can be used until the user revokes it.
	        System.out.println("Token: " + auth.getToken());
	        System.out.println("nsid: " + auth.getUser().getId());
	        System.out.println("Realname: " + auth.getUser().getRealName());
	        System.out.println("Username: " + auth.getUser().getUsername());
	        System.out.println("Authenticated");
	    	property = new Properties();
	    	property.setProperty("Token", auth.getToken());
	    	property.store(new FileOutputStream("setup.properties"), null);	
	        //System.out.println("Permission: " + auth.getPermission().getType());
	    	
	    	removeAuthUI();
	    	
	    } catch (FlickrException e) {
	        System.out.println("Authentication failed");
	        e.printStackTrace();
	    }		            
	 }

	private void removeAuthUI() throws IOException, SAXException, FlickrException 
	{
			frameest.dispose();
			frameint.remove(authenticatebutt);
			
			SetIdPanel = new JPanel();
			set_id = new JTextField(20);
			search_set = new JButton ("Search set");
			SetIdPanel.add(set_id);
			SetIdPanel.add(search_set);
			PhotoPanel = new JPanel();
			PhotoPanel.setLayout(new GridLayout(8,8));

			frameint.add(SetIdPanel, BorderLayout.PAGE_START);
			frameint.add(PhotoPanel, BorderLayout.CENTER);
			frameint.setSize(1200, 950);
			frameint.setVisible(true);	
			
			search_set.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                	String setid = set_id.getText();
                	try {
						retrieveUserSetPhotos(setid);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SAXException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (FlickrException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }	
             });           
	}
	
	//test set-id 72157622188533686(51 photos) 72157624217683640(26 photos) 72157612875234265(191 photos)
	private void retrieveUserSetPhotos(String setid) throws IOException, SAXException, FlickrException {
		PhotoList plist = f.getPhotosetsInterface().getPhotos(setid, 60, 1);
	    PhotoPanel.removeAll();
	    PhotoPanel.repaint();
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = plist.iterator(); iterator.hasNext();) 
		{
		    Photo photo = (Photo) iterator.next();
		    @SuppressWarnings("deprecation")
			JLabel photo_label = new JLabel(new ImageIcon(photo.getThumbnailImage()));
		    PhotoPanel.add(photo_label);
		}
		frameint.setVisible(true);	
	}

	public static void main(String[] args) throws ParserConfigurationException 
	{
        try
        {
            AuthExample t = new AuthExample();
            t.initGUI();
        } 
        catch(Exception e) 
        {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
	
	

