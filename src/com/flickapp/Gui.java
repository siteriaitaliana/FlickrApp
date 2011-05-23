package com.flickapp;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class Gui {
	
	protected static JFrame  frame;
	protected static JPanel  panel_init;
	protected static JPanel  webBrowserPanel;
	protected static JButton button_auth;
	protected static URL	  url;
	
	public static void main(String[] args) {  
	    UIUtils.setPreferredLookAndFeel();  
	    NativeInterface.open();  
	    SwingUtilities.invokeLater(new Runnable() {  
	      public void run() {  
	    	  frame = new JFrame();
		  		panel_init = new JPanel(new BorderLayout());
		  		button_auth = new JButton("Authenticate");
		  		panel_init.add(button_auth, BorderLayout.CENTER);
		  		
			  	    button_auth.addMouseListener(new MouseAdapter() {
			              public void mouseClicked(MouseEvent e) 
			              {
			              	frame.remove(panel_init);
							try {
								url = new URL(null);
								url = Authorization.getAuthenticateURL();
								startBrowser(url); 
							} catch (Exception ex) 
							{
								ex.printStackTrace();
							}
			              }
			      	});

		  		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  		frame.setSize(800, 600);
		  	    frame.add(panel_init);
		  	    frame.setLocationByPlatform(true);  
		  	    frame.setVisible(true);
	      }  
	    });  
	    NativeInterface.runEventPump();  
	 }  

	
	public static void startBrowser(URL url)
	{	
	    webBrowserPanel = new JPanel(new BorderLayout());  
	    webBrowserPanel.setBorder(BorderFactory.createTitledBorder("Native Web Browser component"));  
	    final JWebBrowser webBrowser = new JWebBrowser();  
	    webBrowser.navigate("http://www.google.com");  
	    webBrowserPanel.add(webBrowser, BorderLayout.CENTER);  
	    frame.add(webBrowserPanel, BorderLayout.CENTER);  
		
	}

}
  
