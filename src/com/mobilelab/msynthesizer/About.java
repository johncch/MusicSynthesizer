package com.mobilelab.msynthesizer;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.app.Activity;
import android.content.Intent;

public class About extends Activity{
	  /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        

		ImageButton backAboutBtn = (ImageButton) findViewById(R.id.backAbtB);
		
		backAboutBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(About.this, MusicSynthesizer.class);  //intent to about activity
				startActivity(intent);
			}
		});
	}
    
}
