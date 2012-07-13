package com.eNotebook.SATE2012;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class eDaily extends Activity implements View.OnClickListener{

	Button save;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edailyedit);
		
		assignedObjects();
		
		save.setOnClickListener(this);
	}
	
	
	private void assignedObjects()
	{
		save = (Button) findViewById(R.id.bSave);
	}
	
	public void onClick(View view)
	{
		// Create a bitmap out of the view
		Bitmap neweDaily = findViewById(R.id.leDaily).getDrawingCache();
		
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
		
		// Open the file stream and copy the image into the file
		try
		{
			TextView dummytxt = (TextView) findViewById(R.id.tvDisplayPath);
			dummytxt.setText("I'm saving the picture at " + newedaily.toString());
			FileOutputStream ostream = new FileOutputStream(newedaily);
			neweDaily.compress(CompressFormat.PNG, 100, (OutputStream)ostream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
}
