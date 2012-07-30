package com.eNotebook.SATE2012;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TwoFiftySevenMenu extends Activity implements View.OnClickListener{

	Button addLink;
	Button back;
	EditText linkbox;
	
	Toast errormessage;
	
	ListView videoMenu;
	
	TextView empty;
	String[] thumbnailurls;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.twofiftysevenmenu);
		assignobjects();
		addLink.setOnClickListener(this);
		displayVideos();
		
		VideoListAdapter adapter = new VideoListAdapter(this, thumbnailurls);
		videoMenu.setAdapter(adapter);
	}
	
	protected void assignobjects()
	{
		addLink = (Button) findViewById(R.id.bAddLink);
		back = (Button) findViewById(R.id.bBack257Menu);
		linkbox = (EditText) findViewById(R.id.etAddLink);
		
		videoMenu = (ListView) findViewById(R.id.lvVideoMenu);
		empty = (TextView) findViewById(R.id.tvEmpty257);
	}

	public void onClick(View view)
	{
        try
        {
            // Find url text
            String newid = linkbox.getText().toString();
            
            // Check for blank url
            if (newid.length() == 0)
            {
            	errormessage = Toast.makeText(getApplicationContext(),
            				"Please enter a valid video ID.", 
            				Toast.LENGTH_LONG);
            	errormessage.show();
            	return;
            }
            
            // Finds the text directory and creates one if none exists
	        File urlpath = new File(getFilesDir(), "TwoFiftySeven");
	        if (!urlpath.exists())
	        	urlpath.mkdir();
        	// Create a new file for the new eDaily
            File newtext = new File(urlpath, newid);
            try 
            { newtext.createNewFile(); }
            catch(IOException e) 
            { e.printStackTrace(); } 
                        
            // Start the preview activity
            Intent restartMenu = new Intent("com.eNotebook.SATE2012." + "TwoFiftySevenMenu");
            startActivity(restartMenu);
        }
        catch (Exception e)
        { e.printStackTrace(); }
	}
	
	public void displayVideos()
	{
		File idpath = new File(getFilesDir(), "TwoFiftySeven");
		String[] ids = {};
		
		if (!idpath.exists())
			return;
			//DISPLAY 
		else
		{
			ids = idpath.list();
			/* declare: urls.length() != 0*/
		
			thumbnailurls = new String[ids.length];
			String[] urls = new String[ids.length];
			for (int i = 0; i < ids.length; i++)
			{
				urls[i] = "http://youtu.be/" + ids[i];
				thumbnailurls[i] = "http://img.youtube.com/vi/" + ids[i] + "/default.jpg";
			}
		}
	}
	
	// Adapter for listview
	public class VideoListAdapter extends BaseAdapter {
	    
	    private Activity activity;
	    private String[] data;
	    private static LayoutInflater inflater=null;
	    
	    public VideoListAdapter(Activity a, String[] d) {
	        activity = a;
	        data=d;
	        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }

	    public int getCount() {
	        return data.length;
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }
	    
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View vi=convertView;
	        if(convertView==null)
	            vi = inflater.inflate(R.layout.item, null);

	        TextView text=(TextView)vi.findViewById(R.id.text);;
	        ImageView image=(ImageView)vi.findViewById(R.id.image);
	        
	        text.setText("item "+position);
	        image.setImageDrawable(getDrawablefromWeb(data[position]));
	        return vi;
	    }
	    
	    public Drawable getDrawablefromWeb(String url)
	    {
	    	try
	    	{
	    		InputStream instream = (InputStream) new URL(url).getContent();
	    		Drawable thumbnail = Drawable.createFromStream(instream, url);
	    		return thumbnail;
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		return null;
	    	}
	    }
	}
	
}
