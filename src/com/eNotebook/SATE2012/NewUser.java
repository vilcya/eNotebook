package com.eNotebook.SATE2012;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewUser extends Activity implements View.OnClickListener{

	// Button view
	Button create;
	
	// Edittext views for inputting name and passwords
	EditText firstname, lastname, password, passwordCon;
	
	// Spinners for inputting team info
	Spinner team, lead;
	
	// Pop up a toast if there is something wrong
    Toast errormessage;
    
    DataPassing dp = new DataPassing();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newuser);
	
		assignObjects();
		
		teamSpinnerAdaptor();
		leadSpinnerAdaptor();
		
		create.setOnClickListener(this);
		
	}
	
	
	private void teamSpinnerAdaptor() {
		// TODO Auto-generated method stub
		
		List<String> teamlist = new ArrayList<String>();
		
		String finalresult = dp.performRequest(null, 
				"http://virtualdiscoverycenter.net/login/PHP/getTeams.php", 
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
		        	
		        	JSONObject jsondata = jarray.getJSONObject(j);
		        	teamlist.add(jsondata.getString("team_name"));
		        }
	        }
    	}	
    	catch(Exception e)
    	{ 
    		Log.w("log_cat", "JSON Parsing error " + e.toString());
    		e.printStackTrace(); 
    	}
		
		ArrayAdapter<String> teamAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teamlist);
		teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		team.setAdapter(teamAdapter);
		
	}

	private void leadSpinnerAdaptor() {
		List<String> leadlist = new ArrayList<String>();
		leadlist.add("No");
		leadlist.add("Yes");
		
		ArrayAdapter<String> leadAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, leadlist);
		leadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		lead.setAdapter(leadAdapter);
	}
	
	
	// Assigns globals
	protected void assignObjects()
	{
		create = (Button) findViewById(R.id.bCreateAcc);
		firstname = (EditText) findViewById(R.id.etCFirstName);
		lastname = (EditText) findViewById(R.id.etCLastName);
		password = (EditText) findViewById(R.id.etCPassword);
		passwordCon = (EditText) findViewById(R.id.etCPasswordCon);
		lead = (Spinner) findViewById(R.id.dbLead);
		team = (Spinner) findViewById(R.id.dbTeam);
		
	}

	public void createAccount()
	{
		String finalresult = "", lname, fname, pwd, pwdcon, teamname, leadstat;
		
    	// Gets the new names
    	lname = lastname.getText().toString();
    	fname = firstname.getText().toString();
    	pwd = password.getText().toString();
    	pwdcon = passwordCon.getText().toString();
    	teamname = team.getSelectedItem().toString();
    	leadstat = lead.getSelectedItem().toString();
  
        // Check that none of the fields are empty
        if (lname.length() == 0 || fname.length() == 0 || pwd.length() == 0 || pwdcon.length() == 0)
        {
        	errormessage = Toast.makeText(getApplicationContext(),
    				"One or more fields are blank. Please provide your information.", 
    				Toast.LENGTH_LONG);
        	errormessage.show();
        	return;
        }
	    
        // Checks the leadership status
        if (leadstat.equals("Yes"))
        	leadstat = "true";
        else
        	leadstat = "false";
        
        // Check that the passwords are the same
        if (!pwd.equals(pwdcon))
        {
        	errormessage = Toast.makeText(getApplicationContext(),
    				"Password and confirm password do not match!", 
    				Toast.LENGTH_LONG);
        	errormessage.show();
        	return;
        }

        // Create parameters for POST request
        ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
        
        parameters.add(new BasicNameValuePair("first_name", fname));
        parameters.add(new BasicNameValuePair("last_name", lname));
        parameters.add(new BasicNameValuePair("password", pwd));
        parameters.add(new BasicNameValuePair("team_name", teamname));
        parameters.add(new BasicNameValuePair("team_lead", leadstat));
        
        // Perform POST request
        finalresult = dp.performRequest(parameters, 
        		"http://virtualdiscoverycenter.net/login/PHP/createAccount.php", 
        		"POST");
        
        // Check for errors given back by PHP script 
        // If the name is already taken
        if(finalresult.contains("Duplicate entry"))
        {
        	errormessage = Toast.makeText(getApplicationContext(), 
        			"Account with name " + fname + " " + lname + " already exists", 
        			Toast.LENGTH_LONG);
        	errormessage.show();
        }
        
        // Other errors server side
        else if (finalresult.contains("false") || finalresult.contains("ERROR"))
        {
        	errormessage = Toast.makeText(getApplicationContext(), 
        			"Create Account failed, please try again.", Toast.LENGTH_LONG);
        	errormessage.show();
        }
                
        // If the account was successfully created
        else
        {
        	errormessage = Toast.makeText(Menu.getContext(), 
        			"Account successfully made. Please log in to continue.", 
        			Toast.LENGTH_LONG);
        	errormessage.show();
        	
	        Intent loginpage = new Intent("com.eNotebook.SATE2012." + "OPTION");
	        startActivity(loginpage);
        }
	}
	
	 public void onClick(View v){ 
		String url = "http://virtualdiscoverycenter.net/login/PHP/createAccount.php";
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
 			createAccount();
	 }
	
}
