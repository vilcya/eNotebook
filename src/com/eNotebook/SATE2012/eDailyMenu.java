package com.eNotebook.SATE2012;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class eDailyMenu extends Activity implements View.OnClickListener{
	
	Button editeDaily;
	TextView mydummytext;
	
	// Folder with all the edailies
	File edailypath;
	// List of all the edaily files
	File[] edailies;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edailymenu);
		
		assignedObjects();
		
		editeDaily.setOnClickListener(this);
		
		// Get the file path of the edailies
		edailypath = new File(getFilesDir(), "eDailies");
		
		// If the directory does not exist, make it
		if(!edailypath.exists())
			mydummytext.setText("Sorry, you have no eDailies. I'm at " + edailypath.toString());
		else
		{
			edailies = edailypath.listFiles();
			if(edailies!= null)
			{
				// Go through each edaily and display them
				for (File edaily: edailies)
					mydummytext.setText("" + edaily.toString());	
			}
		}
	}
	
	private void assignedObjects() {
		
		mydummytext = (TextView) findViewById(R.id.tvedaily);
		editeDaily = (Button) findViewById(R.id.ibAdd);
    }
	
	public void onClick(View view) {
    	
        if (view.getId() == R.id.ibAdd){
        	Intent ourIntent = new Intent("com.eNotebook.SATE2012." + "EDAILY");
        	startActivity(ourIntent);
        }
        
    }
}
