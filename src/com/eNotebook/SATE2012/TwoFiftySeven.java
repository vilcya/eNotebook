/* TwoFiftySeven.java
 * 
 * Currently has no database interaction - 
 *  want to be able to pull list of videos from database
 *  and load them in the application for browsing.
 *
 */

package com.eNotebook.SATE2012;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;

public class TwoFiftySeven extends Activity {
	
	// Initialize webview
	WebView myVideo;
	
	// Initialize bundle for storing info between activities
	Bundle extras;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.twofiftyseven);
		assignobjects();
		
		// Allow Javascript and plugins
		myVideo.getSettings().setJavaScriptEnabled(true);
		myVideo.getSettings().setPluginsEnabled(true);
		
		// Embedded video url
		String video="<html><iframe width=\"150\" height=\"100\" src=\"" + "http://www.youtube.com/embed/" 
						+ extras.getString("videoID") + "?rel=0&fs=0\" frameborder=\"0\"></iframe></html>";
		// Load it onto the webview
		myVideo.loadData(video, "text/html", "utf-8");
	}
	
	// Assign global variables
	protected void assignobjects()
	{	
		extras = getIntent().getExtras();
		
		myVideo = (WebView) findViewById(R.id.wvVideo);
		myVideo.setBackgroundColor(Color.TRANSPARENT);
	}
	
}
