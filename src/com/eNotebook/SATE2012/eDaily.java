/* 
 * eDaily.java
 * Activity for creating a new eDaily
 *  updates the file and updates the edaily
 */


package com.eNotebook.SATE2012;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    
    // Class which contains data passing classes 
    DataPassing dp = new DataPassing();
    
    @Override
    /* Called when the activity begins */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sets the xml layout
        setContentView(R.layout.edaily);
        
        // Assigns views and variables
        assignObjects();
        
        // Check if this is an edit or a new eDaily
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
    private void assignObjects()
    {
    	// Button for saving
        save = (Button) findViewById(R.id.bPreview);
        back = (Button) findViewById(R.id.bDailyBack);
        // Find today's date
        datetoday = dp.getDateToday();
        
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
    	// Goes back to the menu
    	if(view.getId() == R.id.bDailyBack)
    		myIntent = new Intent("com.eNotebook.SATE2012." + "EDAILYMENU");
    	
    	// Saves the eDaily and puts it into the database
    	else{
    		// Checks the state of wifi
    		ConnectivityManager connection = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    		NetworkInfo wifi = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    		
    		String url = "http://virtualdiscoverycenter.net/login/PHP/submitEDaily.php";
    		
    		// If wifi is not strong enough for data transfer, notifies user
    		if(!wifi.isConnected() || !dp.checkConnection(url))
    		{
    			errormessage = Toast.makeText(getApplicationContext(), 
    					"Cannot save, please check your Wifi connection.", Toast.LENGTH_LONG);
    			errormessage.show();
    			return;
    		}
    		else // wifi is fine
    		{
    			// Saves the data in internal and performs POST into database
	    		saveData();
	    		
	    		// Start the preview activity
	            myIntent = new Intent("com.eNotebook.SATE2012." + "EDAILYPREVIEW");
	            myIntent.putExtra("filename", datetoday);
    		}
    	}
    	// Switch intents
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

    
    
}

