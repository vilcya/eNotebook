package com.eNotebook.SATE2012;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ListView;

public class eDailyMenu extends Activity implements View.OnClickListener{
    
	
    Button newedaily, backtomenu;
    ListView list;
    
    // List of all the edaily files with their text counterpart
    File[] edailies;
    String[] edailytextpaths;
    String[] edailydates;

    // Default list that shows up 
    String[] tmp = {"Sorry, there are currently no eDailies!"};
    
    // Gallery for horizontal view
    Gallery edailygall;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.edailymenu);
        assignedObjects();
        getPath();
        newedaily.setOnClickListener(this);
        backtomenu.setOnClickListener(this);
        
        ArrayAdapter<String> adapter;
        if (edailytextpaths == null)
        {
        	 adapter = new ArrayAdapter<String> 
        			(this, android.R.layout.simple_list_item_1, tmp);
        }
        else
        {
        	adapter = new ArrayAdapter<String> 
        			(this, android.R.layout.simple_list_item_1, edailytextpaths);
        }
        list.setAdapter(adapter);
        
        list.setOnItemClickListener(new OnItemClickListener()
        {
        	public void onItemClick(AdapterView<?> a, View v, int position, long id)
        	{
        		Intent ourIntent = new Intent("com.eNotebook.SATE2012." + "EDAILYPREVIEW");
        		ourIntent.putExtra("filename", edailytextpaths[position]);
        		startActivity(ourIntent);
        	}
        });
    }
    
    private void getPath() {

        // Get the file path of the edailies
        File edailytextpath = new File(getFilesDir(), "Text");
        
        // Check if directory exists
        if(!edailytextpath.exists())
            return;
        else
            edailytextpaths = edailytextpath.list();
    }
    
    private void assignedObjects() {
        newedaily = (Button) findViewById(R.id.sAdd);
        backtomenu = (Button) findViewById(R.id.bBack);
        list = (ListView) findViewById(R.id.lvDaily);
    }
    
    public void onClick(View view) {
    	Intent ourIntent;
    	
    	if(view.getId() == R.id.bBack)    
    		ourIntent = new Intent("com.eNotebook.SATE2012." + "MENU");
    	else
	        ourIntent = new Intent("com.eNotebook.SATE2012." + "EDAILY");
    	
    	startActivity(ourIntent);
    }
    
    
}

