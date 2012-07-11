package com.eNotebook.SATE2012;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {
	String documents[] = {"eDailies", "eTecNotes", "257s"}; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(Menu.this, 
			android.R.layout.simple_list_item_1, documents));
	} 

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		String clickedItem = documents[position];
		// Look up class given the path
		try 
		{ 	
			Class ourClass = Class.forName
				("com.eNotebook.SATE2012." + clickedItem);
			Intent ourIntent = new Intent(Menu.this, ourClass);
			startActivity(ourIntent);
		}
		catch(ClassNotFoundException e)
		{	e.printStackTrace();  }
	}
}
