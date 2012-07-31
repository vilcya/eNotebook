package com.eNotebook.SATE2012;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TwoFiftySevenMenu extends Activity implements View.OnClickListener{

	// Navigation buttons 
	Button addLink;
	Button back;
	// For adding a new video
	EditText searchbar;
	
	// For displaying the videos
	ListView videoMenu;
	
	// When there are no videos
	TextView empty;
	
	// Thumbnail urls of all youtube videos
	String[] thumbnailurls;
	String[] urls;
	String[] ids;
	String[] names;
	
	Toast notify;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.twofiftysevenmenu);
		assignobjects();
		setVideoUrls();
		addLink.setOnClickListener(this);
		back.setOnClickListener(this);
		
		// Check wifi
		ConnectivityManager connection = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		
		
		if (!wifi.isConnected()) {			   
			empty.setVisibility(TextView.VISIBLE);
			empty.setText("Please connect to WiFi to view your videos.");
		}
		else if (ids == null)
		{
			empty.setVisibility(TextView.VISIBLE);
			empty.setText("You have no videos. Press the plus to add one.");
		}
		else {		
			VideoListAdapter adapter = new VideoListAdapter(this, thumbnailurls);
			videoMenu.setAdapter(adapter);
			// Set on click listener (when an item on the list is pressed, go to preview)
	        videoMenu.setOnItemClickListener(new OnItemClickListener()
	        {
	        	public void onItemClick(AdapterView<?> a, View v, int position, long id)
	        	{
	        		// Start the preview page with the corresponding filename
	    			Intent previewIntent = new Intent("com.eNotebook.SATE2012." + "TWOFIFTYSEVEN");
	    			previewIntent.putExtra("videoID", "" + ids[(int)id]);
	    			startActivity(previewIntent);
	        	}
	        });
		}

	}
	
	protected void assignobjects()
	{
		addLink = (Button) findViewById(R.id.bAddLink);
		back = (Button) findViewById(R.id.bBack257Menu);
		searchbar = (EditText) findViewById(R.id.etAddLink);
		
		videoMenu = (ListView) findViewById(R.id.lvVideoMenu);
		empty = (TextView) findViewById(R.id.tvEmpty257);
	}

	public void onClick(View view)
	{
		Intent ourIntent;
		if(view.getId() == R.id.bBack257Menu)
			ourIntent = new Intent("com.eNotebook.SATE2012." + "MENU");
		else
			ourIntent = new Intent("com.eNotebook.SATE2012." + "TWOFIFTYSEVENADD");
		
		startActivity(ourIntent);
		
	}
	
	public void setVideoUrls()
	{
		File idpath = new File(getFilesDir(), "TwoFiftySeven");
		
		if (!idpath.exists())
			return;
			//DISPLAY 
		else
		{
			ids = idpath.list();
			/* declare: urls.length() != 0*/
		
			thumbnailurls = new String[ids.length];
			urls = new String[ids.length];
			for (int i = 0; i < ids.length; i++)
			{
				urls[i] = "http://youtu.be/" + ids[i];
				thumbnailurls[i] = "http://img.youtube.com/vi/" + ids[i] + "/default.jpg";
			}
		}
		
		names = new String[urls.length];
		for (int i = 0; i < urls.length; i++)
			names[i] = getNamefromUrl(urls[i]);
	}
	
	public String getNamefromUrl(String url)
	{
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		try
		{
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
		
		String begin = "<div class=\"title\">";
		String end = "</div>";
		String part = builder.substring(builder.indexOf(begin) + begin.length());
	    return part.substring(0, part.indexOf(end));
	}
	
	// Adapter for listview
	public class VideoListAdapter extends BaseAdapter {
	    
	    private Activity activity;
	    private String[] data;
	    private LayoutInflater inflater=null;
	    
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
	            vi = inflater.inflate(R.layout.listview257, null);

	        TextView text=(TextView)vi.findViewById(R.id.tvList257Text);;
	        ImageView image=(ImageView)vi.findViewById(R.id.ivList257Image);
	        
	        text.setText(names[position]);
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
