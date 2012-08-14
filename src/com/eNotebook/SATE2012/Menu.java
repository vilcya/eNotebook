/* Menu.java
 * Class for the main menu of the application
 */

package com.eNotebook.SATE2012;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Menu extends Activity implements View.OnClickListener{
    
	// Different options of the menu
	String documents[] = {"eDailyMenu", "eTecNoteMenu", "TwoFiftySevenMenu", "Option"}; 
    
	// Title
    TextView intro;
    
    // Navigation buttons
    Button eDaily;
    Button eTecNote;
    Button twofiftyseven;
    Button option;
    
    // To create a Menu context for passing across activities
    private static Menu instance;
    
    public Menu ()
    { instance = this;}
    
    public static Context getContext()
    {
    	return instance;
    }
    
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
        
        // Set the layout and assign views
        setContentView(R.layout.menu);
        assignedObjects();
        
        // Set the onclick listeners
        eDaily.setOnClickListener(this);
        eTecNote.setOnClickListener(this);
        twofiftyseven.setOnClickListener(this);
        option.setOnClickListener(this);
    } 
    
    

    /* Assign layout objects to respective variables  */
    private void assignedObjects() {
    	// Title
    	Typeface tf = Typeface.createFromAsset(getAssets(), "Arr.TTF");
        intro = (TextView) findViewById(R.id.tvMenuIntro);
        intro.setTypeface(tf);
        
        // Navigation
        eDaily = (Button) findViewById(R.id.bEDaily);
        eTecNote = (Button) findViewById(R.id.bETecNote);
        twofiftyseven = (Button) findViewById(R.id.b257);
        option = (Button) findViewById(R.id.bOptions);
    }
    
    /* Triggered when user clicks a view */
    public void onClick(View view) {
    	
    	String name = "";
    	
    	// Checks which button was clicked
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
        
        // Start intent for the corresponding button being clicked
        Intent ourIntent = new Intent("com.eNotebook.SATE2012." + name.toUpperCase());
        startActivity(ourIntent);

        
    }
    
}