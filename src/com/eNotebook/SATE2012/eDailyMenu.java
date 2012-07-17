package com.eNotebook.SATE2012;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class eDailyMenu extends Activity implements View.OnClickListener{
    
    Button editeDaily;
    TextView mydummytext;
    Gallery edailygall;
    
    // Folder with all the edailies
    File edailypath;
    // List of all the edaily files
    File[] edailies;
    
    // List of bitmaps to add to the gallery
    Bitmap[] edailyimages;
    String[] edailytextpaths;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        setContentView(R.layout.edailymenu);
        
        assignedObjects();
        getPath();
        
        editeDaily.setOnClickListener(this);
        
        // Gallery
        if (edailyimages != null)
            edailygall.setAdapter(new ImageAdapter(this));
        edailygall.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String eDailyData = readTextfromFile(edailytextpaths[(int)id]);
                String[] eDailyComponents = eDailyData.split("*****");
                setContentView(R.layout.edailyedit);
                
                EditText date = (EditText) findViewById(R.id.etDate);
                EditText name = (EditText) findViewById(R.id.etName);
                EditText accomplishedtoday = (EditText) findViewById(R.id.etAccToday);
                EditText accomplishedtomorrow = (EditText) findViewById(R.id.etAccTomorrow);
                
                date.setText(eDailyComponents[0]);
                name.setText(eDailyComponents[1]);
                accomplishedtoday.setText(eDailyComponents[2]);
                accomplishedtomorrow.setText(eDailyComponents[3]);
            }
            
        });

    }
    
    private String readTextfromFile(String path)
    {
        File edailyfile = new File(path);
        FileInputStream instream;
        InputStreamReader instreamread;
        BufferedReader buf;
        String data = "";
        String tmp = "";
        
        try 
        { 
            instream = new FileInputStream(edailyfile);
            instreamread = new InputStreamReader (instream);
            buf = new BufferedReader(instreamread);

            try
            {
                while((tmp = buf.readLine()) != null)
                    data += tmp;
            }
            catch(IOException e)
            { e.printStackTrace(); }
            
        }
        catch(FileNotFoundException e)
        { e.printStackTrace(); }
        
        return data;
    }
    
    private void getPath() {
        // Create metrics
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        // Get the file path of the edailies
        edailypath = new File(getFilesDir(), "eDailies");
        
        // If the directory does not exist, make it
        if(!edailypath.exists())
        {
            mydummytext.setText("Sorry, you have no eDailies. I'm at " + edailypath.toString());
            return;
        }
        else
        {
            edailies = edailypath.listFiles();
            
            if(edailies == null)
            {
                mydummytext.setText("edailies is not a directory - your code should not be doing this");
                return;
            }
            // Go through each edaily and display them
            mydummytext.setText("" + edailies.length);
            edailyimages = new Bitmap[edailies.length];
            edailytextpaths = new String[edailies.length];
            for (int i = 0; i < edailies.length; i++)
            {
                File edaily = edailies[i];
                
                mydummytext.setText("" + edaily.toString() + " I have " + edailies.length + " edailies.");
                
                // Find the edaily and put it on the image view
                Bitmap bmedaily = BitmapFactory.decodeFile(edaily.getAbsolutePath());
                bmedaily.setDensity(metrics.densityDpi);
                
                edailyimages[i] = bmedaily;
                
                updateTextPathsList(edaily.toString(), i);
            }
        }
        
        
    }
    
    private void updateTextPathsList(String edailypath, int i)
    {
        edailytextpaths[i] = edailypath.replaceAll("eDailies", "Text");
    }
    
    private void assignedObjects() {
        mydummytext = (TextView) findViewById(R.id.tvedaily);
        editeDaily = (Button) findViewById(R.id.ibAdd);
        edailygall = (Gallery) findViewById(R.id.gedailies);
    }
    
    public void onClick(View view) {
        
        if (view.getId() == R.id.ibAdd){
            Intent ourIntent = new Intent("com.eNotebook.SATE2012." + "EDAILY");
            startActivity(ourIntent);
        }
        
    }
    
    public class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {
            mContext = c;
        }
 
        public int getCount() {
            return edailyimages.length;
        }
 
        public Object getItem(int position) {
            return position;
        }
 
        public long getItemId(int position) {
            return position;
        }
 
        public View getView(int position, View convertView, ViewGroup parent) {
 
            ImageView i = new ImageView(mContext);
 
            Bitmap curedaily = edailyimages[position];
            i.setImageBitmap(curedaily);
            i.setLayoutParams(new Gallery.LayoutParams(curedaily.getWidth(), curedaily.getHeight()));
            return i;
        }
 
        private Context mContext;
 
    }
}

