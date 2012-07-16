package com.eNotebook.SATE2012;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
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
				RelativeLayout rl = (RelativeLayout) findViewById(R.id.rledailymenu);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
				// Go through each edaily and display them
				for (File edaily: edailies)
				{
					// Create a new image view
					ImageView imageback = new ImageView(this); 
					mydummytext.setText("" + edaily.toString());
					
					// Find the edaily and put it on the image view
					Bitmap bmedaily = BitmapFactory.decodeFile(edaily.getAbsolutePath());
					bmedaily = Bitmap.createScaledBitmap(bmedaily, 200, 200, true);
					bmedaily.setDensity(metrics.densityDpi);
					imageback.setImageBitmap(bmedaily);
					
					// Add it into the relative layout
					lp.addRule(RelativeLayout.BELOW, R.id.tvedaily);
					lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					rl.addView(imageback, lp);
					
				}
			}
			else
				mydummytext.setText("edailies is not a directory - your code should not be doing this");
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
