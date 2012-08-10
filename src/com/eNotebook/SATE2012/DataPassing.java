package com.eNotebook.SATE2012;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;

import android.util.Log;

public class DataPassing{

	public DataPassing()
	{}
	
	
	/* HTTP DATA PASSING */
	
	public boolean checkConnection(String urlstr)
	{
		Boolean reachable = false;
		try {
            URL url = new URL(urlstr);
            HttpURLConnection urlc = (HttpURLConnection) url
                             .openConnection();
            urlc.setRequestProperty("User-Agent", "Android Application");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(30 * 1000); // Thirty seconds timeout in ms
                                                                              
            urlc.connect();
            if (urlc.getResponseCode() == 200) { // Good response
                     reachable = true;
            } else { // Anything else is unwanted
                     reachable = false;
            }
	    } catch (IOException e) {
	            Log.e("DEBUG", e.getMessage());
	            reachable = false;
	    }
	    
	    return reachable;
	}
	
	
	
	/* INTERNAL STORAGE FUNCTIONS*/
	
	
	
	/* Takes in a file path name and returns the text
     *  read in from that file 
     */
    public String readTextfromFile(String path)
    {
    	File file = new File(path);	// create the file
        FileInputStream instream;			// input stream to read file
        InputStreamReader instreamread; 	// reader for stream
        BufferedReader buf;					// buffer for reader
        
        String lineofdata = "";				// a line of the file
        String data = "";					// will contain complete string
       
        try 
        {
        	// Begin creating the instream buffer
            instream = new FileInputStream(file);
            instreamread = new InputStreamReader (instream);
            buf = new BufferedReader(instreamread);
            
            try
            {
            	// Read the line until end of file and add to data
                while((lineofdata = buf.readLine()) != null)
                    data += lineofdata;
            }
            catch(IOException e)
            { e.printStackTrace(); }
            
        }
        catch(FileNotFoundException e)
        { e.printStackTrace(); }
        
        // Return final result
        return data;
    }
    
    
    /* Saves the text to specified filepath */
    public void saveTexttoFile(String text, File filepath)
    {
		// Create a new file for saving the user's name
	    try 
	    { filepath.createNewFile(); }
	    catch(IOException e)
	    { e.printStackTrace(); } 
	        	
	    try 
	    {
	        // Open the file stream and copy the text into the file
	        FileOutputStream ostream = new FileOutputStream(filepath);
	        ostream.write(text.getBytes());
	        ostream.close();
    	}
    	catch(Exception e)
    	{ e.printStackTrace(); }
    }
	
    /* Deletes all user information - occurs when logged out */
    public boolean deleteUserInfo(File path) 
    {
        if( path.exists() ) {
          File[] files = path.listFiles();
          for(int i=0; i<files.length; i++) {
             if(files[i].isDirectory()) {
            	 deleteUserInfo(files[i]);
             }
             else {
               files[i].delete();
             }
          }
        }
        return( path.delete() );
    }
    
    /* Returns null on error */
    public String performRequest(ArrayList<NameValuePair> parameters, String url, String postOrGet)
    {    	
    	HttpClient client = HttpClientFactory.getThreadSafeClient();
        client.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, true);
        HttpPost post = new HttpPost(url);
	        
        HttpEntity entity = null;
	    
        try
	    {
        	if (postOrGet.equals("POST"))
        	{
		        post.setEntity(new UrlEncodedFormEntity(parameters));
		        HttpResponse response = client.execute(post);
		        entity = response.getEntity();
        	}
        	else // "GET"
        	{
        		HttpGet get = new HttpGet(url);
    	        HttpResponse response = client.execute(get);
    	        entity = response.getEntity();
        	}
	    }
	    catch(Exception e)
	    {
	    	Log.e("log_cat", "HTTP POST ERROR " + e.toString());
	    	e.printStackTrace(); 
	    	return null;
	    }
	    
	    try
	    {
	    	InputStream instream = entity.getContent();
	        // Convert buffer to string
	        BufferedReader bufreader = new BufferedReader(new InputStreamReader(instream, "iso-8859-1"), 8);
	        StringBuilder sbuilder = new StringBuilder();
	        String line = null;
	        while((line = bufreader.readLine()) != null)
	        {
	        	sbuilder.append(line + "\n");
	        }
	        instream.close();
	
	        return sbuilder.toString();
	    }
	    catch(Exception e)
	    {
	    	Log.e("log_cat", "HTTP Response ERROR " + e.toString());
	    	e.printStackTrace(); 
	    	return null;
	    }
	       
    }
    	
}
