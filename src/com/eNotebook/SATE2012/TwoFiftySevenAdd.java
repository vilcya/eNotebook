package com.eNotebook.SATE2012;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TwoFiftySevenAdd extends Activity implements View.OnClickListener{
    
    // Navigation buttons
    Button add;
    Button back;
    EditText link;
    Toast errormessage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set the layout and assign views
        setContentView(R.layout.twofiftysevenadd);
        assignedObjects();
        
        // Set the onclick listeners
        add.setOnClickListener(this);
        back.setOnClickListener(this);
    } 

    /* Assign layout objects to respective variables  */
    private void assignedObjects() {
        
        // Navigation
        add = (Button) findViewById(R.id.bAddLink257);
        back = (Button) findViewById(R.id.bBack257);
        link = (EditText) findViewById(R.id.etAddLink);
    }
    
    
    /* Triggered when user clicks a view */
    public void onClick(View view) {
    	// Checks which button was clicked
        
        if(view.getId() == R.id.bAddLink257) 
        {
 
	        try
	        {
	            // Find url text
	            String newid = link.getText().toString();
	            
	            // Check for blank url
	            if (newid.length() == 0)
	            {
	            	errormessage = Toast.makeText(getApplicationContext(),
	            				"Please enter a valid video ID.", 
	            				Toast.LENGTH_LONG);
	            	errormessage.show();
	            	return;
	            }
	            
	            // Finds the text directory and creates one if none exists
		        File urlpath = new File(getFilesDir(), "TwoFiftySeven");
		        if (!urlpath.exists())
		        	urlpath.mkdir();
	        	// Create a new file for the new eDaily
	            File newtext = new File(urlpath, newid);
	            try 
	            { newtext.createNewFile(); }
	            catch(IOException e) 
	            { e.printStackTrace(); }
	            
	            Intent ourIntent = new Intent("com.eNotebook.SATE2012." + "TWOFIFTYSEVEN");
		        ourIntent.putExtra("videoID", newid);
	            startActivity(ourIntent);
	        }
	        catch (Exception e)
	        { e.printStackTrace(); }
	    
        } else {
        	Intent ourIntent = new Intent("com.eNotebook.SATE2012." + "TWOFIFTYSEVENMENU");
            startActivity(ourIntent);
        }

        
        
    }
}