package com.eNotebook.SATE2012;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class TwoFiftySeven extends Activity implements View.OnClickListener{
	
	WebView myVideo;
	Button back;
	
	Bundle extras;
	
	Toast notify;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.twofiftyseven);
		assignobjects();
		back.setOnClickListener(this);
		
		myVideo.getSettings().setJavaScriptEnabled(true);
		myVideo.getSettings().setPluginsEnabled(true);
		
		String video= "<html><iframe src=\"" + "http://www.youtube.com/embed/" 
						+ extras.getString("videoID") + "\" frameborder=\"0\" allowfullscreen></iframe></html>";
		myVideo.loadData(video, "text/html", "utf-8");
		
		notify = Toast.makeText(getApplicationContext(),
				extras.getString("videoID"), 
				Toast.LENGTH_LONG);
		notify.show();
	}
	
	protected void assignobjects()
	{	
		extras = getIntent().getExtras();
		myVideo = (WebView) findViewById(R.id.wvVideo);
		back = (Button) findViewById(R.id.b257Back);
	}
	
	public void onClick(View v)
	{
		Intent menu = new Intent("com.eNotebook.SATE2012." + "TWOFIFTYSEVENMENU");
		startActivity(menu);
	}
		
}
