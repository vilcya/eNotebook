package com.eNotebook.SATE2012;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NewUser extends Activity{

	// Button view
	Button create;
	
	// Edittext views for inputting name
	EditText firstname, lastname, password, passwordCon;
	
	// Edittext views for inputting name
	Spinner team, lead;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newuser);
		
		teamSpinnerAdaptor();
		leadSpinnerAdaptor();
		
		//create.setOnClickListener(this);
		
	}
	
	private void teamSpinnerAdaptor() {
		// TODO Auto-generated method stub
		
		team = (Spinner) findViewById(R.id.dbTeam);
		List<String> teamlist = new ArrayList<String>();
		teamlist.add("Holodeck");
		teamlist.add("Avatar");
		teamlist.add("Eagle Eye");
		
		ArrayAdapter<String> teamAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teamlist);
		teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		team.setAdapter(teamAdapter);
		
	}

	private void leadSpinnerAdaptor() {
		// TODO Auto-generated method stub
		
		
		lead = (Spinner) findViewById(R.id.dbLead);
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

	}

	
	// public void onClick(View v){ }
}
