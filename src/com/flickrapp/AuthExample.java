package com.flickrapp;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.util.IOUtilities;


public class AuthExample {
	protected Flickr f;
    protected RequestContext requestContext;
    protected String frob;
    protected String token = null;
    protected Properties properties = null;
    protected AuthInterface  authInterface;
    protected Auth auth;
    
    public void authenticate() {
    	
        try {
            f = new Flickr("5b80dfcc5d7235f0b5438aa5ca16d8dd", "ea56d12dbcf95b89", new REST());
            Flickr.debugStream = false;
            requestContext = RequestContext.getRequestContext();
            authInterface = f.getAuthInterface();
            frob           = authInterface.getFrob();
            System.out.println(frob);
			//if (token == null) {
            URL url = f.getAuthInterface().buildAuthenticationUrl(Permission.DELETE, frob);
            /*LargeMessagesDialog dlg = new LargeMessagesDialog("Auth.Info.GetToken.Browse", url.toExternalForm());
            dlg.setVisible(true);*/

            JOptionPane.showMessageDialog(null,"After You close this message, a web browser will be started. Please log-in and permit JPhotoTagger uploading images.");
            Desktop.getDesktop().browse(url.toURI());
            JOptionPane.showMessageDialog(null,"Please now close this dialog only, only after You have confirmed whitin the web bowser, that the app can upload images!");            
            
            try {
                Auth auth = authInterface.getToken(frob);
                token = auth.getToken();
                System.out.println("Authentication success");
                // This token can be used until the user revokes it.
                
                System.out.println("Token: " + auth.getToken());
                System.out.println("nsid: " + auth.getUser().getId());
                System.out.println("Realname: " + auth.getUser().getRealName());
                System.out.println("Username: " + auth.getUser().getUsername());
                //System.out.println("Permission: " + auth.getPermission().getType());
            } catch (FlickrException e) {
                System.out.println("Authentication failed");
                e.printStackTrace();
           
        } } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error while authorizing");
        
        }
    }
    
    public static void main(String[] args) {
        try {
            AuthExample t = new AuthExample();
            t.authenticate();
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
