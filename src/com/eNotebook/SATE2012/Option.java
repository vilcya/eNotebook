package com.eNotebook.SATE2012;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
	Button done, back;
	
	// Edittext views for inputting name
	EditText firstname;
	EditText lastname;
	
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
		done.setOnClickListener(this);
		back.setOnClickListener(this);
		
	}

	public void CurrentName(){
		
		File namepath = new File(getFilesDir(), "UserInformation/name");
		
		if ( !namepath.exists())
    	{
            CurName.setText("No Name Set Yet!" );
            return;
    	}
		else 
		{
			String myname = readTextfromFile(namepath.toString());
			CurName.setText("Current Name: " + myname);
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
    public void onClick(View v){
		
    	if(v.getId() == R.id.bOptionBack)
    	{
    		Intent backIntent = new Intent("com.eNotebook.SATE2012." + "MENU");
            startActivity(backIntent);
    	}
    	
    	else
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
	
	    try
	    {
	    	String fname = firstname.getText().toString();
	    	String lname = lastname.getText().toString();
	        // Check that none of the fields are empty
	        if (fname.length() == 0 || lname.length() == 0)
	        {
	        	errormessage = Toast.makeText(getApplicationContext(),
        				"One or more fields are blank. If you do not have a first or last name, please make one up.", 
        				Toast.LENGTH_LONG);
	        	errormessage.show();
	        	return;
	        }
	        
	        // Create the string for going into the file
	        String fullname = fname + " " + lname;
	
	        // Open the file stream and copy the text into the file
	        FileOutputStream ostream = new FileOutputStream(newtext);
	        ostream.write(fullname.getBytes());
	        ostream.close();
	        
	        // Start the preview activity
	        Intent previewIntent = new Intent("com.eNotebook.SATE2012." + "MENU");
	        startActivity(previewIntent);
	    }
	    catch (Exception e)
	    { e.printStackTrace(); }
	    
    	}
	}
	
	protected void assignObjects()
	{
		done = (Button) findViewById(R.id.bdoneButton);
		back = (Button) findViewById(R.id.bOptionBack);
		CurName = (TextView) findViewById(R.id.tvCurrentName);
		firstname = (EditText) findViewById(R.id.etNameFirst);
		lastname = (EditText) findViewById(R.id.etNameLast);
	}
	
}
