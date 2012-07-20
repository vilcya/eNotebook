package com.eNotebook.SATE2012;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Menu extends Activity implements View.OnClickListener{
    String documents[] = {"eDailyMenu", "eTecNote", "257", "Option"}; 
    String name;
    
    TextView intro;
    Button eDaily;
    Button eTecNote;
    Button twofiftyseven;
    Button option;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Finds the user information directory and creates one if none exists
		File userpath = new File(getFilesDir(), "UserInformation/name");
        if (!userpath.exists())
        {
        	Intent ourIntent = new Intent("com.eNotebook.SATE2012." + "OPTION");
            startActivity(ourIntent);
        }
        	
        setContentView(R.layout.menu);
        
        assignedObjects();
        
        eDaily.setOnClickListener(this);
        eTecNote.setOnClickListener(this);
        twofiftyseven.setOnClickListener(this);
        option.setOnClickListener(this);
            
    } 

    /* Assign layout objects to respective variables  */
    private void assignedObjects() {
    	// Title
        intro = (TextView) findViewById(R.id.tvMenuIntro);
        
        // Navigation
        eDaily = (Button) findViewById(R.id.bEDaily);
        eTecNote = (Button) findViewById(R.id.bETecNote);
        twofiftyseven = (Button) findViewById(R.id.b257);
        option = (Button) findViewById(R.id.bOptions);
    }
    
    /* Triggered when user clicks a view */
    public void onClick(View view) {
    	
        switch(view.getId()){
	        case R.id.bEDaily:
	            name = documents[0];
	            break;
	        case R.id.bETecNote:
	            name = documents[1];
	            break;
	        case R.id.b257:
	            name = documents[2];	            
	            break;
	        case R.id.bOptions:
	        	name = documents[3];
	        	break;
        }
        
        Intent ourIntent = new Intent("com.eNotebook.SATE2012." + name.toUpperCase());
        startActivity(ourIntent);

        
    }
    
}