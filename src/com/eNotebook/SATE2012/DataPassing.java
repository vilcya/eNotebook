/* DataPassing.java 
 * Class which contains useful functions for data passing
 * and HTTP connections.
 */

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class DataPassing{

	// Dummy Constructor to initialize new instance
	public DataPassing()
	{}
	
	/* HTTP DATA PASSING */	
	
	/* Pings the urlstr and checks if wifi is strong enough for
	 *  passing data.*/ 
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
	
    /* Deletes directory/file and sub directories
     *  as specified by path */
    public boolean deleteFiles(File path) 
    {
        if( path.exists() ) {
          File[] files = path.listFiles();
          for(int i=0; i<files.length; i++) {
             if(files[i].isDirectory()) {
            	 deleteFiles(files[i]);
             }
             else {
               files[i].delete();
             }
          }
        }
        return( path.delete() );
    }
    
    /* Performs an HTTP request
     *  parameters: specifies variables for POST request 
     *  url: specifies url involved in the request 
     *  postOrGet: "POST" or "GET" - specifies which http request is occuring
     *  
     *  returns null on error
     */
    public String performRequest(ArrayList<NameValuePair> parameters, String url, String postOrGet)
    {    	
    	// Initializes client from the factory 
    	HttpClient client = HttpClientFactory.getThreadSafeClient();
        client.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, true);
        HttpEntity entity = null;
	    
        try
	    {
        	// If we want a POST request
        	if (postOrGet.equals("POST"))
        	{
        		HttpPost post = new HttpPost(url);
		        post.setEntity(new UrlEncodedFormEntity(parameters));
		        HttpResponse response = client.execute(post);
		        entity = response.getEntity();
        	}
        	else // GET request
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
	    
	    // Read in the response from the script
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
	
	        // Return in string form
	        return sbuilder.toString();
	    }
	    catch(Exception e)
	    {
	    	Log.e("log_cat", "HTTP Response ERROR " + e.toString());
	    	e.printStackTrace(); 
	    	return null;
	    }
	       
    }
    	
    
    /* Return today's date in string format MM.dd.yyyy */
    public String getDateToday()
    {
    	// Create the format and calendar instance
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//"MMMMMMMMM dd, yyyy");
    	Calendar cal = Calendar.getInstance();
    	
    	// Set the format and return
    	Date today = cal.getTime();
    	return sdf.format(today);
    }
    
    
    /* Downloads all of the eDailies from the server
     *  textpath: specifies the file/directory we want to delete
     *  fullname: specifies the person's name  
     */ 
    public void downloadEDaily(File textpath, String fullname)
    {
    	// Perform GET request to grab all of the user's eDaily (they should already be logged in)
        String finalresult = performRequest(null, 
	        		"http://virtualdiscoverycenter.net/login/PHP/getEDaily.php", 
	        		"GET");
    	try
    	{
	        // Parse the JSON data
	        JSONArray fulljarray = new JSONArray(finalresult);
	        
	        for (int i = 0; i < fulljarray.length(); i++)
	        {
	        	JSONArray jarray = fulljarray.getJSONArray(i);
	        	
		        for(int j=0; j<jarray.length(); j++)
		        {
		        	JSONObject jsondata = jarray.getJSONObject(i);
		        	
		        	// Create a new file for the new eDaily
		            File newtext = new File(textpath, jsondata.getString("Date"));
		            try 
		            { newtext.createNewFile(); }
		            catch(IOException e) 
		            { e.printStackTrace(); } 
	                
	                // Create the string for going into the file
	                String edailytext = fullname + "*****" 
	                					+ jsondata.getString("Today") + "*****" 
	                					+ jsondata.getString("Tomorrow");

	                // Open the file stream and copy the text into the file
	                FileOutputStream ostream = new FileOutputStream(newtext);
	                ostream.write(edailytext.getBytes());
	                ostream.close();
		        }
	        }
	    }
	    catch (Exception e)
	    { 
	    	e.printStackTrace();
	    }
    }
    
}
