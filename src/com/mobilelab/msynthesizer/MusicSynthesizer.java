package com.mobilelab.msynthesizer;

import com.mobilelab.synth.SynthManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ViewFlipper;

public class MusicSynthesizer extends Activity implements OnTouchListener, OnCheckedChangeListener {
	
	private ViewFlipper flipper;
	private DrawView drawView;
	
	private SynthManager sm;
	
	private RadioGroup waveGroup;
	private RadioButton sqrBtn;
	private RadioButton triBtn;
	private RadioButton sawBtn;
	private RadioButton sinBtn;
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        drawView = (DrawView) findViewById(R.id.XYPad);
       
        drawView.setOnTouchListener(this);
        
        waveGroup = (RadioGroup) this.findViewById(R.id.WaveForms);
        waveGroup.setOnCheckedChangeListener(this);
        sqrBtn = (RadioButton) this.findViewById(R.id.SquareWave);
        triBtn = (RadioButton) this.findViewById(R.id.TriangleWave);
        sawBtn = (RadioButton) this.findViewById(R.id.SawWave);
        sinBtn = (RadioButton) this.findViewById(R.id.SineWave);        
        
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
        
        sm = new SynthManager();
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

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		int action = event.getAction();
		dumpEvent(event);		
		if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
			float f = 400 + event.getY() * 4;
			Log.d("MS", "playing.." + f);
			sm.play(f);
		} else {
			sm.stop();
		}
		
		return true;
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(group.getId() == waveGroup.getId()) {
			if(checkedId == sqrBtn.getId()) {
				sm.setCurrentWaveShape(sm.SQUARE_WAVE);
			} else if (checkedId == triBtn.getId()) {
				sm.setCurrentWaveShape(sm.TRIANGLE_WAVE);
			} else if (checkedId == sawBtn.getId()) {
				sm.setCurrentWaveShape(sm.SAW_WAVE);
			} else {
				sm.setCurrentWaveShape(sm.SINE_WAVE);
			}
		}
	}
	
	private void dumpEvent(MotionEvent event) {
		 String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
		 StringBuilder sb = new StringBuilder();
		 int action = event.getAction();
		 int actionCode = action;
		 sb.append("event ACTION_").append(names[actionCode]);
		 sb.append("[Coord: " + event.getX() + ", " + event.getY() + "]");
		 Log.d("MS", sb.toString());
	 }
}  