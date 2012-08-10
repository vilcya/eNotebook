package com.eNotebook.SATE2012;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
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
		
		String finalresult = "";
		
		try
    	{
	        // Get to other webpage
			HttpClient client = new DefaultHttpClient();
	        HttpGet submitedaily = new HttpGet("http://virtualdiscoverycenter.net/login/PHP/getTeams.php");
	        HttpResponse response2 = client.execute(submitedaily);
	        HttpEntity entity2 = response2.getEntity();

	        InputStream instream2 = entity2.getContent();

	        BufferedReader bufreader2 = new BufferedReader(new InputStreamReader(instream2, "iso-8859-1"), 8);
	        StringBuilder sbuilder2 = new StringBuilder();
	        String line2 = null;
	        while((line2 = bufreader2.readLine()) != null)
	        	sbuilder2.append(line2 + "\n");
	        instream2.close();
	        finalresult = sbuilder2.toString();
    	}
    	catch(Exception e)
    	{ 
    		Log.e("log_cat", "HTTP Connection Error EDaily-Grab " + e.toString());
    		e.printStackTrace(); 
    	}
    	
    	
    	// Finds the text directory and creates one if none exists
        File textpath = new File(getFilesDir(), "Text");
        if (!textpath.exists())
            textpath.mkdir();
        
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
		// TODO Auto-generated method stub
		
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
	    
        if (leadstat.equals("Yes"))
        {
        	leadstat = "true";
        } 
        
        else
        {
        	leadstat = "false";
        }
        
        // Check that the passwords are the same
        if (!pwd.equals(pwdcon))
        {
        	errormessage = Toast.makeText(getApplicationContext(),
    				"Password and confirm password do not match!", 
    				Toast.LENGTH_LONG);
        	errormessage.show();
        	return;
        }
             
        HttpClient client = new DefaultHttpClient();

        try
	    {
	        // Access the database and connect through a post
	        ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
	        
	        parameters.add(new BasicNameValuePair("first_name", fname));
	        parameters.add(new BasicNameValuePair("last_name", lname));
	        parameters.add(new BasicNameValuePair("password", pwd));
	        parameters.add(new BasicNameValuePair("team_name", teamname));
	        parameters.add(new BasicNameValuePair("team_lead", leadstat));
	        
	        client.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, true);
	        HttpPost post = new HttpPost("virtualdiscoverycenter.net/login/PHP/createAccount.php");
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
	    }
        catch(Exception e)
        { 
        	Log.e("log_cat", "HTTP Connection Error Login " + e.toString());
        	e.printStackTrace(); 
        }
        
        if (finalresult.contains("false"))
        {
        	errormessage = Toast.makeText(getApplicationContext(), "Create Account failed, please try again.", Toast.LENGTH_LONG);
        	errormessage.show();
        }
        
        errormessage = Toast.makeText(getApplicationContext(),
				finalresult, 
				Toast.LENGTH_LONG);
    	errormessage.show();
        
    	
	}
	
	 public void onClick(View v){ 
	 
		 createAccount();
	 }
	
}
