package com.mobilelab.msynthesizer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ViewFlipper;


public class MusicSynthesizer extends Activity {
	 ViewFlipper flipper;
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
       
        /*create buttons for switching between option panels*/
        Button button1 = (Button) findViewById(R.id.panel1B);
        Button button2 = (Button) findViewById(R.id.panel2B);
        Button button3 = (Button) findViewById(R.id.panel3B);
        Button button4 = (Button) findViewById(R.id.panel4B);


        /* if button1 is pressed, display 1st panel*/
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if (flipper.getDisplayedChild() != 0){
                flipper.setInAnimation(inFromRightAnimation());
               // flipper.setOutAnimation(outToRightAnimation());
                flipper.setDisplayedChild(0);  
            	}
            }
        });

        /* if button2 is pressed, display 2st panel*/
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	/*only do animation if panel isn't currently displayed*/
            	if (flipper.getDisplayedChild() != 1){
                flipper.setInAnimation(inFromRightAnimation());
                //flipper.setOutAnimation(outToRightAnimation());
                flipper.setDisplayedChild(1);   
            	}
            }
        });
       
        /* if button3 is pressed, display 3srd panel*/
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	/*only do animation if panel isn't currently displayed*/
            	if (flipper.getDisplayedChild() != 2){
                flipper.setInAnimation(inFromRightAnimation());
                //flipper.setOutAnimation(outToLeftAnimation());
                flipper.setDisplayedChild(2); 
            	}
            }
        });
        
        /* if button4 is pressed, display 4th panel*/
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	/*only do animation if panel isn't currently displayed*/
            	if (flipper.getDisplayedChild() != 3){
                flipper.setInAnimation(inFromRightAnimation());
                //flipper.setOutAnimation(outToLeftAnimation());
                flipper.setDisplayedChild(3); 
            	}
            }
        });
	}
    
    
    /*** inFromRightAnimation
     *   Animation for panels to "slide in" from the right when their button is pressed
     *   @return inFromRight - animation to be used for panels to slide in
     ***/
    private Animation inFromRightAnimation() {
    	Animation inFromRight = new TranslateAnimation(
    	Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
    	Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
    	inFromRight.setDuration(200);
    	inFromRight.setInterpolator(new AccelerateInterpolator());
    	return inFromRight;
    }
  
   /*** outToLeftAnimation
     *   Animation for panels to "slide out" to the left when another panel button is pressed
     *   @return outtoLeft - animation to be used for panels to slide out
     ***/
     private Animation outToLeftAnimation() {
    	Animation outtoLeft = new TranslateAnimation(
    	Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  -1.0f,
    	Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
    	outtoLeft.setDuration(500);
    	outtoLeft.setInterpolator(new AccelerateInterpolator());
    	return outtoLeft;
    }
}   