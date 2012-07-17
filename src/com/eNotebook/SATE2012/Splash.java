package com.eNotebook.SATE2012;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Splash extends Activity {

	//MediaPlayer ourSong;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		//ourSong = MediaPlayer.create(Splash.this, R.raw.burnitdown);
		//ourSong.start();
		Thread timer = new Thread(){
			
			public void run(){
				try{
					sleep(1000);
				} 
				catch (InterruptedException e){	
					e.printStackTrace();
				} 
				finally{
					Intent openMain = new Intent("com.eNotebook.SATE2012.MENU");
					startActivity(openMain);
				}
				
			}
			
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//ourSong.release();
		finish();
	}

	
	
}