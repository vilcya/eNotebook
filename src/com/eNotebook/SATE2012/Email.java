package com.eNotebook.SATE2012;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
 
public class Email extends Activity implements View.OnClickListener{
        
	Button send, back;
    EditText address, subject, emailtext;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email);
        
        assignedObjects();
        
        send.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    
    private void assignedObjects() {
		
        send = (Button) findViewById(R.id.bSendEmail);
        back = (Button) findViewById(R.id.bEmailBack);
        address = (EditText) findViewById(R.id.emailaddress);
        subject = (EditText) findViewById(R.id.emailsubject);
        emailtext = (EditText) findViewById(R.id.emailtext);
		
	}
    
    
    
    public void onClick(View view) {
        // TODO Auto-generated method stub

    	String path = "file:///data/data/com.eNotebook.SATE2012/files/eDailies/hey.png";
    	Uri screenshotUri = Uri.parse(path);
    	
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{address.getText().toString()});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject.getText());
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailtext.getText());
		emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		emailIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
		emailIntent.setType("image/png");
		Email.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		
		
		if (view.getId() == R.id.bEmailBack){
            Intent backIntent = new Intent("com.eNotebook.SATE2012." + "EDAILYMENU");
            startActivity(backIntent);
        }
}
}
    


	

