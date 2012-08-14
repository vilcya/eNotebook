/* eDailyMenu.java
 * Displays all of the eDailies using a ListView.
 *  On click, it shows a preview of the respective eDaily. 
 */


package com.eNotebook.SATE2012;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class eDailyMenu extends Activity implements View.OnClickListener{
    
	// Navigation buttons and the listview
    Button newedaily, backtomenu, refresh;
    ListView list;
    
    // For Search bar
    EditText searchbar;
    ArrayList<String> arraysort;
    
    // View for when list is empty
    TextView empty;
    
    // List of all the edaily text files
    String[] edailytextpaths = {};
    
    // For data passing between database-app and internal storage-app
    DataPassing dp = new DataPassing();
    
    Toast errormessage;
    
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
        refresh.setOnClickListener(this);
       
        // Set the adapter for the listview
        if (edailytextpaths.length == 0)
        	empty.setVisibility(TextView.VISIBLE);
        
        // Set the adapter for the listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String> 
    			(this, R.layout.simple_list, edailytextpaths);
    	list.setAdapter(adapter);

    	// Set the arraylist to the contents of the main list, for searching later
        arraysort = new ArrayList<String>(Arrays.asList(edailytextpaths));
        
        // Set on click listener (when an item on the list is pressed, go to preview)
        list.setOnItemClickListener(new OnItemClickListener()
        {
        	public void onItemClick(AdapterView<?> a, View v, int position, long id)
        	{
        		// Start the preview page with the corresponding filename
    			Intent previewIntent = new Intent("com.eNotebook.SATE2012." + "EDAILYPREVIEW");
    			previewIntent.putExtra("filename", arraysort.get(position));
    			startActivity(previewIntent);
        	}
        });
        
        // For the search bar
        searchbar.addTextChangedListener(new TextWatcher() {
        	public void afterTextChanged(Editable s) {}
        	
        	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        	
        	// Reset the adapter to the sorted list if the user searches for something  
        	public void onTextChanged(CharSequence s, int start, int before, int count) {
        		
        		// Clear the new array
        		arraysort.clear();
        		
        		// Create the new array filtered through the searchbar
        		for(int i =0; i < edailytextpaths.length; i++)
        		{
        			if((edailytextpaths[i].toLowerCase()).contains(
        					(searchbar.getText().toString().toLowerCase())))
        				arraysort.add(edailytextpaths[i]);
        			
        		}
        		
        		// Set the adapter to the new list that contains filtered material 
        		list.setAdapter(new ArrayAdapter<String>(eDailyMenu.this, 
        												 R.layout.simple_list, arraysort));        	
        	}
        	
        	
        });
    }
    
    // Find the file path and set the global
    private void getPath() {

        // Get the file path of the edailies
        File edailytextpath = new File(getFilesDir(), "Text");
        
        // Check if directory exists
        if(!edailytextpath.exists())
            return;
        else
            edailytextpaths = edailytextpath.list();
    }
    
    // Assign globals (views)
    private void assignedObjects() {
    	// Navigation
        newedaily = (Button) findViewById(R.id.bAdd);
        backtomenu = (Button) findViewById(R.id.bBack);
        refresh = (Button) findViewById(R.id.bRefresh);
        
        // The list display
        list = (ListView) findViewById(R.id.lvDaily);
        
        // For searching through the eDailies
        searchbar = (EditText) findViewById(R.id.etSearch);
  
        // Place holder in case there are no eDailies to display
        empty = (TextView) findViewById(R.id.tvEmptyElement);
    }
    
    // If a button is pressed
    public void onClick(View view) {
    	Intent ourIntent = null;
    	
    	// Back button is pressed
    	if(view.getId() == R.id.bBack)    
    		ourIntent = new Intent("com.eNotebook.SATE2012." + "MENU");
    	// New button is pressed
    	else if (view.getId() == R.id.bAdd)
    	{
	        ourIntent = new Intent("com.eNotebook.SATE2012." + "EDAILY");
	        ourIntent.putExtra("loadInitialText", false);
    	}
    	
    	else
    	{
    		// Check if wifi is connected
    		ConnectivityManager connection = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    		NetworkInfo wifi = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    		
    		if(!wifi.isConnected())
    		{
    			errormessage = Toast.makeText(getApplicationContext(), 
    					"Cannot refresh, please check your Wifi connection.", Toast.LENGTH_LONG);
    			errormessage.show();
    			return;
    		}
    		else
    		{
	    		File textpath = new File(getFilesDir(), "Text");
	    		dp.deleteFiles(textpath);
	    		textpath.mkdir();
	    		
	    		File namepath = new File(getFilesDir(), "UserInformation/name");
	    		
	    		if(!namepath.exists()) // SHOULD NEVER OCCUR
	    			return;
	    		
	    		dp.downloadEDaily(textpath,
	    				 dp.readTextfromFile(namepath.toString()));
	    		
	    		ourIntent = new Intent("com.eNotebook.SATE2012." + "EDAILYMENU");
    		}
    	}
    	
    	// Start the correct activity
    	startActivity(ourIntent);
    }

    
}

