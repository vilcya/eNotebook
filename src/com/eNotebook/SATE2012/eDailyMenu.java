/* eDailyMenu.java
 * Displays all of the eDailies using a ListView.
 *  On click, it shows a preview of the respective eDaily. 
 */


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
import android.widget.ListView;

public class eDailyMenu extends Activity implements View.OnClickListener{
    
	// Navigation buttons and the listview
    Button newedaily, backtomenu;
    ListView list;
    
    // List of all the edaily files with their text counterpart
    File[] edailies;
    String[] edailytextpaths;
    String[] edailydates;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.edailymenu);
        
        // Assign globals
        assignedObjects();
        
        // Find the path and the text
        getPath();
        
        // Set on click listeners
        newedaily.setOnClickListener(this);
        backtomenu.setOnClickListener(this);
       
        // Set the adapter for the listview
        ArrayAdapter<String> adapter;
        if (edailytextpaths == null)
        	list.setEmptyView(findViewById(R.id.tvEmptyElement));
        else
        {
        	adapter = new ArrayAdapter<String> 
        			(this, R.layout.simple_list, edailytextpaths);
        	list.setAdapter(adapter);
        }

        
        list.setOnItemClickListener(new OnItemClickListener()
        {
        	public void onItemClick(AdapterView<?> a, View v, int position, long id)
        	{
        		
    			Intent previewIntent = new Intent("com.eNotebook.SATE2012." + "EDAILYPREVIEW");
    			previewIntent.putExtra("filename", edailytextpaths[position]);
    			startActivity(previewIntent);
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
        newedaily = (Button) findViewById(R.id.bAdd);
        backtomenu = (Button) findViewById(R.id.bBack);
        list = (ListView) findViewById(R.id.lvDaily);
    }
    
    public void onClick(View view) {
    	Intent ourIntent;
    	
    	if(view.getId() == R.id.bBack)    
    		ourIntent = new Intent("com.eNotebook.SATE2012." + "MENU");
    	else
    	{
	        ourIntent = new Intent("com.eNotebook.SATE2012." + "EDAILY");
	        ourIntent.putExtra("loadInitialText", false);
    	}
    	
    	startActivity(ourIntent);
    }

    
}

