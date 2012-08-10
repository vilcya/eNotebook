/* Option.java
 * Logs a person in, downloads all of the files from the database
 * and saves them into internal storage as a "cache" - Deleted upon
 * logout.
 */

package com.eNotebook.SATE2012;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Option extends Activity implements View.OnClickListener {
	
	// Button view
	Button login, logout, create;
	
	// Edittext views for inputting name
	EditText firstname, lastname, password;
	
	// TextView for showing the currently stored name
	TextView CurName;
	
    // Pop up a toast if there is something wrong
    Toast errormessage;
	
    DataPassing dp = new DataPassing();
    
    private static Option instance;
    
    public Option ()
    { instance = this;}
    
    public static Context getContext()
    {
    	return instance;
    }
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);

		// Assign globals and variables
		assignObjects();
		// See if the person is logged in
		checkLogin();
		
		// Set on click listeners
		login.setOnClickListener(this);
		logout.setOnClickListener(this);
		create.setOnClickListener(this);
	}

	// Assigns globals
	protected void assignObjects()
	{
		// Buttons for navigation
		login = (Button) findViewById(R.id.bLogin);
		logout = (Button) findViewById(R.id.bLogout);
		create = (Button) findViewById(R.id.bCreate);
		
		// Displays current user
		CurName = (TextView) findViewById(R.id.tvCurrentName);
		
		// Views for input
		firstname = (EditText) findViewById(R.id.etFirstName);
		lastname = (EditText) findViewById(R.id.etLastName);
		password = (EditText) findViewById(R.id.etPassword);
	}
	
	// Checks if the person is logged in or not
	public void checkLogin(){
		
		File namepath = new File(getFilesDir(), "UserInformation/name");
		
		// If the user is not logged in
		if ( !namepath.exists())
    	{
            CurName.setText("You are not logged in!" );
            logout.setVisibility(Button.GONE);
            return;
    	}
		// If user is logged in
		else 
		{
			String myname = dp.readTextfromFile(namepath.toString());
			login.setVisibility(Button.GONE);
			firstname.setVisibility(EditText.GONE);
			lastname.setVisibility(EditText.GONE);
			password.setVisibility(EditText.GONE);
			create.setVisibility(Button.GONE);
			CurName.setText("Current User: " + myname);
		}
	}
    
    
    
    // On click function
    public void onClick(View v){
		
    	// If back is pressed
    	if(v.getId() == R.id.bLogout)
    	{
    		dp.deleteFiles(new File(getFilesDir(),"UserInformation"));
    		dp.deleteFiles(new File(getFilesDir(),"Text"));
    		dp.deleteFiles(new File(getFilesDir(),"TwoFiftySeven"));
    		
    		Intent backIntent = new Intent("com.eNotebook.SATE2012." + "OPTION");
            startActivity(backIntent);
    	}
    	
    	else if(v.getId() == R.id.bCreate)
    	{
    		Intent backIntent = new Intent("com.eNotebook.SATE2012." + "NEWUSER");
            startActivity(backIntent);
    	}
    		
    	// If submit is pressed 
    	else
    	{
    		String url = "http://virtualdiscoverycenter.net/login/PHP/login.php";
    		ConnectivityManager connection = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    		NetworkInfo wifi = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    		
    		if(!wifi.isConnected() || !dp.checkConnection(url))
    		{
    			errormessage = Toast.makeText(getApplicationContext(), 
    					"Cannot login, please check your Wifi connection.", Toast.LENGTH_LONG);
    			errormessage.show();
    			return;
    		}
    		else
    			serverInteraction();
    	}
	}
    
    
    public void serverInteraction()
    {
    	// Gets the new names
    	String lname = lastname.getText().toString();
    	String fname = firstname.getText().toString();
    	String pwd = password.getText().toString();
    	String fullname = fname + " " + lname;
    	
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
        
        parameters.add(new BasicNameValuePair("first_name", fname));
        parameters.add(new BasicNameValuePair("last_name", lname));
        parameters.add(new BasicNameValuePair("password", pwd));
                
        String finalresult = dp.performRequest(parameters, 
        			"http://virtualdiscoverycenter.net/login/PHP/login.php",
        			"POST");
        
        if (finalresult.contains("false"))
        {
        	errormessage = Toast.makeText(getApplicationContext(), "Login failed, please try again.", Toast.LENGTH_LONG);
        	errormessage.show();
        }
	 
        else
        {
        	// Save the username in internal storage
        	
        	saveName(fullname);
        	// Finds the text directory and creates one if none exists
	        File textpath = new File(getFilesDir(), "Text");
	        if (!textpath.exists())
	            textpath.mkdir();
	        
        	dp.downloadEDaily(textpath, fullname);
        	errormessage = Toast.makeText(Menu.getContext(), "Login succesful!", Toast.LENGTH_LONG);
        	errormessage.show();
        }
        
        // Start the preview activity
        Intent previewIntent = new Intent("com.eNotebook.SATE2012." + "MENU");
        startActivity(previewIntent);
    }

    
    public void saveName(String fullname)
    {
    		
		File textpath = new File(getFilesDir(), "UserInformation");
        if (!textpath.exists())
            textpath.mkdir();
        
        File newpath = new File(textpath, "name");
        
        dp.saveTexttoFile(fullname, newpath);
    }
	
}
