package com.eNotebook.SATE2012;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Option extends Activity implements View.OnClickListener {
	
	// Button view
	Button done;
	
	// Edittext views for inputting name
	EditText firstname;
	EditText lastname;
	
    // Pop up a toast if there is something wrong
    Toast errormessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		
		assignObjects();
		done.setOnClickListener(this);
	}

	public void onClick(View v){
		
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
	
	protected void assignObjects()
	{
		done = (Button) findViewById(R.id.bdoneButton);
		
		firstname = (EditText) findViewById(R.id.etNameFirst);
		lastname = (EditText) findViewById(R.id.etNameLast);
	}
	
}
