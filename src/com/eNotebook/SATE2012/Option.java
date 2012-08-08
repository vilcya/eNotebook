/* Option.java
 * Class for changing your name. 
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
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Option extends Activity implements View.OnClickListener {
	
	// Button view
	Button login, logout;
	
	// Edittext views for inputting name
	EditText firstname, lastname, password;
	
	// TextView for showing the currently stored name
	TextView CurName;
	
    // Pop up a toast if there is something wrong
    Toast errormessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		
		assignObjects();
		CurrentName();
		login.setOnClickListener(this);
		logout.setOnClickListener(this);
	}

	// Assigns globals
	protected void assignObjects()
	{
		login = (Button) findViewById(R.id.bLogin);
		logout = (Button) findViewById(R.id.bLogout);
		CurName = (TextView) findViewById(R.id.tvCurrentName);
		firstname = (EditText) findViewById(R.id.etFirstName);
		lastname = (EditText) findViewById(R.id.etLastName);
		password = (EditText) findViewById(R.id.etPassword);
	}
	
	// Sets the current name view if one exists
	public void CurrentName(){
		
		File namepath = new File(getFilesDir(), "UserInformation/name");
		
		if ( !namepath.exists())
    	{
            CurName.setText("You are not logged in!" );
            return;
    	}
		else 
		{
			String myname = readTextfromFile(namepath.toString());
			CurName.setText("Current User: " + myname);
		}
	}

	/* Takes in a file path name and returns the text
     *  read in from that file 
     */
    private String readTextfromFile(String path)
    {
    	File edailyfile = new File(path);	// create the file
        FileInputStream instream;			// input stream to read file
        InputStreamReader instreamread; 	// reader for stream
        BufferedReader buf;					// buffer for reader
        
        String lineofdata = "";				// a line of the file
        String data = "";					// will contain complete string
       
        try 
        {
        	// Begin creating the instream buffer
            instream = new FileInputStream(edailyfile);
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
    
    static public boolean deleteDirectory(File path) {
        if( path.exists() ) {
          File[] files = path.listFiles();
          for(int i=0; i<files.length; i++) {
             if(files[i].isDirectory()) {
               deleteDirectory(files[i]);
             }
             else {
               files[i].delete();
             }
          }
        }
        return( path.delete() );
      }
    
    // On click function
    public void onClick(View v){
		
    	// If back is pressed
    	if(v.getId() == R.id.bLogout)
    	{
    		
    		deleteDirectory(new File(getFilesDir(),"UserInformation"));
    		deleteDirectory(new File(getFilesDir(),"Text"));
    		deleteDirectory(new File(getFilesDir(),"TwoFiftySeven"));
    		
    		Intent backIntent = new Intent("com.eNotebook.SATE2012." + "OPTION");
            startActivity(backIntent);
    	}
    	
    	// If submit is pressed 
    	else
    	{	
    		String finalresult;
		    try
		    {
		    	// Gets the new names
		    	String lname = lastname.getText().toString();
		    	String fname = firstname.getText().toString();
		    	String pwd = password.getText().toString();
		    	
		    	saveName(fname + " " + lname);
		    	
		        // Check that none of the fields are empty
		        if (lname.length() == 0 || fname.length() == 0 || pwd.length() == 0)
		        {
		        	errormessage = Toast.makeText(getApplicationContext(),
	        				"One or more fields are blank. Please provide your information.", 
	        				Toast.LENGTH_LONG);
		        	errormessage.show();
		        	return;
		        }
		        
		        
		        // Access the database and connect through a post
		        ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		        
		        parameters.add(new BasicNameValuePair("firstname", fname));
		        parameters.add(new BasicNameValuePair("lastname", lname));
		        parameters.add(new BasicNameValuePair("password", pwd));
		        
		        HttpClient client = new DefaultHttpClient();
		        HttpPost post = new HttpPost("http://sate.virtualdiscoverycenter.net/login/login.php");
		        post.setEntity(new UrlEncodedFormEntity(parameters));
		        HttpResponse response = client.execute(post);
		        HttpEntity entity = response.getEntity();
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
		        
		        finalresult = sbuilder.toString();
		        
		        
		        // Finds the text directory and creates one if none exists
		        File textpath = new File(getFilesDir(), "Text");
		        if (!textpath.exists())
		            textpath.mkdir();
		        
		        // Parse the JSON data
		        JSONArray jarray = new JSONArray(finalresult);
		        for(int i=0; i<jarray.length(); i++)
		        {
		        	JSONObject jsondata = jarray.getJSONObject(i);
		        	
		        	// Create a new file for the new eDaily
		            File newtext = new File(textpath, jsondata.getString("date"));
		            try 
		            { newtext.createNewFile(); }
		            catch(IOException e) 
		            { e.printStackTrace(); } 
	                
	                // Create the string for going into the file
	                String edailytext = jsondata.getString("name") + "*****" 
	                					+ jsondata.getString("acctoday") + "*****" 
	                					+ jsondata.getString("acctom");

	                // Open the file stream and copy the text into the file
	                FileOutputStream ostream = new FileOutputStream(newtext);
	                ostream.write(edailytext.getBytes());
	                ostream.close();
		        }
		        
		    }
		    catch (Exception e)
		    { 
		    	errormessage.setText("Login failed. Please try again.");
		    	errormessage.show();
		    }
		    
		    
		 // Start the preview activity
	        Intent previewIntent = new Intent("com.eNotebook.SATE2012." + "MENU");
	        startActivity(previewIntent);
		    
    	}
	}
    
    public void saveName(String fullname)
    {
    	try
    	{
    		// Finds the user information directory and creates one if none exists
	    	
			File textpath = new File(getFilesDir(), "UserInformation");
	        if (!textpath.exists())
	            textpath.mkdir();
	        
			// Create a new file for saving the user's name
		    File newtext = new File(textpath, "name");
		    try 
		    { newtext.createNewFile(); }
		    catch(IOException e)
		    { e.printStackTrace(); } 
	        		
	        // Open the file stream and copy the text into the file
	        FileOutputStream ostream = new FileOutputStream(newtext);
	        ostream.write(fullname.getBytes());
	        ostream.close();
    	}
    	catch(Exception e)
    	{ e.printStackTrace(); }
    }
	
}
