/* eDailyMenu.java
 * Displays all of the eDailies using a ListView.
 *  On click, it shows a preview of the respective eDaily. 
 */


package com.eNotebook.SATE2012;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
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

public class eDailyMenu extends Activity implements View.OnClickListener{
    
	// Navigation buttons and the listview
    Button newedaily, backtomenu;
    ListView list;
    
    // For Search bar
    EditText searchbar;
    int textlength = 0;
    ArrayList<String> arraysort = new ArrayList<String>();
    
    TextView empty;
    
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
        	empty.setVisibility(TextView.VISIBLE);
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
        
        searchbar.addTextChangedListener(new TextWatcher() {
        	public void afterTextChanged(Editable s) {}
        	
        	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        	
        	public void onTextChanged(CharSequence s, int start, int before, int count) {
        		textlength = searchbar.getText().length();
        		arraysort.clear();
        		
        		for(int i =0; i < edailytextpaths.length; i++)
        		{
        			if(textlength <= edailytextpaths[i].length())
        			{
        				if(searchbar.getText().toString().equalsIgnoreCase((String) 
        								edailytextpaths[i].subSequence(0, textlength)))
        				{
        					arraysort.add(edailytextpaths[i]);
        				}
        			}
        		}
        		
        		list.setAdapter(new ArrayAdapter<String>(eDailyMenu.this, 
        												 android.R.layout.simple_list_item_1, arraysort));
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
        searchbar = (EditText) findViewById(R.id.etSearch);
        
        empty = (TextView) findViewById(R.id.tvEmptyElement);
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

