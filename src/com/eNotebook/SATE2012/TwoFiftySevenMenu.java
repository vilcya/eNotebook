package com.eNotebook.SATE2012;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class TwoFiftySevenMenu extends Activity implements View.OnClickListener{

	Button addLink;
	EditText linkbox;
	
	WebView myVideo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.twofiftysevenmenu);
		assignobjects();
		addLink.setOnClickListener(this);
		
		myVideo.getSettings().setJavaScriptEnabled(true);
		myVideo.getSettings().setPluginsEnabled(true);
		
		String video= "<html><iframe width=\"140\" height=\"105\" src=\"http://www.youtube.com/embed/Bvu6xVUE9lM\" frameborder=\"0\" allowfullscreen></iframe></html>";
		myVideo.loadData(video, "text/html", "utf-8");
	}
	
	protected void assignobjects()
	{
		addLink = (Button) findViewById(R.id.baddLink);
		linkbox = (EditText) findViewById(R.id.etaddLink);
		
		myVideo = (WebView) findViewById(R.id.wvVideo);
	}
	
	public void onClick(View view)
	{
		
	}
}
