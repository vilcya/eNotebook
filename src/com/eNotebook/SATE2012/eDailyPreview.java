/* 
 * eDailyPreview.java
 * Activity that displays the edited eDaily's preview before
 * sending it off via e-mail. Only previews today's eDaily (since
 * previous eDailies can not be edited)
 */


package com.eNotebook.SATE2012;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class eDailyPreview extends Activity implements View.OnClickListener{
	
	// Navigation buttons
	Button menu;
	
	// TextViews to create the eDaily image
	TextView tvdate, tvname, tvacctoday, tvacctom;
	
	// Gives the user descriptions on what is happening
	TextView description;
	
	// Serves as the filename
	String date;
	
	// Extras from previous page
	Bundle extras;
	
    @Override
    /* Upon creation of the activity (after user saves) */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the xml layout
		setContentView(R.layout.edailypreview);
		
		// Assign the views and variables
		assignedObjects();
		
		// Set on click listeners
		menu.setOnClickListener(this);
		
		// Set the introduction
		description.setText("eDaily Preview");
		
		// Create the layout
		setLayout();
	}
    
    /* Function that assigns views and variables */
    private void assignedObjects()
    {
    	// Sets the date to today's date
        tvdate = (TextView) findViewById(R.id.tvDate);
        
        // Find the other views on the eDaily image
        tvname = (TextView) findViewById(R.id.tvName);
        tvacctoday = (TextView) findViewById(R.id.tvAccToday);
        tvacctom = (TextView) findViewById(R.id.tvAccTomorrow);
        
        // Initializes the text description
        description = (TextView) findViewById(R.id.tvPreviewText);
        
        // Bundle of extras
        extras = getIntent().getExtras(); 
        // Get the filename, which is the date
        date = extras.getString("filename");
        tvdate.setText("Today's Date: " + date);
        
        // Set navigation buttons
        menu = (Button) findViewById(R.id.bPreviewMenu);
        
    }
    
    /* Sets the layout for the eDaily image */
    private void setLayout()
    {
    	// Find where the current eDaily text file is
    	if(extras == null)
    		return;
    	
    	File textpath = new File(getFilesDir(), "Text/" + date);
    	File namepath = new File(getFilesDir(), "UserInformation/name");
    	
    	// Error handling for non-existent paths 
    	//  problem with code if this occurs
    	if (!textpath.exists() || !namepath.exists())
    	{
            description.setText("FATAL: I could not find the path!" );
            return;
    	}
    	
    	// Read the file, parse the string, and set the correct views
    	String mystring = readTextfromFile(textpath.toString());
    	String myname = readTextfromFile(namepath.toString());
    	parseText(mystring, myname);
    	
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
                    data += lineofdata + "\n";
            }
            catch(IOException e)
            { e.printStackTrace(); }
            
        }
        catch(FileNotFoundException e)
        { e.printStackTrace(); }
        
        // Return final result
        return data;
    }
    
    /* Parses a given string read from a text file into 
     *  components and sets the correct views
     */
    private void parseText(String textfromfile, String name)
    {
    	// Split the string given the regular expression
    	String[] components = textfromfile.split("\\|\\|\\|");
    	
    	// Make sure that the split has not failed
    	//  problem with code if this happens
    	if (components.length == 0 || components.length > 2)
    	{
    		description.setText("FATAL: Nothing was inputted.");
    		return;
    	}
    	
    	// Set the respective views on the eDaily
    	tvname.setText("Student's Name: " + name);
    	tvacctoday.setText(components[0]);
    	tvacctom.setText(components[1]);
    }
    
    public void onClick(View view)
    {
    	Intent ourintent = new Intent("com.eNotebook.SATE2012." + "MENU");
    	startActivity(ourintent);
    }
    
    
}
