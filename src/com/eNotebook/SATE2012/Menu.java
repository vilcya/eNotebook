package com.eNotebook.SATE2012;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Menu extends ListActivity implements View.OnClickListener{
    String documents[] = {"eDailies", "eTecNotes", "257"}; 
    String name;
    
    TextView intro;
    Button eDaily;
    Button eTecNotes;
    Button twofiftyseven;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        assignedObjects();
        
        eDaily.setOnClickListener(this);
        eTecNotes.setOnClickListener(this);
        twofiftyseven.setOnClickListener(this);
            
    } 

    private void assignedObjects() {
        // TODO Auto-generated method stub
        intro = (TextView) findViewById(R.id.tvMenuIntro);
        eDaily = (Button) findViewById(R.id.bEDaily);
        eTecNotes = (Button) findViewById(R.id.bETecNotes);
        twofiftyseven = (Button) findViewById(R.id.b257);
    }
    
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch(view.getId()){
        case R.id.bEDaily:
            
            name = documents[0];
            
            break;
        
        case R.id.bETecNotes:
            
            name = documents[1];
            
            break;
        
        case R.id.b257:
            
            name = documents[2];
            
            break;
        
        }
        
        Intent ourIntent = new Intent("com.eNotebook.SATE2012." + name.toUpperCase());
        startActivity(ourIntent);

        
    }
    
}