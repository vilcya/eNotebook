/* 
 * eDaily.java
 * Activity for creating a new eDaily
 */


package com.eNotebook.SATE2012;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class eDaily extends Activity implements View.OnClickListener{

	// Buttons for saving 
    Button save, back;
    
    // Text views inside the template
    EditText accomplishedtoday, accomplishedtomorrow;
    
    // Contains today's date in the format MM.dd.yyyy
    String datetoday;
    
    // Pop up a toast if there is something wrong
    Toast errormessage;

    // Get bundle of extras
    Bundle extras;
    
    DataPassing dp = new DataPassing();
    
    @Override
    /* Called when the activity begins */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sets the xml layout
        setContentView(R.layout.edaily);
        
        // Assigns views and variables
        assignedObjects();
        
        if(extras.getBoolean("loadInitialText"))
        {
        	accomplishedtoday.setText(extras.getString("acctoday"));
        	accomplishedtomorrow.setText(extras.getString("acctomorrow"));
        }
    
        // Set the on click listener
        save.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    
    /* Function that assigns views and variables */
    private void assignedObjects()
    {
    	// Button for saving
        save = (Button) findViewById(R.id.bPreview);
        back = (Button) findViewById(R.id.bDailyBack);
        // Find today's date
        datetoday = getDateToday();
        
        // Views for eDaily template
        accomplishedtoday = (EditText) findViewById(R.id.etToday);
        accomplishedtomorrow = (EditText) findViewById(R.id.etTmrw);
        
        // Get bundle
        extras = getIntent().getExtras();
    }

    /* Event triggered on a button click */
    public void onClick(View view)
    {
    	Intent myIntent;
    	if(view.getId() == R.id.bDailyBack)
    		myIntent = new Intent("com.eNotebook.SATE2012." + "EDAILYMENU");
    	
    	else{
    		saveData();
    		
    		// Start the preview activity
            myIntent = new Intent("com.eNotebook.SATE2012." + "EDAILYPREVIEW");
            myIntent.putExtra("filename", datetoday);
    	}
    	startActivity(myIntent);
    }
    
    
    /* Function that adds the file into internal storage
     * We do not update from the database because this will
     * take significantly longer depending on internet 
     * connections and amount of data passed back */
    public void saveData()
    {
    	// Check if the calendar returned correctly
        if (datetoday.length() == 0)
        	return;

        try
        {
            // Find all of the text from the views
            String myacctoday = accomplishedtoday.getText().toString();
            String myacctom = accomplishedtomorrow.getText().toString();
            String edailytext = getName() + "*****" + myacctoday + "*****" + myacctom;
            
            // Check that none of the fields are empty
            if (myacctoday.length() == 0 || myacctom.length() == 0)
            {
            	errormessage = Toast.makeText(getApplicationContext(),
            				"Please fill in all the blanks or Dr. Williams will hunt you down!!! (with three exclamation marks)", 
            				Toast.LENGTH_LONG);
            	errormessage.show();
            	return;
            }
            
            // Finds the text directory and creates one if none exists
	        File textpath = new File(getFilesDir(), "Text");
	        if (!textpath.exists())
	            textpath.mkdir();
        	// Create a new file for the new eDaily
            File newtext = new File(textpath, datetoday);
            
            // Create the string for going into the file
            dp.saveTexttoFile(edailytext, newtext);
            
            
            // Send to database
            errormessage = Toast.makeText(getApplicationContext(), postData(myacctoday, myacctom), Toast.LENGTH_LONG);
            errormessage.show();
                       
        }
        catch (Exception e)
        { 
        	Log.e("log_cat", "Saving ERROR " + e.toString());
        	e.printStackTrace(); 
        }
    }
    
    /* Posts the data into the PHP script - stores it in database */
    public String postData(String today, String tomorrow)
    {
    	ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
    	parameters.add(new BasicNameValuePair("today", today));
        parameters.add(new BasicNameValuePair("tomorrow", tomorrow));

        return dp.performRequest(parameters, 
        		"http://virtualdiscoverycenter.net/login/PHP/submitEDaily.php",
        		"POST");
    }
    
    /* Retrieves user's name */
    public String getName()
    {
    	File namepath = new File(getFilesDir(), "UserInformation/name");
    	
    	// Error handling for non-existent paths 
    	//  problem with code if this occurs
    	if (!namepath.exists())
    	{
    		errormessage = Toast.makeText(getApplicationContext(),
    				"FATAL: I could not find the path", 
    				Toast.LENGTH_LONG);
    		errormessage.show();
            return null;
    	}
    	
    	// Returns text from the file
    	return dp.readTextfromFile(namepath.toString());
    }
    

    
    
    /* Return today's date in string format MM.dd.yyyy */
    private String getDateToday()
    {
    	// Create the format and calendar instance
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar cal = Calendar.getInstance();
    	
    	// Set the format and return
    	Date today = cal.getTime();
    	return sdf.format(today);
    }
    
    
}

