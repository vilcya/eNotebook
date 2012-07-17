package com.eNotebook.SATE2012;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class eDaily extends Activity implements View.OnClickListener{

    Button save;
    Button back;
    TextView description;
    RelativeLayout layoutedaily;
    
    
    // Text views inside the template
    EditText date;
    EditText name;
    EditText accomplishedtoday;
    EditText accomplishedtomorrow;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edailyedit);
        
        assignedObjects();
    
        save.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    
    private void assignedObjects()
    {
        save = (Button) findViewById(R.id.bSave);
        back = (Button) findViewById(R.id.bEDailyBack);
        description = (TextView) findViewById(R.id.tvDisplayPath);
        layoutedaily = (RelativeLayout) findViewById(R.id.leDaily);
        
        date = (EditText) findViewById(R.id.etDate);
        name = (EditText) findViewById(R.id.etName);
        accomplishedtoday = (EditText) findViewById(R.id.etAccToday);
        accomplishedtomorrow = (EditText) findViewById(R.id.etAccTomorrow);
    }

    
    
    public void onClick(View view)
    {
        if (view.getId() == R.id.bEDailyBack){
            Intent ourIntent = new Intent("com.eNotebook.SATE2012." + "EDAILYMENU");
            startActivity(ourIntent);
        }
        
        else{
            
            // Create a bitmap out of the view
            layoutedaily.setDrawingCacheEnabled(true);
            Bitmap bmpedaily = layoutedaily.getDrawingCache();
            
            /* Find the path to save and create a directory if one
               does not exist */
            // For the image path
            File edailypath = new File(getFilesDir(), "eDailies");
            if (!edailypath.exists())
                edailypath.mkdir();
            
            // For the text path 
            File textpath = new File(getFilesDir(), "Text");
            if (!textpath.exists())
                textpath.mkdir();
            
            // Create a new file for the new eDaily
            if (date.getText().toString().length() == 0)
                description.setText("The date was not specified, please specify the date and try again.");
            else
            {
                File newedaily = new File(edailypath, date.getText().toString());
                File newtext = new File(textpath, date.getText().toString());
                try 
                { 
                    newedaily.createNewFile();
                    newtext.createNewFile();
                }
                catch(IOException e) 
                { e.printStackTrace(); } 
                 
                
                // Open the file stream and copy the image into the file
                try
                {
                    description.setText("I'm saving the picture at " + newedaily.toString());
                    FileOutputStream ostream = new FileOutputStream(newedaily);
                    bmpedaily.compress(CompressFormat.PNG, 100, (OutputStream)ostream);
                    
                    FileOutputStream ostream2 = new FileOutputStream(newtext);
                    String edailytext = date.getText().toString() + "*****" + 
                                        name.getText().toString() + "*****" +
                                        accomplishedtoday.getText().toString() + "*****" +
                                        accomplishedtomorrow.getText().toString();
                    ostream2.write(edailytext.getBytes());
                    ostream2.close();
                }
                catch (Exception e)
                { e.printStackTrace(); }
            }
        }
    
    }
    
    
}
