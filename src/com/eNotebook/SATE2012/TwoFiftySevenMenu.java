package com.eNotebook.SATE2012;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TwoFiftySevenMenu extends Activity implements View.OnClickListener{

	// Navigation buttons 
	Button addLink, back;
	
	// For searchbar
	EditText searchbar;
	ArrayList<String> thumbnailsort, namesort;

	// For displaying the videos
	ListView videoMenu;
	VideoListAdapter adapter;
	
	// When there are no videos
	TextView empty;
	
	// Thumbnail urls, id, and name of youtube videos
	String[] thumbnailurls, ids, names;
	
	
	// For loading page
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.twofiftysevenmenu);
		assignobjects();
		// Function that sets all of the lists
		setVideoUrls();
		
		// Listens for click
		addLink.setOnClickListener(this);
		back.setOnClickListener(this);
		
		// Check wifi
		ConnectivityManager connection = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		// If wifi is not connected
		if (!wifi.isConnected()) {			   
			empty.setVisibility(TextView.VISIBLE);
			empty.setText("Please connect to WiFi to view your videos.");
		}
		// Else if there are no videos to display
		else if (ids == null)
		{
			empty.setVisibility(TextView.VISIBLE);
			empty.setText("You have no videos. Press the plus to add one.");
		}
		// Otherwise display all the videos
		else {		
			// Creates arraylists for searchbar sorting
			namesort = new ArrayList<String>(Arrays.asList(names));
			thumbnailsort = new ArrayList<String> (Arrays.asList(thumbnailurls));
			
			// Make and set the adapter
			adapter = new VideoListAdapter(this, thumbnailsort, namesort);
			videoMenu.setAdapter(adapter);
			// Set on click listener (when an item on the list is pressed, go to preview)
	        videoMenu.setOnItemClickListener(new OnItemClickListener()
	        {
	        	public void onItemClick(AdapterView<?> a, View v, int position, long id)
	        	{
	        		// Start the preview page with the corresponding id
	    			Intent previewIntent = new Intent("com.eNotebook.SATE2012." + "TWOFIFTYSEVEN");
	    			// Listview may be different (after searching, so find the id)
	    			ArrayList <String> dummy = new ArrayList<String>(Arrays.asList(names));
	    			previewIntent.putExtra("videoID", "" + ids[dummy.indexOf(namesort.get((int)id))]);
	    			// Go to preview page
	    			startActivity(previewIntent);
	        	}
	        });
	        
	        
	        // Search bar functionality
	        searchbar.addTextChangedListener(new TextWatcher() {
	        	public void afterTextChanged(Editable s) {}
	        	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	        	
	        	// When text changes, look through the list and filter  
	        	public void onTextChanged(CharSequence s, int start, int before, int count) {
	        		
	        		// Clear the arrays
	        		thumbnailsort.clear();
	        		namesort.clear();
	        		
	        		// Create the new array filtered through the searchbar
	        		for(int i =0; i < names.length; i++)
	        		{
	        			if((names[i].toLowerCase()).contains(
	        					(searchbar.getText().toString().toLowerCase())))
	        			{
	        				thumbnailsort.add(thumbnailurls[i]);
	        				namesort.add(names[i]);
	        			}
	        		}
	        		
	        		// Notify the adapter that we changed the array
	        		adapter.notifyDataSetChanged();
	        	}
	        });
		}
	}
	
	// Assign globals and views
	protected void assignobjects()
	{
		addLink = (Button) findViewById(R.id.bAddLink);
		back = (Button) findViewById(R.id.bBack257Menu);
		searchbar = (EditText) findViewById(R.id.etAddLink);
		
		videoMenu = (ListView) findViewById(R.id.lvVideoMenu);
		empty = (TextView) findViewById(R.id.tvEmpty257);
	}

	// Event that runs when button is clicked
	public void onClick(View view)
	{
		Intent ourIntent;
		
		// Check which button was clicked
		if(view.getId() == R.id.bBack257Menu)
			ourIntent = new Intent("com.eNotebook.SATE2012." + "MENU");
		else
			ourIntent = new Intent("com.eNotebook.SATE2012." + "TWOFIFTYSEVENADD");
		
		startActivity(ourIntent);
	}
	
	
	// Set the global arrays
	public void setVideoUrls()
	{
		// Find the path
		File idpath = new File(getFilesDir(), "TwoFiftySeven");
		
		if (!idpath.exists()) // This should never occur
			return;
		else
		{
			// Find all of the files (filename is id)
			ids = idpath.list();
		
			// Initialize and put in the correct urls for thumbnail pictures
			thumbnailurls = new String[ids.length];
			for (int i = 0; i < ids.length; i++)
				thumbnailurls[i] = "http://img.youtube.com/vi/" + ids[i] + "/default.jpg";
		}
		
		// Parse and find all of the names of the video
		names = new String[thumbnailurls.length];
		for (int i = 0; i < names.length; i++)
			names[i] = getNamefromUrl("http://youtu.be/" + ids[i]);

	}
	
	// Function that looks at the webpage and parses out the name of video
	public String getNamefromUrl(String url)
	{
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		try
		{
			// Connect and read line by line
			URL page = new URL(url);
			URLConnection connection = page.openConnection();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			for(String line; (line = reader.readLine()) != null;)
				builder.append(line.trim());
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return "";
		}
		finally 
		{
			if (reader != null)
			{
				try {reader.close();}
				catch(IOException e) {e.printStackTrace();}
			}
		}
		
		// Parse out the string for the title of the video
		String begin = "<div class=\"title\">";
		String end = "</div>";
		String part = builder.substring(builder.indexOf(begin) + begin.length());
	    return part.substring(0, part.indexOf(end));
	}
	

	//Adapter for listview
	public class VideoListAdapter extends BaseAdapter {
	    
	    private Activity activity;
	    private ArrayList<String> data, datatext;
	    private LayoutInflater inflater=null;
	    
	    // Constructor for setting globals
	    public VideoListAdapter(Activity a, ArrayList<String> d, ArrayList<String> n) {
	        activity = a;
	        data=d;
	        datatext=n;
	        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }

	    // Returns the size
	    public int getCount() {
	        return data.size();
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }
	    
	    // Sets the layout for the listview object
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View vi=convertView;
	        if(convertView==null)
	            vi = inflater.inflate(R.layout.listview257, null);

	        // Find the views
	        TextView text=(TextView)vi.findViewById(R.id.tvList257Text);
	        ImageView image=(ImageView)vi.findViewById(R.id.ivList257Image);
	        
	        // Set the data
	        text.setText(datatext.get(position));
	        image.setImageDrawable(getDrawablefromWeb(data.get(position)));
	        return vi;
	    }
	    
	    @Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			// Change the data set
			data = thumbnailsort;
			datatext = namesort;
		}

	    // Function that takes the image from the website given the url
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
