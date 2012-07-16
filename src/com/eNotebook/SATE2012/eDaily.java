package com.eNotebook.SATE2012;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class eDaily extends Activity implements View.OnClickListener{

	Button save;
	Button back;
	TextView text;
	RelativeLayout layoutedaily;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edailyedit);
		
		assignedObjects();
	
		save.setOnClickListener(this);
		back.setOnClickListener(this);
	}
	
	private void assignedObjects()
	{
		save = (Button) findViewById(R.id.bSave);
		back = (Button) findViewById(R.id.bEDailyBack);
		text = (TextView) findViewById(R.id.tvDisplayPath);
	}

	
	
	public void onClick(View view)
	{
		if (view.getId() == R.id.bEDailyBack){
        	Intent ourIntent = new Intent("com.eNotebook.SATE2012." + "EDAILYMENU");
        	startActivity(ourIntent);
        }
		
		else{
			
			// Create a bitmap out of the view
			layoutedaily = (RelativeLayout) findViewById(R.id.leDaily);
			layoutedaily.setDrawingCacheEnabled(true);
			Bitmap bmpedaily = layoutedaily.getDrawingCache();
			
			/* Find the path to save and create a directory if one
			   does not exist */
			File edailypath = new File(getFilesDir(), "eDailies");
			if (!edailypath.exists())
				edailypath.mkdir();

			// Create a new file for the new eDaily
			File newedaily = new File(edailypath, "test1.png");
			try { newedaily.createNewFile(); }
			catch(IOException e) 
			{ e.printStackTrace(); } 
			 
			TextView dummytxt = (TextView) findViewById(R.id.tvDisplayPath);
			
			// Open the file stream and copy the image into the file
			try
			{
				dummytxt.setText("I'm saving the picture at " + newedaily.toString());
				FileOutputStream ostream = new FileOutputStream(newedaily);
				bmpedaily.compress(CompressFormat.PNG, 100, (OutputStream)ostream);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			

		}
	
	}
	
	
}
