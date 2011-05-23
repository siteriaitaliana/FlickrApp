package com.flickapp;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.Permission;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Authorization {
	
    protected static   RequestContext requestContext;
    protected static   String         frob;
    protected static   AuthInterface  authInterface;
    protected static   boolean        authenticated = false;
    protected static   Auth           auth;
    protected static   String 		  token = null;
    protected static   URL			  url;

    
    public static URL getAuthenticateURL() throws Exception {
    	   try {
            Flickr flickr = new Flickr("5b80dfcc5d7235f0b5438aa5ca16d8dd", "ea56d12dbcf95b89", new REST());
            Flickr.debugStream = false;
            requestContext = RequestContext.getRequestContext();
            authInterface  = flickr.getAuthInterface();
			frob           = authInterface.getFrob();
        	url = authInterface.buildAuthenticationUrl(Permission.DELETE, frob);
            } catch (Exception e) {
				e.printStackTrace();
			}
		return url;   
    }
    
    public static void authenticate(URL url) throws Exception{
    JOptionPane.showMessageDialog(null, "Auth.Info.SendToken");
 
    
    JOptionPane.showMessageDialog(null, "Auth.Info.GetToken.Confirm");
    
    auth = authInterface.getToken(frob);
    token = auth.getToken();
    auth = new Auth();
    auth.setToken(token);
    requestContext.setAuth(auth);
    }
  
}