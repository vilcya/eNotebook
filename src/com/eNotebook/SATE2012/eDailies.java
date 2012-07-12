package com.eNotebook.SATE2012;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class eDailies extends Activity{

	// Folder with all the edailies
	File edailypath;
	// List of all the edaily files
	File[] edailies;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edaily);
		
		// Get the file path of the edailies
		edailypath = new File(getFilesDir(), "eDailies");
		// If the directory does not exist, make it
		if (!edailypath.exists())
		{
			try { edailypath.createNewFile(); }
			catch(IOException e) 
			{ e.printStackTrace(); }
			
			edailypath.mkdir();
		}
		
		TextView mydummytext = (TextView) findViewById(R.id.tvedaily); 
		
		// Go through each edaily and display them
		edailies = edailypath.listFiles();
		mydummytext.setText("Hello, the edaily path is " + edailypath.toString());
		for (File edaily : edailies)
		{
			mydummytext.setText("Welcome to edailies! We are at path" + edaily.toString());
		}
		
	}
	
}
